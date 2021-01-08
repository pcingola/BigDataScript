package exec

import (
	"aws"
	"bufio"
	"fileutil"
	"log"
	"os"
	"os/exec"
	"strconv"
	"strings"
	"syscall"
	"tmpfile"
)

// Command indicating to remove file (taskLogger file)
const CMD_REMOVE_FILE = "@rm"	// Command to delete a local file
const CMD_KILL = "@kill"	// Command to kill a local process
const CMD_AWS_DELETE_QUEUE_COMMAND = "@aws_sqs_delete_queue"	// Command to delete a queue
const CMD_AWS_TERMINATE_INSTANCE = "@aws_ec2_terminate"	// Command to terminate an EC@ instance on AWS
const CMD_AWS_S3_DELETE_FILE = "@aws_s3_rm"

func (be *BdsExec) createTaskLoggerFile() {
	prefix := "bds.pid." + strconv.Itoa(syscall.Getpid())
	pidTmpFile, err := tmpfile.TempFile(prefix)
	if err != nil {
		log.Fatal(err)
	}
	be.TaskLoggerFile = pidTmpFile
	if DEBUG {
		log.Printf("Debug createTaskLoggerFile: Creating file '%s'\n", be.TaskLoggerFile)
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
func (be *BdsExec) TaskLoggerCleanUpAll() {
	if DEBUG {
		log.Printf("Debug taskLoggerCleanUpAll: Start '%s'\n", be.TaskLoggerFile)
	}

	// Parse task logger file
	defer os.Remove(be.TaskLoggerFile) // Make sure the PID file is removed
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
	file, err := os.Open(be.TaskLoggerFile)
	if err != nil {
		log.Printf("Error taskLoggerParseFile: Cannot open TaskLogger file '%s' (PID: %d)\n", be.TaskLoggerFile, syscall.Getpid())
		return cmds
	}
	defer file.Close() // Make sure the file is deleted

	// Read line by line
	if DEBUG {
		log.Printf("Debug taskLoggerParseFile: Parsing taskLogger file '%s'\n", be.TaskLoggerFile)
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
	var ec2 *aws.AwsEc2
	var s3 *aws.AwsS3
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
			case CMD_AWS_DELETE_QUEUE_COMMAND:
				if VERBOSE {
					log.Printf("Info: Deleting AWS SQS queue '%s'\n", pid)
				}
				awssqs, err := aws.NewSqs(pid, "")
				if err != nil {
					log.Printf("Error: Creating AWS SQS client for queue '%s', error: %s\n", pid, err)
				} else {
					err := awssqs.Delete() // Delete queue
					if err != nil {
						log.Printf("Error: Deleting AWS SQS queue '%s', error: %s\n", pid, err)
					}
				}
			case CMD_AWS_S3_DELETE_FILE:
				if VERBOSE {
					log.Printf("Info: Deleting AWS S3 file '%s'\n", pid)
				}
				if s3 == nil {
					s3 = aws.NewS3()
				}
				s3.Delete(pid)
			case CMD_AWS_TERMINATE_INSTANCE:
				if VERBOSE {
					log.Printf("Info: Terminating AWS EC2 instance '%s'\n", pid)
				}
				if ec2 == nil {
					ec2 = aws.NewEc2()
				}
				ec2.Add(pid) // Add instance ID to request
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

	// Delete all EC2 instances
	if ec2 != nil {
		err := ec2.Terminate()
		if err != nil {
			log.Printf("Error: Terminating instances '%v', error: %s\n", ec2.InstanceIds, err)
		} else if VERBOSE {
			log.Printf("Info: Terminated AWS EC2 instances '%v'\n", ec2.InstanceIds)
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
