package tee

import (
	"log"
	"os"
)

//-----------------------------------------------------------------------------
// Tee: Copy written data to output file and Stdout / Stderr
//-----------------------------------------------------------------------------

type Tee struct {
	outFile string
	useStdErr bool
	out *os.File
}

// Close Tee
func (t *Tee) Close() error {
	if t.out != nil {
		return t.out.Close()
	}

	return nil
}

// Initialize a Tee
func NewTee(outFile string, useStdErr bool) *Tee {

	// t := &Tee{outFile: outFile, useStdErr: useStdErr}
	t := &Tee{outFile, useStdErr, nil}

	// Copy to STDOUT to file (or to stdout)
	if (outFile == "") || (outFile == "-") {
		t.outFile = ""
	} else {
		out, err := os.Create(outFile)
		if err != nil {
			log.Fatal(err)
		}
		t.out = out
	}

	return t
}

// Write to Tee
func (t *Tee) Write(buf []byte) (n int, err error) {

	if t.out != nil {
		n, err = t.out.Write(buf)
	} else {
		n = len(buf)
		err = nil
	}

	// Also write to stdout / stderr
	if t.useStdErr {
		os.Stderr.Write(buf)
	} else {
		os.Stdout.Write(buf)
	}

	return n, err
}
