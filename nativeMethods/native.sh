#!/bin/sh

# Delete old files
rm -vf *.java 

# Create java classes
cat native.txt | ./native.pl 

# Copy to source tree
#mv -v -n NativeLibraryString.java ../src/ca/mcgill/mcb/pcingola/bigDataScript/lang/nativeMethods/
#mv -v -n *.java ../src/ca/mcgill/mcb/pcingola/bigDataScript/lang/nativeMethods/string/

