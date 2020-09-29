# Goals 
Complex dependencies can be defined using `goal` and `dep` 

`goal` and `dep` are used to express dependencies in a declarative manner.
As opposed to `task` expression, which are evaluated immediately, `dep` can be used to define a dependency (using the same syntax as `task`). 
A `dep` is not evaluated until a `goal` requires that dependency to be triggered.

E.g.: File <a href="bds/test_18.bds">test_18.bds</a>
```
#!/usr/bin/env bds

in   := 'in.txt'
mid1 := 'mid1.txt'
mid2 := 'mid2.txt'
out  := 'out.txt'

stime := 3

# Dependencies: There is no need declare them in order
dep( out <- mid2 )     sys echo $mid2 > $out  ; echo OUT   ; sleep 1
dep( mid2 <- mid1 )    sys echo $mid1 > $mid2 ; echo MID2  ; sleep 1
dep( mid1 <- in )      sys echo $in   > $mid1 ; echo MID1  ; sleep 1

goal out

```

Running the code, we get
```
# Remove old files (if any)
$ rm *.txt

# Create input
$ date > in.txt

$ ./test_18.bds
MID1
MID2
OUT
```

In this case, `bds` created a directed acyclic graph of the dependencies needed to satisfy the goal 'out.txt' and then executed the required 'dep' declarations.
				
**Note:** A `goal` expression returns a list of task Ids to be executed, which can be quite useful for debugging purposes. So in the previous example you could write:
```
tids := goal out
print "Executing tasks: $tids\n"
```
				
### Intermediate files within a 'goal' can be deleted 

In the previous example, if we delete the intermediate files 'mid1.txt' and or 'mid2.txt', and we re-execute the script, `bds` will notice that the output 'out.txt' is still valid with respect to the input 'in.txt' and will not execute any task.
```
# Remove intermediate files
$ rm mid?.txt

# Re-execute (out.txt is still valid because in.txt was not changed)
$ ./test_18.bds
$
```
				
How this works: `bds` calculates the dependency graph and checks whether the `goal` is up to date with respect to the inputs (which are the leaves in the dependency graph).
If the goal up to date, then nothing is done.
This feature is particularly useful when intermediate files are large and we need to clean them up (since we are working with big data problems, this is often the case).


### Dependencies that do not create files 

What if the final step in your pipeline does not create any files?
in this case, you can use `taskId` as a goal, for example:
```
#!/usr/bin/env bds

tid := dep( taskName := 'hi' ) {
    sys echo Hello
}

goal tid	# We use task Id instead of a file name
```
				
				
### Multiple goals 

Sometimes it is convenient to fire multiple goals at once. You can do this by passing a list, instead of a string, to `goal`.
				
```
#!/usr/bin/env bds

string[] outs
for(int i=0; i < 3 ; i++ ) {
    in := "in.$i.txt"
    out := "out.$i.txt"
    outs += out

    sys date > $in
    dep( out <- in ) sys cat $in > $out ; echo Hi $i
}

goal outs	# We use a list of goals, it is interpreted as multiple goal statements (one for each item in the list)
```

### Use-case example for `dep` and `goal`
				
The `goal` statement helps to program complex task scheduling interdependencies.

In a previous example (<a href="bds/test_07.bds">test_07.bds</a>), we had an input file `in.txt`, an intermediate file `inter.txt` and an output file `out.txt`.
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

One problem is that if we delete the intermediate file `inter.txt` (e.g. because we may want to delete big files with intermediate results), then both tasks will be executed

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
So when `bds` evaluates the first `task` expression, the dependency `intermediate <- inFile` is true (because `inter.txt` doesn't exist, so it must be updated with respect to `in.txt`).
After that, when the second `task` expression is evaluated,  `out <- intermediate` is also true, since `inter.txt` is newer than `out.txt`.
As a result, both tasks are re-executed, even though `out.txt` is up to date with respect to `in.txt`.
This can be a problem, particularly if each task requires several hours of execution.


There are two ways to solve this, the obvious one is to add a simple `if` statement surrounding the tasks:
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

As you can see, no task is executed the second time, since `out.txt` is up to date, with respect to `in.txt`. 
The fact that intermediate file `inter.txt` was deleted, is ignored, which is what we wanted.
