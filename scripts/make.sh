#!/bin/sh

# Delete old jar
rm -f $HOME/.bds/BigDataScript.jar

# Build Jar file
echo Building JAR file
ant 

# Build go program
echo
echo Building bds wrapper: Compiling GO program
cd go/bds/
go clean
go build
go fmt
cd -

