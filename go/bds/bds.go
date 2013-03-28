/* 
	Execute BigDataScript:

	This command line program allow two execution types

	Commands:
		1) default (no command)	:	Execute main Java package (compiler and interpreter)
		2) exec					:	Execute shell scripts, set maimum execution time, redirect STDOUT and STDERR, write exit code to a file
									bds exec timeout file.stdout file.stderr file.exit command arguments...
		3) help					: 	Show command usage and exit
		4) kill pid             :   Send a kill signal to a process group (same as shell command "kill -- -pid")

	Examples:

	This will load, compile and execute 'myprogram.bds' (BigDataScript program)
		$ bds myprogram.bds

	This will execute "ls -al", redirect standard output and standard error 
	to "out.stdout" and "out.stderr" respectively. When the command finishes, the 
	exit code will be written to "out.exit"

		$ bds exec 10 out.stdout out.stderr out.exit ls -al
*/
package main

import (
	"bufio"
	"bytes"
	"fmt"
	"io"
	"log"
	"os"
	"os/exec"
	"os/signal"
	"strconv"
	"strings"
	"syscall"
	"time"
)

// Store all PID in this file
var pidFile string = ""

/*
	Main
*/
func main() {

	// Are we requested to execute a command?
	if len(os.Args) > 1 {
		if os.Args[1] == "exec" {
			// Execute command and exit
			os.Exit(executeCommandArgs())
		} else if os.Args[1] == "kill" {
			// Kill a process group
			if len(os.Args) != 3 {
				usage("Invalid number of parameters for 'kill' command")
			}

			// Parse pid
			pidStr := os.Args[2]
			pid, err := strconv.Atoi(pidStr)
			if err != nil {
				log.Fatalf("Invalid PID: '%s'\n", pidStr)
			}

			// Kill and exit
			killProcessGroup(pid)
			os.Exit(0)
		} else if os.Args[1] == "help" {
			usage("")
		}
	}

	// Execute BigDataScript.jar
	os.Exit(bigDataScript())
}

/*
	Invoke BigDataScript java program
	WARNING: It is assumed that BigDataScript.jar is in the CLASSPATH
*/
func bigDataScript() int {
	// Create a pidFile (temp file based on pid number)
	prefix := "bds.pid." + strconv.Itoa(syscall.Getpid())
	pidTmpFile, err := tempFile(prefix)
	if err != nil {
		log.Fatal(err)
	}
	pidFile = pidTmpFile

	// Append all arguments from command line
	args := []string{"java", "-Xmx1G", "ca.mcgill.mcb.pcingola.bigDataScript.BigDataScript"}
	args = append(args, "-pid")
	args = append(args, pidFile)
	for _, arg := range os.Args[1:] {
		args = append(args, arg)
	}

	// Execute command
	exitCode := executeCommand("java", args, 0, "", "", "")

	// Kill all pending processes
	killAll(pidFile)

	return exitCode
}

/*
	Execute a command
*/
func executeCommandArgs() int {
	if len(os.Args) < 7 {
		usage("Invalid numeber of parameters for 'exec' command")
	}

	// Parse command line args
	cmdIdx := 2
	timeStr := os.Args[cmdIdx]
	cmdIdx = cmdIdx + 1
	outFile := os.Args[cmdIdx]
	cmdIdx = cmdIdx + 1
	errFile := os.Args[cmdIdx]
	cmdIdx = cmdIdx + 1
	exitFile := os.Args[cmdIdx]
	cmdIdx = cmdIdx + 1
	command := os.Args[cmdIdx]
	cmdIdx = cmdIdx + 1

	// Append other arguments
	args := []string{command}
	for _, arg := range os.Args[7:] {
		args = append(args, arg)
	}

	// Parse time argument
	timeSecs, err := strconv.Atoi(timeStr)
	if err != nil {
		log.Fatalf("Invalid time: '%s'\n", timeStr)
	}

	// Show PID info (parent process is expecting this line first)
	fmt.Printf("%d\n", syscall.Getpid())
	os.Stdout.Sync()

	// Execute command
	return executeCommand(command, args, timeSecs, outFile, errFile, exitFile)
}

