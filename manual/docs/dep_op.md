# Dependency operator `<-` 
				
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

					* This construction is so common that we allow for some syntactic sugar. 
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
