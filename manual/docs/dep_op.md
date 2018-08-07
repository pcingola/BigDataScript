### Dependency operator '`&lt;-`' 
				
				The dependency operator provides a simple way to see if a file needs to be updated (i.e. recalculated) with respect to some inputs.
				For instance, when we already processed some files and have the corresponding results, we may save some work if the inputs have not changed (like "make" command).
				
				
				We introduce the dependency operator `&lt;-` (pronounced 'dep') which is a "make" style operator.
				The expression `out &lt;- in` is true if 'out' file needs to be updated.
				More formally, the expression is true if the file name represented by the variable 'out' does not exist, is empty (zero length) or has a creation date before 'in'.
				E.g.:
				
				 File <a href="bds/test_06.bds">test_06.bds</a>
```
#!/usr/bin/env bds

string inFile  = "in.txt"
string outFile = "out.txt"

# Create 'in.txt' if it doesn't exist
if( !inFile.exists() ) {
    task echo Creating $inFile; echo Hello > $inFile
}

wait

# Create 'out.txt' only if needs to be updated resepct to 'in.txt'
if( outFile <- inFile ) {
    task echo Creating $outFile; cat $inFile > $outFile
}
```

When executing for the first time, both tasks are executed and both files ('in.txt' and 'out.txt') are created.
```
$ ./test_06.bds
Creating in.txt
Creating out.txt
```

If we execute for a second time, since files have not changed, no `task` is executed.
```
$ ./test_06.bds
$
```

If we now change the contents of 'in.txt', and run the script again, the second `task` will be executed (because 'out.txt' needs to be updated with respect to 'in.txt')
```
# Update 'in.txt'
$ date > in.txt

# Since we updated the input file, the output must be recalculated
$ ./test_06.bds 
Creating out.txt
```

					<li> Summary: If the file 'out.txt' is up to date with respect to 'in.txt', the following condition will be false and the `task` will not execute
```
if( outFile <- inFile ) {
	task echo Creating $outFile; cat $inFile > $outFile
}
```

					<li> This construction is so common that we allow for some syntactic sugar. 
```
task( outFile <- inFile ) { 
	sys echo Creating $outFile; cat $inFile > $outFile
}
```
				

				
				
				<iframe width="640" height="390" src="http://www.youtube.com/embed/oSjhkRuc0I8" frameborder="0" allowfullscreen></iframe>
				

### Automatic task dependency detection 
				
				Programming task dependencies can be difficult.
				BDS can help by automatically inferring task dependencies and executing tasks in the correct order.
				
				 In this example, we have two tasks:
				<ul>
					<li> The first task uses an input file 'in.txt', to create an intermediate file 'inter.txt' 
					<li> The second task uses the intermediate file 'inter.txt' to create the ouptut file 'out.txt'.
				
				The script below does not have a `wait` statement. 
				Instead `bds` automatically infers that the second task depends on the first one, and does not start execution until the first task begins.
				Notice that we don't tell bds to wait for the first task to finish (there is no explicit `wait` statement). 
				
				 File <a href="bds/test_07.bds">test_07.bds</a>
```
#!/usr/bin/env bds

# We use ':=' for declaration with type inference
inFile       := "in.txt"		
intermediate := "inter.txt"
outFile      := "out.txt"

task( intermediate <- inFile) {
    sys echo Creating $intermediate; cat $inFile > $intermediate; sleep 1 ; echo Done $intermediate
}

task( outFile <- intermediate ) {
    sys echo Creating $outFile; cat $intermediate > $outFile; echo Done $outFile
}
```

				As a side note: We used the `:=` operator to declare variables using type inference. 
				So we can write `inFile := "in.txt"` instead of `string inFile = "in.txt"`, which not only is shorter to type, but also makes the code look cleaner.
				
				
				Now let's run the script
```
# Delete old file (if anY)
$ rm *.txt

# Create input file
$ date > in.txt

# Run
$ ./test_07.bds 
Creating inter.txt
Done inter.txt
Creating out.txt
Done out.txt
```
				Note how the second task is executed only after the first one finished.