/*
	Execute a command (using arguments 'args')
	Redirect stdout to outFile (unless file name is empty)
	Redirect stderr to errFile (unless file name is empty)
	Write exit code to exitFile (unless file name is empty)
	Timeout after timeout seconds (unless time is zero)
*/
func executeCommand(command string, args []string, timeSecs int, outFile, errFile, exitFile string) int {

	// Set a new process group.
	// Since we want to killall child processes, we'll send a kill signal to this process group.
	// But we don't want to kill the calling program...
	if err := syscall.Setpgid(0, 0); err != nil {
		log.Fatal(err)
	}

	// Redirect all signals to channel (e.g. Ctrl-C)
	osSignal := make(chan os.Signal, 1)
	signal.Notify(osSignal, os.Interrupt)

	// Create command
	cmd := exec.Command(command)
	cmd.Args = args

	stdout, err := cmd.StdoutPipe()
	if err != nil {
		log.Fatal(err)
	}

	stderr, err := cmd.StderrPipe()
	if err != nil {
		log.Fatal(err)
	}

	// Start process
	err = cmd.Start()
	if err != nil {
		log.Fatal(err)
	}

	// Copy to STDOUT to file (or to stdout)
	if (outFile == "") || (outFile == "-") {
		go tee(os.Stdout, stdout, false)
	} else {
		stdoutFile, err := os.Create(outFile)
		if err != nil {
			log.Fatal(err)
		}
		defer stdoutFile.Close()
		go tee(stdoutFile, stdout, false)
	}

	// Copy to STDERR to file (or to stderr)
	if (errFile == "") || (errFile == "-") {
		go tee(os.Stderr, stderr, true)
	} else {
		stderrFile, err := os.Create(errFile)
		if err != nil {
			log.Fatal(err)
		}
		defer stderrFile.Close()
		go tee(stderrFile, stderr, true)
	}

	return executeCommandTimeout(cmd, timeSecs, exitFile, osSignal)
}

/*
	Execute a command enforcing a timeout and writing exit status to 'exitFile'
*/
func executeCommandTimeout(cmd *exec.Cmd, timeSecs int, exitFile string, osSignal chan os.Signal) int {

	// Wait for execution to finish or timeout
	exitStr := ""
	if timeSecs <= 0 {
		timeSecs = 31536000 // Default: One year
	}

	// Create a timeout process
	// References: http://blog.golang.org/2010/09/go-concurrency-patterns-timing-out-and.html
	exitCode := make(chan string, 1)
	go execute(cmd, exitCode)

	// Wait until executions ends, timeout or OS signal
	kill := false
	select {
	case exitStr = <-exitCode:
		kill = false

	case <-time.After(time.Duration(timeSecs) * time.Second):
		kill = true
		exitStr = "Time out"

	case <-osSignal:
		kill = true
		exitStr = "Signal received"
	}

	// Should we kill child process?
	if kill {
		cmd.Process.Kill()
		cmd.Process.Wait() // Reap their souls
	}

	// Write exitCode to file or show as log message
	if (exitFile != "") && (exitFile != "-") {
		writeFile(exitFile, exitStr) // Dump error to 'exitFile'
	}

	if kill {
		// Should we kill all process groups from pidFile?
		if pidFile != "" {
			killAll(pidFile)
		}

		// Send a SIGKILL to the process group (just in case any child process is still executing)
		syscall.Kill(0, syscall.SIGHUP) // Other options: -syscall.Getpgrp() , syscall.SIGKILL
	}

	// OK? exit value should be zero
	if exitStr == "0" {
		return 0
	}
	return 1
}

/*
	Execute a command and writing exit status to 'exitCode'
*/
func execute(cmd *exec.Cmd, exitCode chan string) {
	// Wait for command to finish
	if err := cmd.Wait(); err != nil {
		exitCode <- err.Error()
	}

	exitCode <- "0"
}

/*
	Parse pidFile and send kill signal to all process groups that have not been marked as 'finished'
	File format: 
		"pid \t {+,-} \n"

	where '+' inidicates the process was started and '-' that 
	the process finished. So all pid that do not have a '-' entry 
	must be killed.
*/
func killAll(pidFile string) {
	var (
		pid  int
		err  error
		line string
		pids map[int]bool
		file *os.File
	)

	//---
	// Open file and parse it
	//---
	pids = make(map[int]bool)

	if file, err = os.Open(pidFile); err != nil {
		return
	}
	defer file.Close()

	// Read line by line
	reader := bufio.NewReader(file)
	for {
		if line, err = readLine(reader); err != nil {
			break
		}
		recs := strings.Split(line, "\t")

		pid, _ = strconv.Atoi(recs[0])

		// Add or remove from map
		if recs[0] == "-" {
			delete(pids, pid)
		} else {
			pids[pid] = true
		}
	}

	// Kill all pending processes
	for pid, running := range pids {
		if running {
			killProcessGroup(pid)
		}
	}
}

/*
	Kill a process group
*/
func killProcessGroup(pid int) {
	syscall.Kill(-pid, syscall.SIGHUP)
}

/*
	Read a line from a file
*/
func readLine(reader *bufio.Reader) (line string, err error) {
	var part []byte
	var prefix bool

	buffer := bytes.NewBuffer(make([]byte, 0))
	for {
		if part, prefix, err = reader.ReadLine(); err != nil {
			break
		}
		buffer.Write(part)
		if !prefix {
			line = buffer.String()
			return
		}
	}
	return
}

