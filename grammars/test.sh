#!/bin/sh -e

grammar="BigDataScript.g4"
init="programUnit"
testFile="../test/z.bds"

# Programs
jar="../lib/antlr-4.7.1-complete.jar"
antlr4="java -Xmx1g -cp $jar org.antlr.v4.Tool"
grun="java -Xmx1g -cp .:$jar org.antlr.v4.runtime.misc.TestRig"

# Delete old files
touch tmp.java tmp.class
rm -vf *.class *.java *.interp *.tokens

# Compile
echo Create Lexer and Parser
$antlr4 $grammar

echo Compile
javac -cp $jar *.java

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

