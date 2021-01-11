package exec

import (
	"log"
	"os"
	"path"
)

const JAVA_CMD = "java"
const JAVA_MEM = "-Xmx4G"
const JAVA_NATIVE_LIB = "-Djava.library.path="
const JAVA_BDS_CLASS = "org.bds.Bds"

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
func (be *BdsExec) BdsJava() int {
	// Create a TaskLoggerFile (temp file based on pid number)
	be.createTaskLoggerFile()
	defer os.Remove(be.TaskLoggerFile) // Make sure 'taskLogger' file is deleted

	be.cmdLineJava() // Create Java command line

	exitCode := be.executeCommand()	// Execute command
	if DEBUG {
		log.Printf("Debug BdsJava: Exit code '%d'\n", exitCode)
	}

	return exitCode
}


// Create Java command line
func (be *BdsExec) cmdLineJava() {
	bdsLibDir := path.Dir(be.execName) + "/" + BDS_NATIVE_LIB_DIR

	// Command to execute
	be.command = JAVA_CMD

	// Append all arguments from command line
	be.cmdargs = []string{ be.command,
		be.javaMem,
		JAVA_NATIVE_LIB + bdsLibDir,
		"-cp", be.execName,
		JAVA_BDS_CLASS }
	be.cmdargs = append(be.cmdargs, be.javaArgs...)	// Append JVM command line options
	be.cmdargs = append(be.cmdargs, "-pid")
	be.cmdargs = append(be.cmdargs, be.TaskLoggerFile)
	be.cmdargs = append(be.cmdargs, be.args[1:]...)	// Append 'bds' command line options
	if DEBUG {
		log.Printf("Debug cmdLineJava: Java command args %v\n", be.cmdargs)
	}
}
