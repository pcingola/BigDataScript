package exec

import (
	"bufio"
	"fmt"
	"log"
	"os"
	"os/exec"
	"os/signal"
	"path"
	"path/filepath"
	"regexp"
	"strconv"
	"strings"
	"syscall"
	"time"

	"fileutil"
	"tee"
	"tmpfile"
)

// Verbose & Debug
const DEBUG = false
const VERBOSE = true

// Exit codes
const EXITCODE_OK = 0
const EXITCODE_ERROR = 1
const EXITCODE_TIMEOUT = 2

const JAVA_CMD = "java"
const JAVA_MEM = "-Xmx4G"
const JAVA_NATIVE_LIB = "-Djava.library.path="
const JAVA_BDS_CLASS = "org.bds.Bds"

const BDS_NATIVE_LIB_DIR = "lib"

// Command indicating to remove file (taskLogger file)
const CMD_REMOVE_FILE = "rm"

type BdsExec struct {
	args []string 			// Command line arguments invoking 'bds'
	execName string 		// This binary's absolute path

	taskLoggerFile string 	// Task logger file to be used by 'Bds (java)'

	cmd *exec.Cmd 			// BdsExec: Command
	cmdargs []string 		// BdsExec: Command arguments
	command string 			// BdsExec: Command to execute (path to a shell script)
	outFile string 			// BdsExec: Copy (tee) stdout to this file
	errFile string 			// BdsExec: Copy (tee) stderr to this file
	exitFile string 		// BdsExec: Write exit code to this file
	timeSecs int 			// BdsExec: Maximum execution time
	exitCode int 			// Command's Exit code

	randTempFile uint32 	// Random seed
}

/*
	Invoke bds java program

	WARNING:
		It is assumed that bds.jar is in the same executable binary as 'bds'

		This is actually a nice hack used to distribute only one file. Since JAR files
		are actually ZIP files and ZIP files are indexed from the end of the file, you can
		append the JAR to the go binary (cat binary jar > new_binary) and you encapsulate
		both in the same file.

		Idea and implementation of this hack: Hernan Gonzalez
*/
func (be *BdsExec) Bds() int {
	// Create a taskLoggerFile (temp file based on pid number)
	prefix := "bds.pid." + strconv.Itoa(syscall.Getpid())
	pidTmpFile, err := tmpfile.TempFile(prefix)
	if err != nil {
		log.Fatal(err)
	}
	be.taskLoggerFile = pidTmpFile
	defer os.Remove(be.taskLoggerFile) // Make sure the PID file is new
	if DEBUG {
		log.Printf("Debug: taskLoggerFile '%s'\n", be.taskLoggerFile)
	}

	bdsLibDir := path.Dir(be.execName) + "/" + BDS_NATIVE_LIB_DIR

	// Append all arguments from command line
	be.cmdargs = []string{ JAVA_CMD,
		JAVA_MEM,
		JAVA_NATIVE_LIB + bdsLibDir,
		"-cp", be.execName,
		JAVA_BDS_CLASS }
	be.cmdargs = append(be.cmdargs, "-pid")
	be.cmdargs = append(be.cmdargs, be.taskLoggerFile)
	for _, arg := range be.args[1:] {
		be.cmdargs = append(be.cmdargs, arg)
	}

	// Execute command
	be.command = JAVA_CMD
	exitCode := be.executeCommand()

	return exitCode
}

/*
  Returns absolute path of executing file.
  WARNING: this must be called before
  changing the current directory
*/
func (be *BdsExec) discoverExecName() string {
	if DEBUG {
		log.Printf("Debug: discoverExecName (%s)\n", be.args[0])
	}

	f := be.args[0]

	if path.IsAbs(f) {
		return f
	}

	wd, err := os.Getwd()
	if err != nil {
		panic(fmt.Sprintf("discoverExecName: Getwd failed '%s'", err))
	}

	_, err = os.Stat(f)
	if err == nil {
		// Relative file exists
		return path.Clean(path.Join(wd, f))
	}

	f2, err := exec.LookPath(f)
	if err != nil {
		panic(fmt.Sprintf("discoverExecName: Lookpath failed '%s'", err))
	}

	if path.IsAbs(f2) {
		return f2
	}

	return path.Clean(path.Join(wd, f2))
}

