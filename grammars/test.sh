#!/bin/sh -e

grammar="BigDataScript.g4"
init="programUnit"
testFile="../test/z.bds"

# Programs
antlr4="java -Xmx1g org.antlr.v4.Tool"
grun="java -Xmx1g org.antlr.v4.runtime.misc.TestRig"

# Delete old files
touch tmp.java tmp.class
rm *.class *.java 

# Compile
echo Create Lexer and Parser
$antlr4 $grammar

echo Compile
javac *.java

#---
# Run
#---

echo Run grammar
base=`basename $grammar .g4`

echo
echo Tokens
$grun $base $init -tokens $testFile

echo
echo Tree
$grun $base $init -tree $testFile

echo
echo Trace
$grun $base $init -trace $testFile

echo
echo Diagnostics
$grun $base $init -diagnostics $testFile

echo
echo 
$grun $base $init -gui $testFile

