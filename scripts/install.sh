#!/bin/sh -e

BDS_HOME="$HOME/.bds"

echo "Changing dir" `dirname $0`

# This script must be run from the parent directory 
cd `dirname $0` &&  cd ..

# Create 'bds' dir
mkdir -p "$BDS_HOME" 2> /dev/null

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
export GOPATH=$GOPATH:`pwd`
go clean
go build
go fmt

# Build binay (go executable + JAR file)
cat bds "$BDS_HOME/BigDataScript.jar" > bds.bin
mv bds.bin bds
chmod a+x bds
mv bds "$BDS_HOME"

# Remove JAR file
rm "$BDS_HOME/BigDataScript.jar"

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
	cp config/bds.default.config "$BDS_HOME/bds.config"
fi

echo "Done!"
