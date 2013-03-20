#!/bin/sh

clear; 
go clean
go build 

touch ls.out ls.err ls.exit
rm ls.{out,err,exit} 

./bds exec 3 ls.out ls.err ls.exit ./ls.sh -a -l -h 
echo bds exit code: $?

echo
echo
echo "---------- LS.OUT ----------"
cat ls.out

echo
echo
echo "---------- LS.ERR ----------"
cat ls.err

echo
echo
echo "---------- LS.EXIT ----------"
cat ls.exit

echo
echo
