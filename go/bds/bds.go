package main

/* 
	Execute module for BigDataScript:

	Commands:
		1) default (no command)	:	Execute main Java package (compiler and interpreter)
		2) exec					:	Execute shell scripts, set maimum execution time, redirec STDOUT and STDERR, write exit code to a file
*/

import (
	"io"
	"log"
	"os"
	"os/exec"
)

func main() {
	BigDataScript()
}


/**
	Invoke BigDataScript java program
	WARNING: It is assumed that BigDataScript.jar is in the CLASSPATH
*/
func BigDataScript() {
	// Create output file
	dstName := "ls.out"
	dst, err := os.Create(dstName)
    if err != nil {
        return
    }
    defer dst.Close()

	// Create command
	cmd := exec.Command("java")
	// Append all arguments from command line
	args := []string{"java", "-Xmx1G", "ca.mcgill.mcb.pcingola.bigDataScript.BigDataScript"}
	for _,arg := range os.Args[1:] {
		args = append(args, arg)
	}
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

	// Copy to STDOUT and STDERR
	go io.Copy(os.Stdout, stdout)
	go io.Copy(os.Stderr, stderr)

	// Wait for command to finish
	cmd.Wait()
}
