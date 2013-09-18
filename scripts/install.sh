#!/bin/sh
set -e

echo "Changing dir" `dirname $0`

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

# Build binay (go executable + JAR file)
cat bds $HOME/.bds/BigDataScript.jar > bds.bin
mv bds.bin bds

# Remove JAR file
rm $HOME/.bds/BigDataScript.jar

# Done
echo "Binary created: $HOME/.bds/bds"

