#!/bin/sh -e


while true 
do 
		# Preparing to run
		rm -f ?.txt ?.final.txt z.bds.*.html
		for i in $(seq 0 9); do date > $i.txt; done

		# Ready to run
		./test/z.bds
done