### Complex dependencies: `dep` and `goal` 
				
				The `goal` statement helps to program complex task scheduling interdependencies.
				

				In the previous example, we had an input file 'in.txt', an intermediate file 'inter.txt' and an output file 'out.txt'.
				One problem is that if we delete the intermediate file 'inter.txt' (e.g. because we may want to delete big files with intermediate results), then both tasks will be executed

				
				For convenience, here is the code again. File <a href="bds/test_07.bds">test_07.bds</a>
```
#!/usr/bin/env bds

inFile       := "in.txt"		
intermediate := "inter.txt"
outFile      := "out.txt"

task( intermediate <- inFile) {
    sys echo Creating $intermediate; cat $inFile > $intermediate; sleep 1 ; echo Done $intermediate
}

task( outFile <- intermediate ) {
    sys echo Creating $outFile; cat $intermediate > $outFile; echo Done $outFile
}
```
```
# Remove intermediate file
$ rm inter.txt 

# Re-execute script 
$ ./test_07.bds
Creating inter.txt
Done inter.txt
Creating out.txt
Done out.txt
```

				Why is this happening? The reason is that `task` statements are evaluated in order. 
				So when `bds` evaluates the first `task` expression, the dependency `intermediate &lt;- inFile` is true (because 'inter.txt' doesn't exist, so it must be updated with respect to 'in.txt').
				After that, when the second `task` expression is evaluated,  `out &lt;- intermediate` is also true, since 'inter.txt' is newer than 'out.txt'.
				As a result, both tasks are re-executed, even though 'out.txt' is up to date with respect to 'in.txt'.
				This can be a problem, particularly if each task requires several hours of execution.
				
				
				There are two ways to solve this, the obvious one is to add a simple 'if' statement surrounding the tasks:
				
				
```
#!/usr/bin/env bds

inFile       := "in.txt"		
intermediate := "inter.txt"
outFile      := "out.txt"

if( outFile <- inFile) {
  task( intermediate <- inFile) {
    sys echo Creating $intermediate; cat $inFile > $intermediate; sleep 1 ; echo Done $intermediate
  }

  task( outFile <- intermediate ) {
    sys echo Creating $outFile; cat $intermediate > $outFile; echo Done $outFile
  }
}
```

				Although it solves the issue, the code is not elegant.
				
				
				The alternative is to use `dep` and `goal`
				<ul>
					<li> `dep` defines a task exactly the same way as `task` expression, but it doesn't evaluate if the tasks should be executed or not (it's just declarative). 
        
					<li> `goal` executes all dependencies nescesary to create an output 
				
				Example:
				
				 File <a href="bds/test_08.bds">test_08.bds</a>
```
#!/usr/bin/env bds

inFile       := "in.txt"		
intermediate := "inter.txt"
outFile      := "out.txt"

dep( intermediate <- inFile) {
    sys echo Creating $intermediate; cat $inFile > $intermediate; sleep 1 ; echo Done $intermediate
}

dep( outFile <- intermediate ) {
    sys echo Creating $outFile; cat $intermediate > $outFile; echo Done $outFile
}

goal outFile
```

			If we execute this script
```
# Delete old files (if any)
$ rm *.txt

# Create input file
$ date > in.txt

# Run script (both tasks should be executed)
$ ./test_08.bds
Creating out.txt
Done out.txt
Creating inter.txt
Done inter.txt
```
			Now we delete 'inter.txt' and re-execute

```
# Delete intermediate file
$ rm inter.txt 

# Run again (out.txt is still up to date with respect to in.txt, so no task should be executed)
$ ./test_08.bds
$ 
```

			As you can see, no task is executed the second time, since 'out.txt' is up to date, with respect to 'in.txt'. 
			The fact that intermediate file 'inter.txt' was deleted, is ignored, which is what we wanted.

			
				
			
# Sys 
				 Local, immediate command execution.  

				A few rules about `sys` expression:
				<ul>
					<li> Everything after `sys` until the end of the line is interpreted to be a command
					<li> If the line ends with a backslash, next line is interpreted as part of the same command (same as in a shell script)
					<li> Variables are interpolated. E.g. ` sys echo Hello $person` will replace '$person' by the variable's name before sending it to the OS for execution.
					<li> BDS immediately executes the OS command in the local machine and waits until the command finishes execution.
					<li> If the command exits with an error condition, then `bds` creates a checkpoint, and exits with a non-zero exit code.
					<li> No resource accounting is performed, the command is executed even if all CPUs are busy executing tasks.
				E.g.
