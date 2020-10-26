package exec

import (
	"fmt"
	"log"
	"os"
	"os/exec"
	"path"
	"path/filepath"
	"strconv"
	"strings"
)

const JAVA_CMD_OPTS_PREFIX = "--bdsJava"
const JAVA_CMD_OPTS_XMX = "--bdsJavaXmx"

/*
  Returns absolute path of executing file.
  WARNING: this must be called before
  changing the current directory
*/
func (be *BdsExec) discoverExecName() string {
	if DEBUG {
		log.Printf("Debug discoverExecName: Start (%s)\n", be.args[0])
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
	Parse commnad line Arguments for different sub-commnads
*/
func (be *BdsExec) parseCmdLineArgs() {
	be.parseCmdLineArgsVerboseDebug()
	switch be.commandName {
		case "exec":
			// Execute 'exec' command and exit
			be.parseCmdLineArgsExec()
		case "kill":
			be.parseCmdLineArgsKill()
		case "help":
			// Nothing to do for 'help'
		case "test":
			// Nothing to do for 'test'
		default:
			// Nothing to do for default (execute bds-java)
			be.parseCmdLineArgsBds()
	}
}

// Parse command line for 'exec' command
func (be *BdsExec) parseCmdLineArgsBds() {
	// Parse command line options for the JVM
	if DEBUG {
		log.Printf("Debug parseCmdLineArgsBds: Parsing command line arguments (len=%d): %v\n", len(be.args), be.args)
	}
	var argsNew, javaArgs []string
	for _, arg := range be.args {
		if strings.HasPrefix(arg, JAVA_CMD_OPTS_XMX) {
			be.javaMem = "-" + strings.TrimPrefix(arg, JAVA_CMD_OPTS_PREFIX)
			if DEBUG {
				log.Printf("Debug parseCmdLineArgsBds: Setting javaMem to '%s'\n", be.javaMem)
			}
		} else if strings.HasPrefix(arg, JAVA_CMD_OPTS_PREFIX) {
			javaOpt := "-" + strings.TrimPrefix(arg, JAVA_CMD_OPTS_PREFIX)
			if DEBUG {
				log.Printf("Debug parseCmdLineArgsBds: Adding java command line option '%s'\n", javaOpt)
			}
			javaArgs = append(javaArgs, javaOpt)
		} else {
			argsNew = append(argsNew, arg)
		}
	}
	be.args = argsNew
	be.javaArgs = javaArgs
}

// Parse command line for 'exec' command
func (be *BdsExec) parseCmdLineArgsExec() {
	if DEBUG {
		log.Printf("Debug parseCmdLineArgsExec: Parsing command line arguments (len=%d): %v\n", len(be.args), be.args)
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
}


// Parse command line for 'kill' command
func (be *BdsExec) parseCmdLineArgsKill() {
	if DEBUG {
		log.Printf("Debug parseCmdLineArgsKill: Parsing command line arguments (len=%d): %v\n", len(be.args), be.args)
	}

	if len(be.args) != 3 {
		be.Usage("Invalid number of parameters for 'kill' command")
	}

	// Parse pid
	pidStr := be.args[2]
	pidToKill, err := strconv.Atoi(pidStr)
	if err != nil {
		log.Fatalf("Invalid PID: '%s'\n", pidStr)
	}
	be.pidToKill = pidToKill
}

/*
  Parse command line arguments: Activate 'VERBOSE' or 'DEBUG'
  Note: Leaves command line args intact
*/
func (be *BdsExec) parseCmdLineArgsVerboseDebug() {
	for _, opt := range be.args {
		switch opt {
		case "-d":
			DEBUG = true
		case "-v":
			VERBOSE, DEBUG = true, true
		}
	}
}

// Set 'execName' by finding the canonical path to the program
func (be *BdsExec) setExecName() {
	be.execName = be.discoverExecName()
	f, err := filepath.EvalSymlinks(be.execName)
	if err == nil {
		be.execName = f
	}
	if DEBUG {
		log.Printf("Debug: execName:%s\n", be.execName)
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
	fmt.Fprintf(os.Stderr, "                 bds [bds_options] program.bds [program_options]\n\n")
	fmt.Fprintf(os.Stderr, "                 %s  : Java '-Xmx' parameter (passed to the JVM)\n\n", JAVA_CMD_OPTS_XMX)
	fmt.Fprintf(os.Stderr, "                 %s* : Java commands passed to the JVM (after removing the '%s' part)\n\n", JAVA_CMD_OPTS_XMX, JAVA_CMD_OPTS_XMX)
	fmt.Fprintf(os.Stderr, "  exec    :  Execute shell scripts and:\n")
	fmt.Fprintf(os.Stderr, "                 i) Show pid.\n")
	fmt.Fprintf(os.Stderr, "                 ii) Enforce maimum execution time.\n")
	fmt.Fprintf(os.Stderr, "                 iii) Redirect STDOUT and STDERR to files.\n")
	fmt.Fprintf(os.Stderr, "                 iv) Write exitCode to a file.\n")
	fmt.Fprintf(os.Stderr, "             Note: If any file name is '-' it is ignored (not redirected).\n")
	fmt.Fprintf(os.Stderr, "             Syntax:\n")
	fmt.Fprintf(os.Stderr, "                 bds exec timeout file.stdout file.stderr file.exit command arguments...\n\n")
	fmt.Fprintf(os.Stderr, "  kill pid :  Kill process group 'pid'.\n")
	fmt.Fprintf(os.Stderr, "Generic options:\n\n")
	fmt.Fprintf(os.Stderr, "  -d :  Debug\n")
	fmt.Fprintf(os.Stderr, "  -v :  Verbose\n")
	os.Exit(1)
}
