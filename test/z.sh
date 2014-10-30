#!/bin/sh


echo Preparing to run
rm -vf ?.txt ?.final.txt
for i in $(seq 0 9); do date > $i.txt; done

echo Ready to run
sleep 1

./test/z.bds

