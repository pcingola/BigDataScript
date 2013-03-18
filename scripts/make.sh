#!/bin/sh -e

# Delete old jar
rm -vf $HOME/.bds/BigDataScript.jar 

# Build Jar file
echo Building JAR file
ant 

# Build go program
echo
echo Building bds wrapper
cd go/bds/
go clean
go build
go fmt
cd -