/*
	Create a new BdsExec structure
*/
func NewBdsExec(args []string) *BdsExec {
	be := &BdsExec{}

	be.args = args
	be.command = ""
	be.outFile = ""
	be.errFile = ""
	be.exitFile = ""
	be.timeSecs = 0

	be.execName = be.discoverExecName()
	f, err := filepath.EvalSymlinks(be.execName)
	if err == nil {
		be.execName = f
	}

	if DEBUG {
		log.Printf("Debug: execName:%s\n", be.execName)
	}

	return be
}

/*
	Execute a command
	Enforce execution time limit
	Redirect stdout and stderr to files
*/
func (be *BdsExec) ExecuteCommandArgs() int {
	if DEBUG {
		log.Print("Debug: ExecuteCommandArgs\n")
	}

	minArgs := 6

	if len(be.args) < minArgs {
		be.Usage("Invalid number of parameters for 'exec' command")
	}

	// Parse command line args
	cmdIdx := 2
	timeStr := be.args[cmdIdx]
	cmdIdx = cmdIdx + 1
	be.outFile = be.args[cmdIdx]
	cmdIdx = cmdIdx + 1
	be.errFile = be.args[cmdIdx]
	cmdIdx = cmdIdx + 1
	be.exitFile = be.args[cmdIdx]
	cmdIdx = cmdIdx + 1
	be.command = be.args[cmdIdx]
	cmdIdx = cmdIdx + 1

	// Append other arguments
	be.cmdargs = []string{be.command}
	for _, arg := range be.args[minArgs:] {
		be.cmdargs = append(be.cmdargs, arg)
	}

	// Parse time argument
	timeSecs, err := strconv.Atoi(timeStr)
	if err != nil {
		log.Fatalf("Invalid time: '%s'\n", timeStr)
	}
	be.timeSecs = timeSecs

	// Show PID info (parent process is expecting this line first)
	fmt.Printf("%d\n", syscall.Getpid())
	os.Stdout.Sync()

	// Execute command
	be.executeCommand()

	if DEBUG {
		log.Printf("Debug, ExecuteCommandArgs: Exit code %d\n", be.exitCode)
	}

	return be.exitCode
}

/*
	Execute a command (using arguments 'args')
	Redirect stdout to outFile    (unless file name is empty)
	Redirect stderr to errFile    (unless file name is empty)
	Write exit code to exitFile   (unless file name is empty)
	Timeout after timeout seconds (unless time is zero)
*/
func (be *BdsExec) executeCommand() int {
	if DEBUG {
		log.Printf("Debug, executeCommand %s\n", be.command)
	}

	// Redirect all signals to channel (e.g. Ctrl-C)
	osSignal := make(chan os.Signal)

	if be.taskLoggerFile != "" {
		// Main bds program
		signal.Notify(osSignal) // Capture all signals
	} else {
		// Set a new process group.
		// We want to be able to kill all child processes, without killing the
		// calling program. E.g. When running using a local executor, the Java Bds
		// calls 'bds exec', so sending a kill to the group when a timeOut occurs,
		// would also kill the parent Java program and kill the whole bds execution
		// (clearly not what we want).
		// To avoid this, we create a new group thus we can send a kill signal to
		// this new process group.

		gpidOri, _ :=  syscall.Getpgid(0);
		if err := syscall.Setpgid(0, 0); err != nil {
			// During an ssh remote execution we will no be albe to do this.
			// In this case, we assume that the SSH daemon will catch the sinals
			// and kill al child processes.
			if DEBUG {
				log.Printf("Error setting process group: %s", err)
			}
		}

		if DEBUG {
			gpidNew, _ :=  syscall.Getpgid(0);
			log.Printf("Info: Setting new process group. Original GPID: %d, new GPID: %d\n", gpidOri, gpidNew)
		}
	}

	// Create command
	be.cmd = exec.Command(be.command)
	be.cmd.Args = be.cmdargs

	// Copy stdout
	stdout := tee.NewTee(be.outFile, false)
	defer stdout.Close()
	be.cmd.Stdout = stdout

	// Copy stderr
	stderr := tee.NewTee(be.errFile, true)
	defer stderr.Close()
	be.cmd.Stderr = stderr

	// Connect to stdin
	be.cmd.Stdin = os.Stdin

	// Start process
	err := be.cmd.Start()
	if err != nil {
		log.Fatal(err)
	}

	be.exitCode = be.executeCommandTimeout(osSignal)
	if DEBUG {
		log.Printf("Debug, executeCommand: Exit code %d\n", be.exitCode)
	}
	return be.exitCode
}

