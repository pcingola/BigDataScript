#!/bin/sh

taskDir="$HOME/.bds/fakeClusterTasks/"
doneDir="$HOME/.bds/fakeClusterTasks/done/"

while true
do
	next=`ls $taskDir/task.* 2> /dev/null | head -n 1`

	if [ ! -z "$next" ]
	then
		echo "Executing task $next"
		$next

		echo "Moving file $next to 'done'"
		mv $next $doneDir
	fi

	sleep 1
done
