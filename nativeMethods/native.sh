#!/bin/sh

# Delete old files
rm -vf *.java 

# Create java classes
cat native.txt | ./native.pl 

# Copy to source tree
mv -vf NativeLibraryString.java ../src/ca/mcgill/mcb/pcingola/bigDataScript/lang/nativeMethods/
mv -vf *.java ../src/ca/mcgill/mcb/pcingola/bigDataScript/lang/nativeMethods/string/

