#!/bin/sh

# Delete old jar
mkdir $HOME/.bds

# Build Jar file
echo Building JAR file
ant 

# Build go program
echo
echo Building GO program
cd go/bds/
go clean
go build

# Copy binary
cp -vf bds $HOME/.bds/

