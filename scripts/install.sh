#!/bin/sh

# Create 'bds' dir
mkdir $HOME/.bds 2> /dev/null

# Build Jar file
echo Building JAR file
ant 

# Build go program
echo
echo Building GO program
cd go/bds/
go clean
go build bds.go

# Copy binary
cp -vf bds $HOME/.bds/