```
$ cat z.bds
print("Before\n")
sys echo Hello
print("After\n")

$ bds z.bds
Before
Hello
After
```

				<li> Everything after a `sys` keyword until the end of the line is considered part of the command to be executed.
				So multiple shell commands can be separated by semicolon
```
sys echo Hello ; echo Bye
```

				<li> Multi-line statements are allowed, by using a backslash at the end of the line (same as a shell script)

```
sys echo HeLLo \
	| tr [A-Z] [a-z] \
	| grep hell
```
				<li> `sys` returns the STDOUT of the command:
				 File <a href="bds/test_12.bds">test_12.bds</a>
```
#!/usr/bin/env bds

dir := sys ls *.bds | head -n 3
print("\nVariable dir is:\n$dir\n")
```

				Executing, we get:
```
$ ./test_12.bds 
test_01.bds
test_02.bds
test_03.bds

Variable dir is:
test_01.bds
test_02.bds
test_03.bds
```
                Note that (roughly) the first half of the output is printed by the command execution, while the second half is printed by the `print` statement (i.e. printing the variable `dir`).

				<li> Characters are passed literally to the interpreting shell. 
					 For example, when you write '\t' it is NOT converted to a tab character before sending it to the shell (no escaping is required):
```
$ cat z.bds
sys echo -e "Hello\tWorld" | awk '{print $1 "\n" $2}'

$ bds z.bds
$ bds z.bds
Hello
World

```
				
			

			<!-- ================================================== -->
			
# Task 
				 Queued command execution with resource management. 

				A `task`expression, just like `sys` expression, also executes a command.
				The main difference is that a `task` is "scheduled for execution" instead of executed immediately.
				Task execution order is not guaranteed but `bds` provides a mechanism for creating task dependencies by means of `wait` statements.
				
				
				A task expression either performs basic resource management or delegates resource management to cluster management tools.
				The idea is that if you schedule a hundred tasks, but you are executing on your laptop which only has 4 CPUs, then `bds` will only execute 4 tasks at a time (assuming each task is declared to consume 1 CPU).
				The rest of the tasks are queued for later execution.
				As executing tasks finish and CPUs become available, the remaining tasks are executed.
				
				Similarly, if you schedule 10,000 tasks for execution, but your cluster only has 1,000 cores, then only 1,000 tasks will be executed as a given time.
				Again, other tasks are queued for later execution, but in this case, all the resource management is done by your cluster's workload management system (e.g. GridEngine, PBS, Torque, etc.).
				
				
				<i class="icon-warning-sign"></i> Most cluster resource management do not guarantee that tasks are executed in the same order as queued.
				Even if they do or if they are executed in the same host, a task can start execution and immediately be preempted. 
				So the next task in the queue can effectively start before the previous one.
				
				
				There are different ways to execute tasks
				<table class="table table-striped">
					 
						  System type  
						  Typical usage  
						  How it is done  
					 
					 
						  `local`  	
						  Running on a single computer. E.g. programming and debugging on your laptop or running stuff on a server   
						  A local queue is created, the total number of CPUs used by all tasks running is less or equal than the number of CPU cores available   
					 
					 
						  `ssh`  	
						  A server farm or a bunch of desktops or servers without a workload management system (e.g. computers in a University campus)  
						  Basic resource management is performed by logging into all computers in the 'cluster' and monitoring resource usage.  
					 
					  
						  `cluster`  
						  Running on a cluster (GridEngine, Torque)  
						  Tasks are scheduled for execution (using 'qsub' or equivalent command). Resource management is delegated to cluster workload management.  
					 
					  
						  `moab`  
						  Running on a MOAB/PBS cluster </a>  
						  Tasks are scheduled for execution (using 'msub'). Resource management is delegated to cluster workload management.  
					 
					  
						  `pbs`  
						  Running on a PBS cluster </a>  
						  Tasks are scheduled for execution (using 'msub'). Resource management is delegated to cluster workload management.  
					 
					  
						  `sge`  
						  Running on a SGE cluster </a>  
						  Tasks are scheduled for execution (using 'qsub'). Resource management is delegated to cluster workload management.  
					 
					  
						  `generic`  
						  Enable user defined scripts to run, kill and find information on tasks</a>  
						  This 'generic' cluster allows the user to write/customize scripts that send jobs to the cluster system. 
							 It can be useful to either add cluster systems not currently supported by `bds`, or to customize parameters and scheduling options beyond what `bds` allows to customize in the config file.
							 For details, see bds.config file and examples in the project's source code (directories `config/clusterGeneric*`).
						 
					 
					  
						  `mesos`  
						  Running on a Mesos framework </a>  
						  Tasks are scheduled for execution in Mesos framework and resource management is delegated to Mesos.  
					 
				</table>
				
				### Scheduling tasks 
				A task is scheduled by means of a `task` expression.
				A `task` expression returns a task ID, a string representing a task.
				E.g.:
				 File <a href="bds/test_09.bds">test_09.bds</a>
