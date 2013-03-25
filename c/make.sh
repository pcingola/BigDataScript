#!/bin/sh

clear; 
touch bds
rm -f bds
gcc -o bds bds.c

touch ls.out ls.err ls.exit
rm ls.{out,err,exit} 

./bds exec ./ls.sh -a -l -h 
echo bds exit code: $?
