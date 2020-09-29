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

###Multiple dependencies
You can have a dependency operator expression `out <- in`, where either `in` and `out` or both can be lists of files. 
The same rules apply: The operator is true if any out file is missing, zero length or the minimum of modification times in <code>out</code> is less than the maximum modificaton times in <code>in</code>

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

###Using `<-` in `tasks`
This construction is so common that we allow for some syntactic sugar. 
```
task( outFile <- inFile ) { 
	sys echo Creating $outFile; cat $inFile > $outFile
}
```
The above syntax means that `task` will only be executed if the dependency is satisfied, i.e. `outFile` needs to be updated respect to `inFile`.

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

