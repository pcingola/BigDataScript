# Creating data pipelines 
In order to create data pipelines, you need to execute 'tasks' and coordinate execution dependencies. Here we show how to do it in `bds`
            
### Executing a task 
The most basic operation is to execute a task, which is done using a `task` expression.
`bds` takes care of executing a task on different environments (local computer, server, cluster, etc.), so you don't need to focus on mundane details (such as cluster queue monitoring, or querying remote computers for resources).


In this toy example, we schedule 10 tasks for execution. 
I'm running this on a computer that only has 8 CPUs, so not all tasks can execute in parallel.

File <a href="../bds/test_04.bds">test_04.bds</a>
```
#!/usr/bin/env bds

for( int i=0 ; i < 10 ; i++ ) {
    task echo Hi $i ; sleep 1 ; echo Done $i ; sleep 1
}
```

Note that bds interpolates variable `$i` (string interpolation simply means replacing by the value of a variable within the string)

```
$ ./test_04.bds
Hi 1
Hi 6
Hi 2
Hi 4
Hi 3
Hi 7
Hi 0
Hi 5
Done 1
Done 6
Done 2
Done 4
Done 3
Done 7
Done 0
Done 5
Hi 9
Hi 8
Done 9
Done 8
```

In this case, we are running on an 8 core computer, so the first 8 tasks get executed in parallel. 
The rest is executed when the first tasks finish.

**Warning:** Task execution order is not guaranteed (e.g. a cluster scheduler can decide to run tasks out of order).
We'll see how to coordinate tasks later.

<iframe width="640" height="390" src="http://www.youtube.com/embed/ehFfU8vLwi8" frameborder="0" allowfullscreen></iframe>


### Running on a cluster 
Here we show how exactly the same script is run on a cluster, keep in mind that not a single line of code changed. 
We copy tha same script (used in the previous section) to a cluster. 
We execute it, but now the tasks are scheduled using MOAB, Torque, PBS or Grid Engine.
        
```
$ bds -s cluster test_04.bds
```

<iframe width="640" height="390" src="http://www.youtube.com/embed/o47wxUdYzvk" frameborder="0" allowfullscreen></iframe>
        
### Task dependencies
        
In a typical pipeline we must be able to control task execution which depends on previously executed tasks.
Here we show how.
        
### Waiting for a task to finish '`wait`' 

The simplest form of execution control is to wait until one or more tasks finish before executing the next task(s).
This is done using the `wait` statement.

In this example, we run a set of tasks (`echo Hi $i`) and after all the tasks finished, we run another set of tasks (`echo Bye $i`)
File <a href="bds/test_05.bds">test_05.bds</a>
```
#!/usr/bin/env bds

for( int i=0 ; i < 10 ; i++ ) {
    task echo Hi $i ; sleep 1 
}

wait
print("After wait\n")

for( int i=0 ; i < 10 ; i++ ) {
    task echo Bye $i ; sleep 1
}
```

Running the script, we get
```
$ ./test_05.bds
Hi 7
Hi 0
Hi 6
Hi 5
Hi 1
Hi 3
Hi 4
Hi 2
Hi 8
Hi 9
After wait
Bye 1
Bye 0
Bye 7
Bye 2
Bye 6
Bye 4
Bye 3
Bye 5
Bye 9
Bye 8
```
* The `wait` statement is like a barrier. Until all tasks are finished, the program does not continue. If any task fails, a checkpoint file is created, where all program data is serialized. We can correct the problem and restart the pipeline where it left.
* The `wait` statement can wait for all tasks, for single tasks or for a list of tasks.
* We then run the same script on a cluster. Again, this is done simply by using `-s cluster` command line. The video shows how tasks are scheduled and on the cluster, always honoring the `wait` statement
* The exit code of a `bds` script is 0 if all tasks executed without any problems, and non-zero if any task failed.

<iframe width="640" height="390" src="http://www.youtube.com/embed/vTekXn3sKzs" frameborder="0" allowfullscreen></iframe>

### Dependency operator '`<-`' 

The dependency operator provides a simple way to see if a file needs to be updated (i.e. recalculated) with respect to some inputs.
For instance, when we already processed some files and have the corresponding results, we may save some work if the inputs have not changed (like "make" command).

We introduce the dependency operator `<-` (pronounced 'dep') which is a "make" style operator.
The expression `out <- in` is true if 'out' file needs to be updated.
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

**Summary:** If the file 'out.txt' is up to date with respect to 'in.txt', the following condition will be false and the `task` will not execute
```
if( outFile <- inFile ) {
    task echo Creating $outFile; cat $inFile > $outFile
}
```

This construction is so common that we allow for some syntactic sugar. 
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

* The first task uses an input file 'in.txt', to create an intermediate file 'inter.txt' 
* The second task uses the intermediate file 'inter.txt' to create the ouptut file 'out.txt'.

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
So when `bds` evaluates the first `task` expression, the dependency `intermediate <- inFile` is true (because 'inter.txt' doesn't exist, so it must be updated with respect to 'in.txt').
After that, when the second `task` expression is evaluated,  `out <- intermediate` is also true, since 'inter.txt' is newer than 'out.txt'.
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
* `dep` defines a task exactly the same way as `task` expression, but it doesn't evaluate if the tasks should be executed or not (it's just declarative). 
* `goal` executes all dependencies nescesary to create an output 

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
