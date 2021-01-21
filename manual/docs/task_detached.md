# Detached tasks 

A "detached" task is a task that is run independently from the `bds` script.
The original `bds` program can finish and the detached task can continue running.

Detached tasks are created by setting `detached:= true`.

Detached tasks have several properties:
- They are considered successful immediately after they start "running" (or scheduled for running in a cluster system)
- The STDOUT & STDERR of detached tasks are not shown on the terminal running the `bds` script
- The exit status does not affect execution of the script. Even if a detached task fails, the script will finish successfully. 
- No other tasks can depend on a detached task, because `bds` does not track detached tasks.

Example: Here is a program executing a task with `detached:= true`
```
println "Before"

task(detached := true) {
	sys echo "Detached task: Start"
	sys sleep 60
	sys echo "Detached task: End"
}
println "After"
wait
println "Done"
```

If we execute, you'll see that the task executes in a second, but the task has a `sleep 60` so the task itself should take one minute to run:
```
$ time bds z.bds
Before
After
Done
bds z.bds  1.65s user 0.13s system 114% cpu 1.550 total
```

We immediately check if the "sleep" command is still running after the `bds` script finished:
``` 
$ ps auxw | grep sleep
bdsuser          87834   0.0  0.0  4268176    548 s002  S     1:56PM   0:00.00 sleep 60
``` 
So the detached task is still running after the `bds` script has finished.
 