# Parallel execution `par` 
`bds` can run parallel code as threads in the same program.

Sometimes multiple branches of an analysis pipeline must be run in parallel. 
`bds` provides a simple `par` expression to run code in parallel.
Originally this was called `parallel`, but then I realized I was too lazy to type all those letters, so I reduced it to `par` (both of them work if you choose to be more verbose).

E.g.: File <a href="bds/test_16.bds">test_16.bds</a>
```
#!/usr/bin/env bds

par {
    # This block runs in parallel
    for( int i : range(1, 5) ) {
        print("Parallel $i\n")
        sleep( 0.2 )
    }
}

for( int i : range(1, 5) ) {
    print("Main $i\n")
    sleep( 0.2 )
}
```

If we run this code:
```
$ ./test_16.bds
Parallel 1
Main 1
Parallel 2
Main 2
Main 3
Parallel 3
Main 4
Parallel 4
Parallel 5
Main 5
```

Perhaps a more elegant way to write the same code would be:
```
#!/usr/bin/env bds

void count(string msg) {
    for( int i : range(1, 5) ) {
        print("$msg $i\n")
        sleep( 0.2 )
    }
}

par count('Parallel')   # Call function in parallel thread
count('Main')           # Call function in 'main' thread
```

`par` also works with optional expressions that must be all 'true' to evaluate the block.
	
```
par( out <- in )  {
    # This block runs in parallel if 'out' needs to be updated
    for( int i : range(1, 5) ) {
        tmp := "$in.$i.tmp"
        task head -n $i $in | tail -n 1 > $tmp
    }
    wait
    task cat $in.*.tmp > $out
}
```

				
				
### Wait in 'par' context 
`par` expressions return a 'parallel ID' string that we can use in `wait` 
```
pid := par longRunningFunction()    // This function is executed in parallel 

wait pid                            // Wait for parallel to finish
```

Here `wait` statement waits until the function "longRunningFunction()" finishes.


We mentioned before that, by default, a `wait` statement with no arguments would wait for 'all' tasks to finish.
Specifically, `wait` statement waits for all tasks scheduled by the same thread and for all 'parallels'.
So, `wait` statement with no arguments, will not restore execution until all threads and tasks triggered by the current thread have finished.


### Calling functions  with 'par' 

A function can be called in a parallel thread using `par` statements.

E.g.:
```
par someFunction(x, y)
```
It is important to notice that the return value from a `par` it is a 'parallel ID' (i.e. a thread ID) and not the function's return value.
This is because the parallel thread could take a long time to process and we don't want to stop execution in the current thread until the function finishes.


So, this sample code will show the 'parallel ID' independently of the function's return value:
```
pid := par someFunction(x, y)  # 'par' returns a thread ID
print "Parallel ID: $pid\n"
```
				
**Important:** When calling a function, arguments are evaluated **before** the new thread is created. 
The reason for this is to simplify race conditions.

### Race conditions in 'par' and how to avoid them 
As is the case when creating threads in any programming language, using `par` can lead to race conditions.

As an example, consider this code:

```
#!/usr/bin/env bds

for( int i : range(0, 10) ) {
    par {
        print "Number: $i\n"
    }
}   
```

The output is (comments added for clarification):
```
$ ./z.bds
Number: 0
Number: 2		# We missed number 1?
Number: 3
Number: 4
Number: 6		# We missed number 5?
Number: 6		# Two '6'?
Number: 8
Number: 8
Number: 10		# Three number 10?
Number: 10
Number: 10
```

This is clearly not the result we wanted.

What happened? Well, obviously this had a race condition. 
From the time thread is created (`par`), until the variable `i` is evaluated in `print` statement (parallel thread), the main thread has already changed `i`'s value.


To avoid this type of race condition, when using `par` to call a function, arguments are evaluated in the current thread.
Then a new thread is created and the function is invoked.
See what happens when we refactor the code:
			
```
#!/usr/bin/env bds

void show(int num) {
    print "Number: $num\n"
}

for( int i : range(0, 10) ) {
    par show(i)
}   
```
			
Now the output is what we expect:
```
$ ./z.bds
Number: 0
Number: 1
Number: 2
Number: 3
Number: 4
Number: 5
Number: 6
Number: 7
Number: 8
Number: 9
Number: 10
```

