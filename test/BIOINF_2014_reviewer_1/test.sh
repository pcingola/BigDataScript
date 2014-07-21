#!/bin/sh

rm -vf test.txt test.csv test.xml *.html

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
cat test.2.out | grep ^copy
echo
echo
echo Ouptut 2:
cat test.2.out | grep ^copy

