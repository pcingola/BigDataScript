#!/bin/sh -e

(
for i in `find .`
do
	echo File $i
	for j in 1 2 3 4 5 6 7 8 
	do
		bds -d test/z.bds &
	done

	wait

done
) 2>&1 | tee run_z.out


