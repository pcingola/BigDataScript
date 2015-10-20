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
	"strconv"
)

/*
	Main
*/
func main() {

	bdsexec := exec.NewBdsExec(os.Args)

	// Parse command line arguments
	if len(os.Args) > 1 {
		if os.Args[1] == "exec" {
			// Execute 'exec' command and exit
			exitCode := bdsexec.ExecuteCommandArgs()
			os.Exit(exitCode)
		} else if os.Args[1] == "kill" {
			// Kill a process group
			if len(os.Args) != 3 {
				bdsexec.Usage("Invalid number of parameters for 'kill' command")
			}

			// Parse pid
			pidStr := os.Args[2]
			pid, err := strconv.Atoi(pidStr)
			if err != nil {
				log.Fatalf("Invalid PID: '%s'\n", pidStr)
			}

			// Kill and exit
			bdsexec.KillProcessGroup(pid)
			os.Exit(0)
		} else if os.Args[1] == "test" {
			// Placeholder for tests
			zzz()
		} else if os.Args[1] == "help" {
			// Show usage and exit
			bdsexec.Usage("")
		}
	}

	// Execute Bds.jar
	os.Exit(bdsexec.Bds())
}

// A function used for testing
func zzz() {
	fmt.Printf("Test function: Zzz\n")
	os.Exit(1)
}
