package tmpfile

import (
	"log"
	"os"
	"strconv"
	"time"
	"fileutil"
)

// Verbose & Debug
const DEBUG = false
const VERBOSE = false

var randTempFile uint32 = randSeed()

// Create a new seed for random numbers
// Code from ioutil
func randSeed() uint32 {
	return uint32(time.Now().UnixNano() + int64(os.Getpid()))
}

//
// Create a temp file. Retun file name instead of file descriptor
//
// Code adapted from ioutil.TempFile
// Ref: http://golang.org/src/pkg/io/ioutil/tempfile.go
//
// TempFile creates a new temporary file in the directory dir
// with a name beginning with prefix, opens the file for reading
// and writing, and returns the resulting *os.File.
// If dir is the empty string, TempFile uses the default directory
// for temporary files (see os.TempDir).
// Multiple programs calling TempFile simultaneously
// will not choose the same file.  The caller can use f.Name()
// to find the name of the file.  It is the caller's responsibility to
// remove the file when no longer needed.
func TempFile(prefix string) (name string, err error) {
	if DEBUG {
		log.Printf("Debug: tempFile\n")
	}

	name = prefix

	// Is just the prefix OK?
	if !fileutil.FileExists(prefix) {
		return
	}

	nconflict := 0
	for i := 0; i < 10000; i++ {
		name = prefix + "." + tempFileNextSuffix()
		if fileutil.FileExists(name) {
			if nconflict++; nconflict > 10 {
				randTempFile = randSeed()
			}
			continue
		}
		break
	}
	return
}

//  from ioutil
func tempFileNextSuffix() string {
	r := randTempFile
	if r == 0 {
		r = randSeed()
	}
	r = r*1664525 + 1013904223 // constants from Numerical Recipes
	randTempFile = r
	return strconv.Itoa(int(1e9 + r%1e9))[1:]
}
