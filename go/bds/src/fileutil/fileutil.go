package fileutil

import (
	"bufio"
	"bytes"
	"log"
	"os"
)

// Does the file exist?
func FileExists(name string) bool {
	f, err := os.OpenFile(name, os.O_RDWR|os.O_CREATE|os.O_EXCL, 0600)
	defer f.Close()
	return os.IsExist(err)
}

/*
	Read a line from a file
*/
func ReadLine(reader *bufio.Reader) (line string, err error) {
	var part []byte
	var prefix bool

	buffer := bytes.NewBuffer(make([]byte, 0))
	for {
		if part, prefix, err = reader.ReadLine(); err != nil {
			break
		}
		buffer.Write(part)
		if !prefix {
			line = buffer.String()
			return
		}
	}
	return
}

/*
	Write a string to a file
*/
func WriteFile(fileName, message string) {
	file, err := os.Create(fileName)
	if err != nil {
		log.Fatal(err)
	}
	defer file.Close()
	file.WriteString(message)
}
