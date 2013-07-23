#!/bin/sh

while true
do
	next=`ls $HOME/.bds/fakeClusterTasks/task.* 2> /dev/null | head -n 1`

	if [ ! -z "$next" ]
	then
		echo "Executing task $next"
		$next

		echo "Deleting file $next"
		rm -f $next
	fi

	sleep 1
done