```
tid := task echo Hello 
print("Task is $tid\n")
```

				Running we get:
```
$ ./test_09.bds
Task is test_09.bds.20140730_214947_810/task.line_3.id_1
Hello
```

				`task` is non-blocking, which means that `bds` continues execution immediately without waiting for the task to finish.
				So, many tasks can be scheduled by simply invoking a task statement many times. 
				
				Once a `task` is scheduled, execution order depends on the underliying system and there is absolutely no guarantee about execution order (unless you use a `wait` statements or other dependency mechanism).
				
				E.g., this example shows clearly all the tasks are NOT executed in order, even on local computers:
				 File <a href="bds/test_10.bds">test_10.bds</a>
```
#!/usr/bin/env bds

for( int i=0 ; i < 10 ; i++ ) task echo Hi $i
```

```
$ ./test_10.bds
Hi 0
Hi 5
Hi 4
Hi 3
Hi 2
Hi 1
Hi 7
Hi 6
Hi 9
Hi 8
```

			### Resource consumption and task options
			Often `task` requires many CPUs or resources.
			In such case, we should inform the resource management system in order to get an efficient allocation of resources (plus many cluster systems kill tasks that fail to report resources correctly).
			
			E.g., In this example we allocate 4 CPUs per task and run it on an 8-core computer, so obviously only 2 tasks can run at the same time:
				 File <a href="bds/test_11.bds">test_11.bds</a>
```
#!/usr/bin/env bds

for( int i=0 ; i < 10 ; i++ ) {
    # Inform resource management that we need 4 core on each of these tasks
    task ( cpus := 4 ) {
        sys echo Hi $i ; sleep 1; echo Done $i
    }
}
```

			Executing on my 8-core laptop, you can see that only 2 tasks are executed each time (each task is declared to require 4 cpus)
```
$ ./test_11.bds
Hi 0
Hi 1
Done 0
Done 1
Hi 3
Hi 2
Done 2
Done 3
Hi 4
Hi 5
Done 4
Done 5
Hi 6
Hi 7
Done 6
Done 7
Hi 9
Hi 8
Done 8
Done 9
```
				
				List of resources or task options
				<table class="table table-striped">
					 
						  Variable name  
						  Default value  
						  Resource / Task options  
					 
					 
						  `cpus`  	
						  1   
						  Number of CPU (cores) used by the process.   
					 
					 
						  `allowEmpty`  	
						  false   
						  If true, empty files are allowed in task's outputs. This means that a task producing empty files does not result in program termination and checkpointing.   
					 
					 
						  `canFail`  	
						  false   
						  If true, a task is allowed to fail. This means that a failed task execution does not result in program termination and checkpointing.   
					 
					 
						  `timeout`  	
						  0   
						  Number of seconds that a process is allowed to execute. Ignored if zero or less. If process runs more than `timeout` seconds, it is killed.   
					 
					 
						  `node`  	
						     
						  If possible this task should be executed on a particular cluster node. This option is only used for cluster systems and ignored on any other systems.   
					 
					 
						  `queue`  	
						     
						  Queue name of preferred execution queue (only for cluster systems).   
					 
					 
						  `retry`  	
						  0   
						  Number of times a task can be re-executed until it's considered failed.   
					 
					 
						  `taskName`  	
						     
						  Assign a task name. This adds a label to the task as well as the taskId returned by `task` expression. Task ID is used to create log files related to the task (shell script, STDOUT, STDERR and exitCode files) so those file names are also changed. This makes it easier to find tasks in the final report and log files (it has no effect other than that). Note: If taskName contains non-allowed characters, they are sanitized (replaced by '_').  
					 
				</table>

				### Conditional execution 
				Conditional execution of tasks can, obviously, be achieved using an `if` statement.
				Since conditional execution is so common, we allow for some syntactic sugar by ` task( expression1, expression2, ... ) { ... } `.
				where `expression1`, `expression2`, etc. are either boolean expressions or variable declarations.
				The task is executed only if all `bool` expressions are `true`.
				
				So the following programs are equivalent
