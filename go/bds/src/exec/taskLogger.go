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

// Command indicating to remove file (taskLogger file)
const CMD_REMOVE_FILE = "@rm"
const CMD_KILL = "@kill"


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
		log.Printf("Debug taskLoggerCleanUpAll: Start '%s'\n", be.taskLoggerFile)
	}

	// Parse task logger file
	defer os.Remove(be.taskLoggerFile) // Make sure the PID file is removed
	cmds := be.taskLoggerParseFile()

	// Kill all pending processes
	be.taskLoggerProcess(cmds)
}

/*
 Parse taskLogger file
 Format is one line per entry:
 	id \t {"+", "-"} \t command

  Returns a hash of commands to run, to remove the resources
*/
func (be *BdsExec) taskLoggerParseFile() map[string]string {
	cmds := make(map[string]string)

	// Open file and parse it
	file, err := os.Open(be.taskLoggerFile)
	if err != nil {
		log.Printf("Error taskLoggerParseFile: Cannot open TaskLogger file '%s' (PID: %d)\n", be.taskLoggerFile, syscall.Getpid())
		return cmds
	}
	defer file.Close() // Make sure the file is deleted

	// Read line by line
	if DEBUG {
		log.Printf("Debug taskLoggerParseFile: Parsing taskLogger file '%s'\n", be.taskLoggerFile)
	}
	reader := bufio.NewReader(file)
	for {
		line, err := fileutil.ReadLine(reader)
		if err != nil {
			break
		}
		recs := strings.Split(line, "\t")

		pid := recs[0]
		addDel := recs[1]

		// Add or remove from map
		switch addDel {
			case "-":
				delete(cmds, pid)
				if DEBUG {
					log.Printf("Debug taskLoggerParseFile: Removing id '%s', add/del: '%s'\n", pid, addDel)
				}
			case "+":
				if len(recs) > 2 && len(recs[2]) > 0 {
					cmd := recs[2]
					cmds[pid] = cmd
					if DEBUG {
						log.Printf("Debug taskLoggerParseFile: Adding id '%s', add/del '%s', cmd '%s'\n", pid, addDel, cmd)
					}
				} else {
					log.Printf("Error taskLoggerParseFile: Invalid line, cmd field not found, line: '%s'\n", cmds, line)
				}
			default:
				log.Printf("Error taskLoggerParseFile: Invalid addDel field '%s', line: '%s'\n", addDel, line)
		}
	}

	return cmds
}

/*
  Process commands in hash to remove resources
  Some "internal" commands are executed directly (e.g. remove files, kill local processes, etc.)
  All "internal" command names start with an '@' character
*/
func (be *BdsExec) taskLoggerProcess(cmds map[string]string) {
	runCmds := make(map[string]string)
	for pid, cmd := range cmds {
		switch cmd {
			case CMD_KILL:
				if VERBOSE {
					log.Printf("Info: Killing PID '%s'\n", pid)
				}
				pidInt, _ := strconv.Atoi(pid)
				be.KillProcessGroup(pidInt) // No need to run a command, just kill local porcess group
			case CMD_REMOVE_FILE:
				// This is a file to be removed, not a command
				if VERBOSE {
					log.Printf("Info: Deleting file '%s'\n", pid)
				}
				os.Remove(pid)
			default:
				// Remove using a command
				if DEBUG {
					log.Printf("Info: Cleanning up '%s' using command '%s'\n", pid, runCmds[cmd])
				}
				// Create command to be executed (or append)
				_, ok := runCmds[cmd]
				if ok {
					runCmds[cmd] = runCmds[cmd] + "\t" + pid
				} else {
					runCmds[cmd] = cmd + "\t" + pid
				}
		}
	}
	be.taskLoggerRunCmd(runCmds) // Run all commands (usually it's only one command)
}

/*
 Run all commands in the dictionary
*/
func (be *BdsExec) taskLoggerRunCmd(runCmds map[string]string) {
	for cmd, args := range runCmds {
		if len(cmd) > 0 {
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
