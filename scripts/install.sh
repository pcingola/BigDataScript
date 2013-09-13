#!/bin/sh
set -e

echo "changing dir" `dirname $0`
# This script must be run from the parent directory 
cd `dirname $0` &&  cd ..

# Create 'bds' dir
mkdir -p $HOME/.bds 2> /dev/null

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
cat bds $HOME/.bds/BigDataScript.jar  > $HOME/.bds/bds
echo "binary created: $HOME/.bds/bds"