/* 
	Write a string to a file 
*/
func writeFile(fileName, message string) {
	file, err := os.Create(fileName)
	if err != nil {
		log.Fatal(err)
	}
	defer file.Close()
	file.WriteString(message)
}

// 
// tee: Copy to file AND stdout (or stderr)
// 
// This code adapted from io.Copy function
//		http://golang.org/src/pkg/io/io.go?s=11569:11629#L338
//
// Tee: Copies from one file to another, but also prints
// to stdout/stderr
//
// ---
//
// Original comments:
// Copy copies from src to dst until either EOF is reached
// on src or an error occurs.  It returns the number of bytes
// copied and the first error encountered while copying, if any.
//
// A successful Copy returns err == nil, not err == EOF.
// Because Copy is defined to read from src until EOF, it does
// not treat an EOF from Read as an error to be reported.
//
// If dst implements the ReaderFrom interface,
// the copy is implemented by calling dst.ReadFrom(src).
// Otherwise, if src implements the WriterTo interface,
// the copy is implemented by calling src.WriteTo(dst).
func tee(dst io.Writer, src io.Reader, useStdErr bool) (written int64, err error) {
	buf := make([]byte, 32*1024)
	for {
		nr, er := src.Read(buf)
		if nr > 0 {
			nw, ew := dst.Write(buf[0:nr])
			if nw > 0 {
				written += int64(nw)

				// Also write to stdout / stderr
				if useStdErr {
					if dst != os.Stderr { // Don't copy twice
						os.Stderr.Write(buf[0:nr])
					}
				} else {
					if dst != os.Stdout { // Don't copy twice
						os.Stdout.Write(buf[0:nr])
					}
				}
			}
			if ew != nil {
				err = ew
				break
			}
			if nr != nw {
				err = io.ErrShortWrite
				break
			}
		}
		if er == io.EOF {
			break
		}
		if er != nil {
			err = er
			break
		}
	}
	return written, err
}

//
// Create a temp file. Retun file name instead of file descriptor
//
// Code adapted from ioutil.TempFile 
// Ref: http://golang.org/src/pkg/io/ioutil/tempfile.go
//
// TempFile creates a new temporary file in the directory dir
// with a name beginning with prefix, opens the file for reading
// and writing, and returns the resulting *os.File.
// If dir is the empty string, TempFile uses the default directory
// for temporary files (see os.TempDir).
// Multiple programs calling TempFile simultaneously
// will not choose the same file.  The caller can use f.Name()
// to find the name of the file.  It is the caller's responsibility to
// remove the file when no longer needed.
func tempFile(prefix string) (name string, err error) {
	name = prefix

	// Is just the prefix OK?
	if !fileExists(prefix) {
		return
	}

	nconflict := 0
	for i := 0; i < 10000; i++ {
		name = prefix + "." + nextSuffix()
		if fileExists(name) {
			if nconflict++; nconflict > 10 {
				randTempFile = reseed()
			}
			continue
		}
		break
	}
	return
}

// Does the file exist?
func fileExists(name string) bool {
	f, err := os.OpenFile(name, os.O_RDWR|os.O_CREATE|os.O_EXCL, 0600)
	defer f.Close()
	return os.IsExist(err)
}

// Code from ioutil
var randTempFile uint32

// Code from ioutil
func reseed() uint32 {
	return uint32(time.Now().UnixNano() + int64(os.Getpid()))
}

// Code from ioutil
func nextSuffix() string {
	r := randTempFile
	if r == 0 {
		r = reseed()
	}
	r = r*1664525 + 1013904223 // constants from Numerical Recipes
	randTempFile = r
	return strconv.Itoa(int(1e9 + r%1e9))[1:]
}

/* 
	Show usage message and exit
*/
func usage(msg string) {
	if( msg != "" ) {
		fmt.Printf("Error: %s\n", msg)
		fmt.Printf("Arguments:\n", msg)
		for n, arg := range os.Args[1:] {
			fmt.Printf("\t%d : %s\n", n, arg );
		}
	}

	// Show help and exit
	fmt.Printf("Usage: bds command\n\n")
	fmt.Printf("Commands:\n\n")
	fmt.Printf("  default :  Execute BigDataScript Java program (compiler and interpreter)\n")
	fmt.Printf("             Syntax:\n")
	fmt.Printf("                 bds [options] program.bds\n\n")
	fmt.Printf("  exec    :  Execute shell scripts. Set maimum execution time.\n")
	fmt.Printf("             Redirect STDOUT and STDERR to files and write exit code to a file.\n")
	fmt.Printf("             If any file name is '-' it is ignored (not redirected).\n")
	fmt.Printf("             Syntax:\n")
	fmt.Printf("                 bds exec timeout file.stdout file.stderr file.exit command arguments...\n\n")
	os.Exit(1)
}
