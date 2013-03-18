#!/bin/sh

clear; 
go build 

rm ls.{out,err,exit} 

./bds exec 3 ls.out ls.err ls.exit ./ls.sh -a -l -h  zxz
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
