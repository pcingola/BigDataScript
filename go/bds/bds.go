/* 
	Execute BigDataScript:

	This command line program allow two execution types

	Commands:
		1) default (no command)	:	Execute main Java package (compiler and interpreter)
		2) exec					:	Execute shell scripts, set maimum execution time, redirect STDOUT and STDERR, write exit code to a file
									bds exec timeout file.stdout file.stderr file.exit command arguments...
		3) help					: Show command usage and exit

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
	"fmt"
	"io"
	"log"
	"os"
	"os/exec"
	"os/signal"
	"strconv"
	"syscall"
	"time"
)

func main() {
	fmt.Printf("PID\t%d\n", syscall.Getpid())

	// Are we requested to execute a command?
	if len(os.Args) > 1 {
		if os.Args[1] == "exec" {
			// Execute command and exit
			os.Exit(ExecuteCommand())
		} else if os.Args[1] == "help" {
			usage()
		}
	}

	// Execute BigDataScript.jar
	os.Exit(BigDataScript())
}

func usage() {
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

/**
Invoke BigDataScript java program
WARNING: It is assumed that BigDataScript.jar is in the CLASSPATH
*/
func BigDataScript() int {
	// Append all arguments from command line
	args := []string{"java", "-Xmx1G", "ca.mcgill.mcb.pcingola.bigDataScript.BigDataScript"}
	for _, arg := range os.Args[1:] {
		args = append(args, arg)
	}

	return executeCommand("java", args, 0, "", "", "")
}

/*
	Execute a command
*/
func ExecuteCommand() int {
	if len(os.Args) < 7 {
		usage()
	}

	timeStr := os.Args[2]
	outFile := os.Args[3]
	errFile := os.Args[4]
	exitFile := os.Args[5]
	command := os.Args[6]

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
		go io.Copy(os.Stdout, stdout)
	} else {
		stdoutFile, err := os.Create(outFile)
		if err != nil {
			log.Fatal(err)
		}
		defer stdoutFile.Close()
		go io.Copy(stdoutFile, stdout)
	}

	// Copy to STDERR to file (or to stderr)
	if (errFile == "") || (errFile == "-") {
		go io.Copy(os.Stderr, stderr)
	} else {
		stderrFile, err := os.Create(errFile)
		if err != nil {
			log.Fatal(err)
		}
		defer stderrFile.Close()
		go io.Copy(stderrFile, stderr)
	}

	return executeCommandTimeout(cmd, timeSecs, exitFile)
}

/*
	Execute a command enforcing a timeout and writing exit status to 'exitFile'
*/
func executeCommandTimeout(cmd *exec.Cmd, timeSecs int, exitFile string) int {

	// Create a timeout process
	// References: http://blog.golang.org/2010/09/go-concurrency-patterns-timing-out-and.html
	exitCode := make(chan string, 1)
	go execute(cmd, exitCode)

	// Redirect all signals to channel (e.g. Ctrl-C)
	osSignal := make(chan os.Signal, 1)
	signal.Notify(osSignal) // , os.Interrupt)

	// Wait for execution to finish or timeout
	exitStr := ""
	if timeSecs <= 0 {
		timeSecs = 31536000 // One year
	}

	// Wait until executions ends, timeout or OS signal
	kill := false
	select {
	case exitStr = <-exitCode:

	case <-time.After(time.Duration(timeSecs) * time.Second):
		kill = true
		exitStr = "Time out"

	case <-osSignal:
		kill = true
		exitStr = "Signal received"
	}

	// Should we kill child process?
	if kill {
		if err := cmd.Process.Kill(); err != nil {
			log.Println("Failed to kill process: ", err)
		}
		cmd.Process.Wait() // Reap their souls

		// Send a SIGKILL to the process group (just in case any child process is still executing)
		syscall.Kill(0, syscall.SIGKILL)
	}

	// Write exitCode to file or show as log message
	if (exitFile == "") || (exitFile == "-") {
		if exitStr != "0" {
			log.Println(exitStr) // No exitFile? Log to console and exit
		}
	} else {
		writeFile(exitFile, exitStr) // Dump error to 'exitFile'
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