/*
	Execute a command enforcing a timeout and writing exit status to 'exitFile'
*/
func (be *BdsExec) executeCommandTimeout(osSignal chan os.Signal) int {
	if DEBUG {
		log.Printf("Debug, executeCommandTimeout\n")
	}

	// Wait for execution to finish or timeout
	exitStr := ""
	if be.timeSecs <= 0 {
		be.timeSecs = 31536000 // Default: One year
	}

	// Create a timeout process
	// References: http://blog.golang.org/2010/09/go-concurrency-patterns-timing-out-and.html
	exitCode := make(chan string, 1)
	go execute(be.cmd, exitCode)

	// Wait until executions ends, timeout or OS signal
	kill := false
	run := true
	for run {
		select {
		case exitStr = <-exitCode:
			kill = false
			run = false
			if DEBUG {
				log.Printf("Debug, executeCommandTimeout: Execution finished (%s)\n", exitStr)
			}

		case <-time.After(time.Duration(be.timeSecs) * time.Second):
			run = false
			kill = true
			exitStr = "Time out"
			if DEBUG {
				log.Printf("Debug, executeCommandTimeout: Timeout!\n")
			}

		case sig := <-osSignal:
			// Ignore some signals (e.g. "window changed")
			sigStr := sig.String()
			if sigStr != "window changed" && sigStr != "child exited" && sigStr != "window size changes" {
				if VERBOSE || DEBUG {
					log.Printf("bds: Received OS signal '%s'\n", sigStr)
				}

				kill = true
				exitStr = "Signal received"
				run = false
			}
		}
	}

	// Write exitCode to file
	be.updateExitFile(exitStr)

	// Should we kill child process?
	if kill {
		// WARNING: The kill(0) signal will also kill this process, so nothing
		// may be execited after this call (platform dependent)
		be.kill()
	}

	// OK? exit value should be zero
	if exitStr == "0" {
		return EXITCODE_OK
	}

	// Timeout?
	if exitStr == "Time out" {
		return EXITCODE_TIMEOUT
	}

	return EXITCODE_ERROR
}

/*
	Execute a command and writing exit status to 'exitCode'
*/
func execute(cmd *exec.Cmd, exitCode chan string) {
	if DEBUG {
		log.Printf("Debug, execute.\n")
	}

	// Wait for command to finish
	if err := cmd.Wait(); err != nil {
		if DEBUG {
			log.Printf("Debug, execute: Failed (%s)\n", err)
		}

		exitCode <- err.Error()
		return
	}

	if DEBUG {
		log.Printf("Debug, execute: Finished OK\n")
	}

	exitCode <- "0"
}

/*
	Kill all child process
*/
func (be *BdsExec) kill() {
	if DEBUG {
		log.Printf("Debug: Killing process\n")
	}
	be.cmd.Process.Kill()
	be.cmd.Process.Wait() // Reap their souls

	// Should we kill all process groups from taskLoggerFile?
	if be.taskLoggerFile != "" {
		be.taskLoggerCleanUpAll()
	}

	// Send a SIGKILL to the process group (just in case any child process is still executing)
	if DEBUG {
		log.Printf("Debug: Killing process group: kill(0, SIGHUP)\n")
	}

	// WARNING: The Kill(0) signal will also kill this process, so nothing
	// may be execited after this call (platform dependent)
	syscall.Kill(0, syscall.SIGHUP)

	if DEBUG {
		log.Printf("Debug: Killed process group: kill(0, SIGHUP)\n")
	}
}