```
shouldExec := true
if( shouldExec ) {
	task( cpus := 4 ) {
		sys echo RUNNING
	}
}
```
				Is the same as:

```
shouldExec := true

task( shouldExec, cpus := 4 ) {
	sys echo RUNNING
}
```

				<i class="icon-warning-sign"></i> This feature is particularly useful when combined with the dependency operator `&lt;-`. 
				For instance, the following `task` will be executed only if 'out.txt' needs to be updated with respect to 'in.txt'
```
in  := 'in.txt'
out := 'out.txt'

task( out <- in , cpus := 4 ) {
	sys echo $in > $out
}
```

				### Syntax sugar 
				There are many ways to write task expressions, here we show some examples.

				<ul>
					<li> A simple task 
```
task echo RUNNING
```
					<li> The same simple task
```
task {
	sys echo RUNNING
}
```
					<li> A simple, multi-line task (a backslash at the end of the line continues in the next line, just like in a shell script)
```
task cat file.txt \
		| grep "^results" \
		| cut -f 2 \
		| sort \
		> out.txtx
```
					<li> A more complex multi-line task (sys commands are just multiple lines in a bash script)
```
task {
	sys cat file.txt | grep "^results" > out.txt
	sys cat other.txt | grep "^exclude" > words.txt
	sys grep -v -f words.txt out.txt > excluded.txt
	sys wc -l excluded.txt
}
```
					<li> A task with dependencies
```
task ( out <- in ) {
	sys cat $in | grep "^results" > $out
	sys cat other.txt | grep "^exclude" > words.txt
	sys grep -v -f words.txt $out > excluded.txt
	sys wc -l excluded.txt
}
```
					<li> A task with multiple inputs and outputs dependencies
```
task ( [out1, out2] <- [in1, in2] ) {
	sys cat $in1 | grep "^results" > $out1
	sys cat $in1 $in2 | wc -l > $out2
}
```
					<li> A task with multiple inputs and outputs dependencies, using 4 CPUs and declaring a local variable 'tmp'
```
task ( [out1, out2] <- [in1, in2] , cpus := 4 , tmp := "$in1.tmp" ) {
	sys cat $in1 | grep "^results" > $out1
	sys cat $in1 $in2 > $tmp
	sys wc -l $tmp | wc -l > $out2
}
```
					<li> A task with a label (`taskName`) is easier to find in the report
```
task ( out <- in, cpus := 4 , taskName := "Filter results" ) {
	sys cat $in | grep "^results" > $out
}
```
				
			

			<!-- ================================================== -->
			
# Wait 
				 Task coordination mechanisms rely on waiting for some tasks to finish before starting new ones.
				As we mentioned several times, task execution order is not guaranteed.
				 File <a href="bds/test_13.bds">test_13.bds</a>
```
#!/usr/bin/env bds

for( int i=0 ; i < 10 ; i++ ) task echo BEFORE $i
for( int i=0 ; i < 10 ; i++ ) task echo AFTER $i
```

```
$ ./test_13.bds
BEFORE 0
BEFORE 4
BEFORE 3
BEFORE 2
BEFORE 1
BEFORE 5
BEFORE 7
BEFORE 6
BEFORE 8
AFTER 1
AFTER 0
BEFORE 9	<-- !!!
AFTER 6
AFTER 5
AFTER 4
AFTER 3
AFTER 2
AFTER 7
AFTER 8
AFTER 9
```
				If a task must be executed after another task finishes, we can introduce a `wait` statement.
				 File <a href="bds/test_13.bds">test_13.bds</a>
