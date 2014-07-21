#!/bin/sh

rm -vf test.txt test.csv test.xml test.1.out test.2.out *.html

echo
echo
echo
echo Running for the first time
echo "TEST" | tr -d "\n" > test.txt
bds test.bds | tee test.1.out

echo
echo
echo
echo Running again
rm test.csv
bds -d test.bds 2>&1 | tee test.2.out

echo
echo
echo Ouptut 1:
grep ^copy test.1.out 
echo
echo
echo Ouptut 2:
grep ^copy test.2.out 

