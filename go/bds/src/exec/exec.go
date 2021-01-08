package exec

import (
	"fmt"
	"log"
	"os"
	"os/exec"
	"syscall"
)

// Verbose & Debug
var DEBUG bool = false
var VERBOSE bool = false

// Exit codes
const EXITCODE_OK = 0
const EXITCODE_ERROR = 1
const EXITCODE_TIMEOUT = 2

const BDS_NATIVE_LIB_DIR = "lib"

const MAX_CHECKSUM_ITERS = 100
const CHECKSUM_LINE_START = "# Checksum: " // This has to match the one defined in 'Task.java'
const CHECKSUM_SLEEP_TIME = 10 // Sleep time while waiting for correct checksum to appear (in miliseconds)


type BdsExec struct {
	args []string			// Command line arguments invoking 'bds' binary
	commandName string		// Command to execute: 'exec', 'kill', 'help', etc.
	execName string			// This binary's absolute path

	TaskLoggerFile string	// Task logger file to be used by 'Bds (java)'

	cmd *exec.Cmd			// Exec: Command
	cmdargs []string		// Exec: Command arguments
	command string			// Exec: Command to execute (path to a shell script)
	outFile string			// Exec: Copy (tee) stdout to this file
	errFile string			// Exec: Copy (tee) stderr to this file
	exitFile string			// Exec: Write exit code to this file
	timeSecs int			// Exec: Maximum execution time
	exitCode int			// Exec: Command's Exit code
	taskId string			// Exec: Task ID
	noCheckSum bool			// Exec: Disable performing checksum on (shell) file to execute
	awsSqsName string		// Exec: Send command outputs to an AWS SQS queue

	pidToKill int			// Kill: PID to kill

	javaMem string			// Bds: Java memory
	javaArgs []string		// Bds: Other Java arguments

	randTempFile uint32		// Random seed
}

/*
	Create a new BdsExec structure
*/
func NewBdsExec() *BdsExec {
	be := &BdsExec{}

	be.args = os.Args
	if len(os.Args) > 1 {
		be.commandName = os.Args[1]
	}
	be.setExecName()

	// Exec initialize
	be.command = ""
	be.outFile = ""
	be.errFile = ""
	be.exitFile = ""
	be.timeSecs = 0
	be.noCheckSum = false

	// Bds
	be.javaMem = JAVA_MEM

	be.parseCmdLineArgs()

	return be
}

/*
	Execute a command
	Enforce execution time limit
	Redirect stdout and stderr to files
*/
func (be *BdsExec) ExecuteCommandArgs() int {
	// Show PID info (parent process is expecting this line first)
	fmt.Printf("%d\n", syscall.Getpid())
	os.Stdout.Sync()

	if DEBUG {
		log.Print("Debug ExecuteCommandArgs: Start\n")
	}

	// Execute command
	be.executeCommand()

	if DEBUG {
		log.Printf("Debug ExecuteCommandArgs: Exit code %d\n", be.exitCode)
	}

	return be.exitCode
}
