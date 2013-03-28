#!/bin/sh -e

# Delete old jar
rm -vf $HOME/.bds/BigDataScript.jar 

# Build Jar file
echo Building JAR file
ant 

# # Build go program
# cd c
# echo Building bds wrapper: Compiling C program
# rm -rvf bds
# gcc -o bds bds.c
# cd -

# Build go program
echo
echo Building bds wrapper: Compiling GO program
cd go/bds/
go clean
go build
go fmt
cd -

