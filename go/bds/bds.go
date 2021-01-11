/*
	Execute bds:

	This program allows different "commands"
	Commands:
		1) default (no command)	:	Execute BDS Java package (compiler and interpreter)

		2) exec					:	Execute shell command and:
										i) Enforce maximum execution time,
										ii) redirect STDOUT and STDERR to files
										iii) show PID to stdout
										iv) write exit code to file
									Format:
										bds exec timeout file.stdout file.stderr file.exit command arguments...

		3) help					: 	Show command usage and exit

		4) kill pid             :   Send a kill signal to a process group (same as shell command "kill -- -pid")

	Examples:

	This will load, compile and execute 'myprogram.bds' (bds program)
		$ bds myprogram.bds

	This will execute "ls -al", redirect standard output and standard error
	to "out.stdout" and "out.stderr" respectively. When the command finishes, the
	exit code will be written to "out.exit"

		$ bds exec 10 out.stdout out.stderr out.exit ls -al
*/
package main

import (
	"exec"
	"fmt"
	"log"
	"os"
)

/*
	Main
*/
func main() {

	if exec.DEBUG {
		log.Printf("Info main: bds (go) invoked with command line argumnts: %v", os.Args)
	}

	bdsexec := exec.NewBdsExec()

	// Parse command line arguments
	if len(os.Args) > 1 {
		switch os.Args[1] {
		case "exec":
			// Execute 'exec' command and exit
			exitCode := bdsexec.ExecuteCommandArgs()
			os.Exit(exitCode)
		case "kill":
			bdsexec.KillArgs()
			os.Exit(0)
		case "test":
			// Placeholder for tests
			zzz(bdsexec)
			os.Exit(0)
		case "help":
			// Show usage and exit
			bdsexec.Usage("")
		}
	}

	// Execute Bds.jar
	exitCode := bdsexec.BdsJava()
	os.Exit(exitCode)
}

// A function used for testing
func zzz(bdsexec *exec.BdsExec) {
	fmt.Printf("Info: Test function zzz()\n")
	bdsexec.TaskLoggerFile = "z.pid"
	exec.DEBUG = true
	exec.VERBOSE = true
	bdsexec.TaskLoggerCleanUpAll()
}
