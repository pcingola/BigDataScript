#!/bin/bash -ex
set -o pipefail

if [ -z ${BDS_HOME} ]; then
    BDS_HOME="$HOME/.bds"
fi

ORIGDIR=${PWD}
BDS_JAR="${ORIGDIR}/build/bds.jar"
BDS_BIN="${ORIGDIR}/build/bds"

echo "Changing dir" `dirname $0`

# This script must be run from the parent directory 
cd `dirname $0` &&  cd ..

# Create 'bds' dir
mkdir -p "$BDS_HOME" 2> /dev/null || true

#---
# Build bds
#---

# Build Jar file
mkdir bin 2> /dev/null || true
echo Building JAR file
ant 

# Build go program
echo
echo Building GO program
cd go/bds/
export GOPATH=`pwd`
go clean
go build
go fmt

# Build binay (go executable + JAR file)
cat bds "$BDS_JAR" > "$BDS_BIN"
chmod a+x "$BDS_BIN"
mv "$BDS_BIN" "$BDS_HOME"

# Remove JAR file
rm "$BDS_JAR"

# Binary installed
echo "Binary created: $BDS_HOME/bds"

#---
# Copy other stuff
#---

# Copy 'include' dir
echo
echo "Copying include files"
cd - > /dev/null
cp -rvf include "$BDS_HOME"

if [ ! -e "$BDS_HOME/bds.config" ]
then
	echo "Copying default config file"
	cp config/bds.config "$BDS_HOME/bds.config"
fi

# Copy mesos related libraries
mkdir -p $BDS_HOME/mesos || true
cp -vf lib/mesos*jar lib/protobuf*.jar $BDS_HOME/mesos
cp -vf scripts/bds_mesos_executor.sh $BDS_HOME/mesos

echo "Done!"
