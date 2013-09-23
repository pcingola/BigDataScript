#!/bin/sh

# Delete old jar
rm -f $HOME/.bds/BigDataScript.jar

# Make sure 'bin' dir exists
mkdir bin

# Build Jar file
echo Building JAR file
ant 

# Build go program
echo
echo Building bds wrapper: Compiling GO program
cd go/bds/
go clean
go build bds.go
go fmt

# Build binay (go executable + JAR file)
cat bds $HOME/.bds/BigDataScript.jar > bds.bin
mv bds.bin bds
chmod a+x bds

# Remove JAR file
rm $HOME/.bds/BigDataScript.jar

cd -
