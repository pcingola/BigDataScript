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
