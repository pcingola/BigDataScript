#!/bin/sh

# Delete old jar
rm -vf $HOME/.bds/BigDataScript.jar 

# Build Jar file
echo Building JAR file
ant 

# Build C program
echo
echo Building bds wrapper
cd c
rm -vf bds
gcc -o bds bds.c
cd -

