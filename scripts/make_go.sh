#!/bin/bash -eu
set -o pipefail

# Make sure 'bin' dir exists
mkdir -p bin

# Build go program
echo
echo Building bds wrapper: Compiling GO program
cd go/bds/
export GOPATH=`pwd`
go clean
go build 
go fmt

cd -
