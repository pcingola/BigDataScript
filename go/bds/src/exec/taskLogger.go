package exec

import (
	"bufio"
	"fileutil"
	"log"
	"os"
	"os/exec"
	"strconv"
	"strings"
	"syscall"
	"tmpFile"
)

func (be *BdsExec) createTaskLoggerFile() {
	prefix := "bds.pid." + strconv.Itoa(syscall.Getpid())
	pidTmpFile, err := tmpfile.TempFile(prefix)
	if err != nil {
		log.Fatal(err)
	}
	be.taskLoggerFile = pidTmpFile
	if DEBUG {
		log.Printf("Debug createTaskLoggerFile: Creating file '%s'\n", be.taskLoggerFile)
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
		log.Printf("Debug taskLoggerCleanUpAll: Start\n")
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
		log.Printf("Error taskLoggerCleanUpAll: Cannot open TaskLogger file '%s' (PID: %d)\n", be.taskLoggerFile, syscall.Getpid())
		return
	}
	defer file.Close() // Make sure the file is deleted

	// Read line by line
	if DEBUG {
		log.Printf("Debug taskLoggerCleanUpAll: Parsing process pid file '%s'\n", be.taskLoggerFile)
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
			log.Printf("Debug taskLoggerCleanUpAll: \t\tpid: '%s'\tadd/del: '%s'\n", pid, addDel)
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
				log.Printf("Debug taskLoggerCleanUpAll: Not killing PID '%s' (finished running)\n", pid)
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