/*
	Kill a process group
*/
func (be *BdsExec) KillProcessGroup(pid int) {
	if DEBUG {
		log.Printf("Debug, killProcessGroup( %d )\n", pid)
	}

	syscall.Kill(-pid, syscall.SIGHUP)
}

// Loads configuration as map, expects key=val syntax.
// Blank lines, and lines beggining with # are ignored.
func LoadConfig(filename string, dest map[string]string) {
	if DEBUG {
		log.Printf("Debug, LoadConfig(%s)\n", filename)
	}

	re, _ := regexp.Compile("[#].*\\n|\\s+\\n|\\S+[=]|.*\n")
	fi, err := os.Stat(filename)
	if err != nil {
		return
	}
	f, err := os.Open(filename)
	if err != nil {
		return
	}
	buff := make([]byte, fi.Size())
	f.Read(buff)
	f.Close()
	str := string(buff) + "\n"
	s2 := re.FindAllString(str, -1)
	for i := 0; i < len(s2); {
		if strings.HasPrefix(s2[i], "#") {
			i++
		} else if strings.HasSuffix(s2[i], "=") {
			key := strings.ToLower(s2[i])[0 : len(s2[i])-1]
			i++
			if strings.HasSuffix(s2[i], "\n") {
				val := s2[i][0 : len(s2[i])-1]
				if strings.HasSuffix(val, "\r") {
					val = val[0 : len(val)-1]
				}
				i++
				dest[key] = val
			}
		} else if strings.Index(" \t\r\n", s2[i][0:1]) > -1 {
			i++
		} else {
			//
		}
	}
}

/*
	Perform final clean up: Parse taskLoggerFile
		i) Send kill signal to all process groups that have not been marked as 'finished'
		ii) Run commands neede to deallocate processes (e.g. cluster)
		iii) Remove stale output files form unfinshed tasks

	File format:
		"pid \t {+,-} \n"

	where '+' inidicates the process was started and '-' that
	the process finished. So all pid that do not have a '-' entry
	must be killed.
*/
func (be *BdsExec) taskLoggerCleanUpAll() {
	if DEBUG {
		log.Printf("Debug, taskLoggerCleanUpAll\n")
	}

	var (
		err  error
		line string
		file *os.File
	)

	defer os.Remove(be.taskLoggerFile) // Make sure the PID file is removed

	//---
	// Open file and parse it
	//---
	pids := make(map[string]bool)
	cmds := make(map[string]string)

	if file, err = os.Open(be.taskLoggerFile); err != nil {
		log.Printf("Error: Cannot open TaskLogger file '%s' (PID: %d)\n", be.taskLoggerFile, syscall.Getpid())
		return
	}
	defer file.Close() // Make sure the file is deleted

	// Read line by line
	if DEBUG {
		log.Printf("Debug, taskLoggerCleanUpAll: Parsing process pid file '%s'\n", be.taskLoggerFile)
	}
	reader := bufio.NewReader(file)
	for {
		if line, err = fileutil.ReadLine(reader); err != nil {
			break
		}
		recs := strings.Split(line, "\t")

		pid := recs[0]
		addDel := recs[1]
		if DEBUG {
			log.Printf("Debug, taskLoggerCleanUpAll: \t\tpid: '%s'\tadd/del: '%s'\n", pid, addDel)
		}

		// Add or remove from map
		if addDel == "-" {
			delete(pids, pid)
		} else {
			pids[pid] = true
			if len(recs) > 2 && len(recs[2]) > 0 {
				cmds[pid] = recs[2]
			}
		}
	}

	// Kill all pending processes
	runCmds := make(map[string]string)
	for pid, running := range pids {

		// Is it marked as running? Kill it
		if running {
			if cmd, ok := cmds[pid]; !ok {
				if VERBOSE {
					log.Printf("Info: Killing PID '%s'\n", pid)
				}
				pidInt, _ := strconv.Atoi(pid)
				be.KillProcessGroup(pidInt) // No need to run a command, just kill local porcess group
			} else if cmd == CMD_REMOVE_FILE {
				// This is a file to be removed, not a command
				if VERBOSE {
					log.Printf("Info: Deleting file '%s'\n", pid)
				}
				os.Remove(pid)
			} else {
				if DEBUG {
					log.Printf("Info: Killing PID '%s' using command '%s'\n", pid, runCmds[cmd])
				}

				// Create command to be executed
				if _, ok = runCmds[cmd]; ok {
					runCmds[cmd] = runCmds[cmd] + "\t" + pid
				} else {
					runCmds[cmd] = cmd + "\t" + pid
				}
			}
		} else {
			if DEBUG {
				log.Printf("Debug, taskLoggerCleanUpAll: Not killing PID '%s' (finishde running)\n", pid)
			}
		}
	}

	// Run all commands (usually it's only one command)
	for cmd, args := range runCmds {
		if len(cmd) > 0 {
			// fmt.Fprintf(os.Stderr, "\t\trunning command '%s'\n", cmd)
			if VERBOSE {
				log.Printf("Info: Running command '%s'\n", cmd)
			}
			cmdExec := exec.Command(cmd)
			cmdExec.Args = strings.Split(args, "\t")
			err := cmdExec.Run()
			if err != nil {
				log.Fatal(err)
			}
		}
	}
}