```
#!/usr/bin/env bds

for( int i=0 ; i < 10 ; i++ ) task echo BEFORE $i

wait    # Wait until ALL scheduled tasks finish
print("We are done waiting, continue...\n")

for( int i=0 ; i < 10 ; i++ ) task echo AFTER $i

```

				Now, we are sure that all tasks 'AFTER' really run after 'BEFORE'
```
$ ./test_14.bds 
BEFORE 0
BEFORE 2
BEFORE 1
BEFORE 4
BEFORE 3
BEFORE 5
BEFORE 6
BEFORE 7
BEFORE 8
BEFORE 9
We are done waiting, continue...
AFTER 0
AFTER 1
AFTER 2
AFTER 3
AFTER 4
AFTER 5
AFTER 6
AFTER 7
AFTER 8
AFTER 9
```
				We can also wait for a specific task to finish by providing a task ID `wait taskId`, e.g.:
```
string tid = task echo Hi
wait tid	# Wait only for one task
```

				Or you can wait for a list of tasks. 
				For instance, in this program, we create a list of two task IDs and `wait` on the list:
```
string[] tids

for( int i=0 ; i < 10 ; i++ ) {
	# Tasks that wait a random amount of time
	int sleepTime = randInt( 5 )
	string tid = task echo BEFORE $i ; sleep $sleepTime ; echo DONE $i

	# We only want to wait for the first two tasks
	if( i < 2 ) tids.add(tid)
}

# Wait for all tasks in the lists (only the first two tasks)
wait tids
print("End of wait\n")
```

				When we run it, we get:
```
$ bds z.bds
BEFORE 2
BEFORE 0
BEFORE 7
BEFORE 5
BEFORE 6
BEFORE 4
BEFORE 3
BEFORE 1
DONE 0
DONE 3
DONE 4
DONE 5
DONE 6
DONE 7
BEFORE 8
BEFORE 9
DONE 1
End of wait		<- Wait finished here
DONE 2
DONE 8
DONE 9
```

				<i class="icon-warning-sign"></i> There is an implicit `wait` statement at the end of the program. 
				So a program does not exit until all tasks have finished running.
				
				
			

			<!-- ================================================== -->
			
# Dependency operator 
				 You can use a dependency operator `&lt;-` to decide whether tasks should be run or not, based on file existence and time-stamps.  

				The dependency operator is written as ` out &lt;- in`.
				It is true if `out` file needs to be created or updated.
				This means that the operator is true if any of the following is satisfied:
				<ul>
					<li> `out` file does not exist
					<li> `out` file is empty (has zero length)
					<li> `out` latest modification time is earlier than `in` latest modification time
				
				 File <a href="bds/test_15.bds">test_15.bds</a>
```
in  := "in.txt"
out := "out.txt"
if( out <- in ) print("We should update $out\n")
```

				Running the script:
```
$ touch in.txt              # Create in.txt
$ ./test_15.bds
We should update out.txt

$ touch out.txt             # Create zero length out.txt
$ ./test_15.bds
We should update out.txt

$ ls > out.txt              # Create a non-empty out.txt
$ ./test_15.bds
$                           # Nothing done

$ echo hi > in.txt          # Update in.txt
$ ./test_15.bds
We should update out.txt    # Logically, out needs updating
```
				
				<i class="icon-hand-right"></i> In the dependency operator, `in` and `out` can be lists of files. 
				The same rules apply: The operator is true if any out file is missing, zero length or the minimum of modification times in `out` is less than the maximum modificaton times in `in`
				
				
				This can be also used on lists:
```
in1 := "in1.txt"
in2 := "in2.txt"
out := "out.txt"

if( out <- [in1, in2] ) print("We should update $out\n")
```
				or even:
```
in1 := "in1.txt"
in2 := "in2.txt"
out1 := "out1.txt"
out2 := "out2.txt"

if( [out1, out2] <- [in1, in2] ) print("We should update $out1 and $out2\n")
```
				
				A typical usage of `&lt;-` is in conjunction with `task`.
				E.g.
```
task( out <- in ) {
    sys cat $in > $out
}
```
				The command is executed only if `out` need updating
			

