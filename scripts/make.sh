#!/bin/bash -eu
set -o pipefail

# Delete old jar
ORIGDIR=${PWD}
BDS_JAR="${ORIGDIR}/build/bds.jar"
BDS_BIN="${ORIGDIR}/build/bds"

rm -f "$BDS_JAR" || true

# Make sure 'bin' dir exists
mkdir bin || true

# Build Jar file
echo Building JAR file
ant 

# Build go program
echo
echo Building bds wrapper: Compiling GO program
cd go/bds/
export GOPATH=`pwd`
go clean
go build 
go fmt

# Build binay (go executable + JAR file)
cat bds "$BDS_JAR" > "$BDS_BIN"
chmod a+x "$BDS_BIN"
echo "Bds executable: '$BDS_BIN'"

# Remove JAR file
rm "$BDS_JAR"

cd -