/*
	Write exit information to 'exitFile'
*/
func (be *BdsExec) updateExitFile(exitStr string) {
	if (be.exitFile != "") && (be.exitFile != "-") {
		if DEBUG {
			log.Printf("Debug: Writing exit status '%s' to exit file '%s'\n", exitStr, be.exitFile )
		}
		fileutil.WriteFile(be.exitFile, exitStr)
	}
}

/*
	Show usage message and exit
*/
func (be *BdsExec) Usage(msg string) {
	if msg != "" {
		fmt.Fprintf(os.Stderr, "Error: %s\n", msg)
		fmt.Fprintf(os.Stderr, "Arguments:\n")
		for n, arg := range os.Args[1:] {
			fmt.Fprintf(os.Stderr, "\t%d : %s\n", n, arg)
		}
		fmt.Fprintf(os.Stderr, "\n")
	}

	// Show help and exit
	fmt.Fprintf(os.Stderr, "Usage: bds command\n\n")
	fmt.Fprintf(os.Stderr, "Commands:\n\n")
	fmt.Fprintf(os.Stderr, "  default :  Execute bds Java program (compiler and interpreter)\n")
	fmt.Fprintf(os.Stderr, "             Syntax:\n")
	fmt.Fprintf(os.Stderr, "                 bds [options] program.bds\n\n")
	fmt.Fprintf(os.Stderr, "  exec    :  Execute shell scripts and:\n")
	fmt.Fprintf(os.Stderr, "                 i) Show pid.\n")
	fmt.Fprintf(os.Stderr, "                 ii) Enforce maimum execution time.\n")
	fmt.Fprintf(os.Stderr, "                 iii) Redirect STDOUT and STDERR to files.\n")
	fmt.Fprintf(os.Stderr, "                 iv) Write exitCode to a file.\n")
	fmt.Fprintf(os.Stderr, "             Note: If any file name is '-' it is ignored (not redirected).\n")
	fmt.Fprintf(os.Stderr, "             Syntax:\n")
	fmt.Fprintf(os.Stderr, "                 bds exec timeout file.stdout file.stderr file.exit command arguments...\n\n")
	fmt.Fprintf(os.Stderr, "  kill pid :  Kill process group 'pid'.\n")
	os.Exit(1)
}
