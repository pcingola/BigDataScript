#!/bin/sh

while true
do
		echo
		echo "--------------------"
		bds exec 86400 z.out z.err z.exitCode ./z.sh 

		if [ -s z.out ] 
		then
			echo OK
		else
			echo BAD
			break
		fi
done

