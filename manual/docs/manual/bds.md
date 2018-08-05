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

			

			<!-- ================================================== -->
			
# Remote files (Amazon S3, Http, etc.) 
				 Often applications need to run tasks on remote data files, `bds` can transparently handle remote data dependencies

				In many cases data files may reside in non-local file systems, such as `HTTP` or Amazon's `S3` object storage.
				Fortunately `bds` can transparently handle remote dependencies, download the input files and upload the results without you having to write a single line of code.
				
				
				**Example 1:** In this example, the remote file `index.html` is remote input file to the `task`.
				Obviously `index.html` is hosted on GitHub's servers, thus not available on the computer where the script is running.
				Before the command (`cat`) is executed, the remote file is transparently downloaded by `bds`.
				
				
```
in  := 'http://pcingola.github.io/BigDataScript/index.html'
out := 'tmp.html'

task( out <- in ) sys cat $in > $out
```
				
				Notice that: 
				<ol>
					<li> there is no code for downloading the remote file (`index.html`) in the script;
					<li> the file is downloaded on the processing node performing the task, which may differ from the node running the script (e.g. if it is running on a cluster);
					<li> task dependencies are verified without downloading data, so the task, as well as the corresponding download / upload operations, are only performed if required;
					<li> if the file is required in the future, `bds` checks if the local (cached) copy is still valid, and uses the cached file if possible (saving bandwith and time).
				</ol>
				
				**Example 2:** The following example is slightly more complicated, the input ('index.html') is processed (`cat` and `echo` commands) and the results are stored in an `Amazon S3` object.
				Once more, notice that `bds` transparently takes care of downloading the file and then uploading the output to Amazon's S3.
				
				
```
in  := 'http://pcingola.github.io/BigDataScript/index.html'
out := 's3://pcingola.bds/test_remote_12.txt'

task( out <- in ) {
	sys cat $in > $out
	sys echo "This line is appended to the file" >> $out
}
```
			

			<!-- ================================================== -->
			
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
			

			<!-- ================================================== -->
			
# Checkpoints 
				 BigDataScript can save the full state of a running script to a file and restart execution from that point 

				A `checkpoint` is the full serialization of the state of a program. 
				This is a powerful tool to create robust pipelines and to recover from several failure conditions.
				
				
				A checkpoint is created either when a task fails or when an explicit `checkpoint` command is executed.
				E.g.: The following program counts from 0 to 9, creating a checkpoint when the counter gets to 5
				 File <a href="bds/test_19.bds">test_19.bds</a>
```
for( int i=0 ; i < 10 ; i++ ) {
	if( i == 5 ) {
		print("Checkpoint\n")
		checkpoint "my.chp"
	}
	print("Counting $i\n")
}
```

				If we execute it, we get
```
$ bds z.bds
Counting 0
Counting 1
Counting 2
Counting 3
Counting 4
Checkpoint
Counting 5
Counting 6
Counting 7
Counting 8
Counting 9
```
				A checkpoint file `my.chp` created.
				We can restart execution from this checkpoint file, by using the `bds -r` command line option
```
$ bds -r my.chp		# Restart execution from checkpoint file
Counting 5
Counting 6
Counting 7
Counting 8
Counting 9
```

				You can also see information on what was happening when the checkpoint was created:
```
$ bds -i my.chp
Program file: './test_19.bds'
     1 |#!/usr/bin/env bds
     2 |
     3 |for( int i=0 ; i < 10 ; i++ ) {
     4 |	if( i == 5 ) {
     5 |		print("Checkpoint\n")
     6 |		checkpoint
     7 |	}
     8 |	print("Counting $i\n")
     9 |}

Stack trace:
test_19.bds, line 3 :	for( int i=0 ; i < 10 ; i++ ) {
test_19.bds, line 4 :		if( i == 5 ) {
test_19.bds, line 6 :			checkpoint

--- Scope: ./test_19.bds:3 ---
int i = 5
...
```
				
				You can even copy the file(s) to another computer and restart execution there, as shown in this video
				
				<iframe width="640" height="390" src="https://www.youtube.com/embed/ah1XxWTYSLM" frameborder="0" allowfullscreen></iframe>
				
			

			<!-- ================================================== -->
			
# Test cases
				 Because nobody writes perfect code. 

				`bds` provides a simple unit testing functionality. 
				Simply use the `-t` command line option and `bds` will run all functions `test*()` (that is functions whose names start with 'test' and have no arguments).
	
				 File <a href="bds/test_24.bds">test_24.bds</a>
```
#!/usr/bin/env bds

int twice(int n)    return 3 * n    // Looks like I don't really know what "twice" means...

void test01() {
    print("Nice test code 01\n")
}

void test02() {
    i := 1
    i++
    if( i != 2 )    error("I can't add")
}

void test03() {
    i := twice( 1 )
    if( i != 2 )    error("This is weird")
}
```

				When we execute the tests, we get
```
$ bds -t ./test_24.bds 

Nice test code 01
00:00:00.002	Test 'test01': OK

00:00:00.003	Test 'test02': OK

00:00:00.004	Error: This is weird
00:00:00.004	Test 'test03': FAIL

00:00:00.005	Totals
                  OK    : 2
                  ERROR : 1

```
			

			<!-- ================================================== -->
			
#  Debugger (built in) 
				 Bds provides a simple yet powerful built in debugger using `breakpoint` and `debug` statements.

				`bds` provides a simple built in debugger that can be activated using `breakpoint` statement.
				When a `breakpoint` statement is found, `bds` switches to debug mode and prompts the user on the console.
	
				 File <a href="bds/test_24.bds">test_25.bds</a>
```
#!/usr/bin/env bds

int inc(int x) {
    return x + 1;
}

debug "This won't be printed because we are not (yet) in debug mode\n" 
breakpoint "Activate debug mode and insert a breakpoint here!\n"

for( int i=0 ; i < 3 ; i = inc(i) ) {
    print "hi $i\n"
    debug "Variable: $i\n"  # This will be printed
}
```

				When we run this example, the program runs until the first `breakpoint` and then `bds` prompts for debug commands on the console:
```
$ bds test_25.bds 
Breakpoint test_25.bds, line 8: Activate debug mode and insert a breakpoint here!
DEBUG [STEP]: test_25.bds, line 10: 
	for( int i = 0 ; i < 3 ; i = inc( i ) ) {
		print "hi $i\\n"
		debug "Variable: $i\\n"
	}
>
```

			You can type 'h' for help in debug commands:
```
> h
Help:
	[RETURN]  : step
	f         : show current Frame (variables within current scope)
	h         : Help
	o         : step Over
	p         : show Program counter
	r         : Run program (until next breakpoint)
	s         : Step
	t         : show stack Trace
	v varname : show Variable 'varname'
```

			Here is an example of a debug session (comments after '#' added for clarity):
```
$ bds test_25.bds 
Breakpoint test_25.bds, line 8: Activate debug mode and insert a breakpoint here!
DEBUG [STEP]: test_25.bds, line 10: 
    for( int i = 0 ; i < 3 ; i = inc( i ) ) {
        print "hi $i\\n"
        debug "Variable: $i\\n"
    }
> 
DEBUG [STEP]: test_25.bds, line 10: int i = 0 >                      # Pressing Return runs the next step ('int i=0')
DEBUG [STEP]: test_25.bds, line 10: i = 0 > v i                      # Show variable 'i'
int : 0
DEBUG [STEP]: test_25.bds, line 10: i < 3 > 
DEBUG [STEP]: test_25.bds, line 11: print "hi $i\\n" > 
hi 0                                                                 # Output to STDOUT from print statement
DEBUG [STEP]: test_25.bds, line 12: debug "Variable: $i\\n" > 
Debug test_25.bds, line 12: Variable: 0                              # Since we are in debug mode, 'debug' prints to SDTERR
DEBUG [STEP]: test_25.bds, line 10: i = inc( i ) > 
DEBUG [STEP]: test_25.bds, line 10: inc( i ) >                       # Step into function 'inc(i)'
DEBUG [STEP]: test_25.bds, line 4: return x + 1 > t                  # Show stack trace
test_25.bds, line 10 :    for( int i=0 ; i < 3 ; i = inc(i) ) {
test_25.bds, line 3 :    int inc(int x) {
test_25.bds, line 4 :        return x + 1;

DEBUG [STEP]: test_25.bds, line 4: return x + 1 > f                  # Show frames (variables)

---------- Scope Global ----------
string _ = "/Users/pcingola/.bds/bds"
...                                                                  # Edited for brevity
int walltimeout = 86400
int week = 604800

---------- Scope test_25.bds:10:ForLoop ----------                   
int i = 0

---------- Scope test_25.bds:3:FunctionDeclaration ----------
int x = 0

DEBUG [STEP]: test_25.bds, line 4: return x + 1 >                    # Step, exeute 'return' statement
DEBUG [STEP]: test_25.bds, line 10: i < 3 > 
DEBUG [STEP]: test_25.bds, line 11: print "hi $i\\n" > 
hi 1
DEBUG [STEP]: test_25.bds, line 12: debug "Variable: $i\\n" > 
Debug test_25.bds, line 12: Variable: 1
DEBUG [STEP]: test_25.bds, line 10: i = inc( i ) > o
DEBUG [STEP_OVER]: test_25.bds, line 10: inc( i ) >                  # Step Over: execute 'inc(i)' and stop after function returns
DEBUG [STEP_OVER]: test_25.bds, line 10: i < 3 > 
DEBUG [STEP_OVER]: test_25.bds, line 11: print "hi $i\\n" > r        # Run (until another breakpoint). Since there are no more breakpoints, runs until the end of the program
hi 2
Debug test_25.bds, line 12: Variable: 2
```
			
		
			<!-- ================================================== -->
			
# Automatic command line parsing 
				 No need to manually parse command line options for your scripts, `bds` does it for you.

				Automatic command line parsing parses any command line argument that starts with "-" and assigns the value to the corresponding variable.
				 File <a href="bds/test_20.bds">test_20.bds</a>
```
#!/usr/bin/env bds

in := "in.txt"
print("In file is '$in'\n")
```

				If we run this, we get
```
$ ./test_20.bds 
In file is 'in.txt'
```

				Now we pass a command line argument `-in another_file.txt`, and `bds` automatically parses that command line option replacing the value of variable 'in'
```
$ ./test_20.bds -in another_file.txt
In file is 'another_file.txt'
```

				This feature also works for other data types (int, real, bool). 
				In case of bool if the option is present, the variable is set to 'true'.
				 File <a href="bds/test_21.bds">test_21.bds</a>
```
#!/usr/bin/env bds

bool flag
print("Variable flag is $flag\n")
```

```
$ ./test_21.bds
Variable flag is false
```

```
$ ./test_21.bds -flag
Variable flag is true
```

				Or you can specify the value (`true` or `false` which is useful to set to `false` a bool that is by default `true`:
				 File <a href="bds/test_21b.bds">test_21b.bds</a>
```
#!/usr/bin/env bds

flagOn  := true
flagOff := false
print("flagOn = $flagOn\nflagOff = $flagOff\n")
```

				So in this example we can reverse the defaults by running this (note that we can use `-flagOff` instead of `-flagOff true `):
```
$ ./test_21b.bds -flagOn false -flagOff true 
flagOn = false
flagOff = true
```
				Note that we can use `-flagOff` instead of `-flagOff true` (the outcome is the same).

				
				
				You can also apply this to a list of strings.
				In this case, all command line arguments following the `-listName` will be included in the list (up to the next argument starting with '-').
				
				E.g.: Note that list `in` is populated using 'in1.txt in2.txt in3.txt' and `out` is set to 'zzz.txt'
				 File <a href="bds/test_22.bds">test_22.bds</a>
```
#!/usr/bin/env bds

in  := ["in.txt"]
out := "out.txt"
ok	:= false

print("In : $in\n")
print("Out: $out\n")
print("OK : $ok\n")
```

```
$ ./test_22.bds  -ok -in in1.txt in2.txt in3.txt -out zzz.txt
In : [in1.txt, in2.txt, in3.txt]
Out: zzz.txt
OK : true
```

				<iframe width="640" height="390" src="http://www.youtube.com/embed/oSjhkRuc0I8" frameborder="0" allowfullscreen></iframe>
			

			<!-- ================================================== -->
			
# Automatic command line help 
				 A command line 'help' for your scripts can be created automatically by `bds`. 

				When you create variables that are used in command line arguments, you can provide an optional `help` string that `bds` will show when the script is run using either: `-h`, `-help` or `--help` command line options.
				
				
				For example, if we have the following script:
				File <a href="bds/test_26.bds">test_26.bds</a>
```
#!/usr/bin/env bds

int num = 3		help Number of times 'hi' should be printed
int min			help Help for argument 'min' should be printed here
mean := 5		help Help for argument 'mean' should be printed here
someVeryLongCommandLineArgumentName := true    help This command line argument has a really long name

for( int i=0 ; i < num ; i++ ) {
	print "hi $i\n"
}
```

		When running the script using `-h` command line option a help screen is created and printed out automatically (no action is programmed in the script to process the '-h' command line option).
		Note that script command line options are given AFTER script name:
```
$ bds test_26.bds -h
Command line options 'test_26.bds' :
	-num <int>                                : Number of times 'hi' should be printed
	-min <int>                                : Help for argument 'min' should be printed here
	-mean <int>                               : Help for argument 'mean' should be printed here
	-someVeryLongCommandLineArgumentName      : This command line argument has a really long name
```

		
		The same happens if you run the script directly:
```
$ ./test_26.bds -h
Command line options 'test_26.bds' :
	num                                 : Number of times 'hi' should be printed
	min                                 : Help for argument 'min' should be printed here
	mean                                : Help for argument 'mean' should be printed here
	someVeryLongCommandLineArgumentName : This command line argument has a really long name
```
				
				### Help sort order 
				By default, variables are sorted alphabetically when help is shown. 
				This can be overridden by creating a global variable `helpUnsorted` (regardless of its type and value, since the program may not even be running when the help is shown).
				
				File <a href="bds/test_26b.bds">test_26b.bds</a>
```
#!/usr/bin/env bds

# This variable is use to indicate that help should be shown unsorted 
# (i.e. in the same order that variables are declared)
helpUnsorted := true

zzz := 1        help Help for argument 'zzz' should be printed here
aaa := 1        help Help for argument 'aaa' should be printed here

print "Done\n"
```

				Now when we run `bds -h` help lines are shown unsorted:
```
$ ./test_26b.bds -h
Command line options 'test_26b.bds' :
	-zzz <int>  : Help for argument 'zzz' should be printed here
	-aaa <int>  : Help for argument 'aaa' should be printed here
```
				
				### Showing help on empty command line arguments 
				The function `printHelp()` can be called to show the help message. 
				This can be used, for instance, to show a help message when there are no command line arguments by doing something like this:
				
				File <a href="bds/test_26c.bds">test_26c.bds</a>
```
#!/usr/bin/env bds

zzz := 1        help Help for argument 'zzz' should be printed here
aaa := 1        help Help for argument 'aaa' should be printed here
bbb := 1        help Help for argument 'bbb' should be printed here

if( args.isEmpty() ) {
    printHelp()
    exit(1)
}

print "Done\n"
```

				Now when we run `test_26c.bds` without any command line arguments, the help message is shown:
```
$ ./test_26c.bds 
Command line options 'test_26c.bds' :
	-aaa <int>  : Help for argument 'aaa' should be printed here
	-bbb <int>  : Help for argument 'bbb' should be printed here
	-zzz <int>  : Help for argument 'zzz' should be printed here
```
				
				### Help sections 

				Sometimes it is useful to divide the help message into sections.
				Sections are marked by `help` statements as in this example:
				
				File <a href="bds/test_26d.bds">test_26d.bds</a>
```
#!/usr/bin/env bds

help This program does blah
help Actually, a lot of blah blah
help     and even more blah
help     or blah

verbose := false    help Be verbose
quiet   := false    help Be very quiet

help Options related to database
dbPort := 5432      help Database port
dbName := "testDb"  help Database name

print "OK\n"
```
				
				When run, variables are grouped in two "help sections" (note that variables are sorted within each section):
```
$ ./test_26d.bds -h
This program does blah
Actually, a lot of blah blah
    and even more blah
    or blah
	-quiet <bool>     : Be very quiet
	-verbose <bool>   : Be verbose
Options related to database
	-dbName <string>  : Database name
	-dbPort <int>     : Database port
```
			

			<!-- ================================================== -->
			
# Logging 
				 Logging is mundane and boring, but many times necessary. Not many people enjoy adding hundreds of line of code just to perform logging. That's why `bds` can log everything for you.

				Both `sys` and `task` commands create a shell file, execute it and save STDOUT and STDERR to files.
				This gives you an automatic log of everything that was executed, as well as the details of the outputs and exit status from each execution.
				
				For example, let's create a simple program and run it
```
string name = "Pablo"

sys echo Hello $name
```

					Now let's run this script, we use `-v` command line option to make the output verbose:
```
$ bds -v -log z.bds
00:00:00.169	Process ID: z.bds.20140328_224825_685
00:00:00.174	Queuing task 'z.bds.20140328_224825_685/sys.line_4.id_1'
00:00:00.674	Running task 'z.bds.20140328_224825_685/sys.line_4.id_1'
00:00:00.689	Finished task 'z.bds.20140328_224825_685/sys.line_4.id_1'
Hello Pablo
00:00:00.692	Finished running. Exit value : 0
```
				What happened?
				<ol>
					<li> bds parses the `sys` statement and interpolates "echo Hello $name" to "echo Hello Pablo"

					<li> Creates a task and assigns a task ID `z.bds.20140328_224825_685/sys.line_4.id_1`

					<li> It creates a shell script file `z.bds.20140328_224825_685/sys.line_4.id_1.sh` with the code:
```
$ cat z.bds.20140328_224825_685/sys.line_4.id_1.sh 
#!/bin/sh

echo Hello Pablo
```

						<li> Then executes this shell script and saves stdout and stderr to z.bds.20140328_224825_685/sys.line_4.id_1.stdout and z.bds.20140328_224825_685/sys.line_4.id_1.stderr respectively
```
$ cat z.bds.20140328_224825_685/sys.line_4.id_1.stdout
Hello Pablo
$ cat z.bds.20140328_224825_685/sys.line_4.id_1.stderr
```
					Notice that there was no output on stderr (it's empty)

					<li> Command finished without problems, so it continues with the rest of the program
				
				</ol>
				So at the end of the run, we have the file with the script, plus the stdout and stderr files.
				All the information is logged automatically.
				

				

			

			<!-- ================================================== -->
			
# Cleanup 
				 If a script fails, `bds` automatically cleans up stale files and kills pending tasks. 

				In order to make sure that data pipelines are correctly re-executed after a failure, `bds` automatically cleans all dependent files from failed tasks.
				This saves time because the user doesn't need to check for consistency on putative stale files.
				
				
				Also, `bds` ensures that resources are not wasted, by killing all pending tasks.
				In large pipelines, thousands of tasks can be scheduled for execution in a cluster and it is quite difficult for the user to keep track of them and clean them if the pipeline fails.
				Fortunately, `bds` takes care of all these details and issues appropriate commands to kill all pending tasks.

				 File <a href="bds/test_22.bds">test_22.bds</a>
```
#!/usr/bin/env bds

for(int i : range(1,10) ) {
    in  := "in_$i.txt"
    sys date > $in

    out := "out_$i.txt"
    task( out <- in ) {
        sys echo Task $i | tee $out; sleep $i; echo Done $i
    }
}
```

				If I run this example and interrupt it (by pressing Ctrl-C) before it ends:
```
$ ./test_23.bds
Task 4
Task 5
Task 2
Task 3
Task 8
Task 1
Task 7
Task 6
Done 1
Task 9
Done 2
Task 10
Done 3
^C				# Here I pressed Ctr-C
2014/07/31 00:43:46 bds: Received OS signal 'interrupt'
2014/07/31 00:43:46 bds: Killing PID '32523'
2014/07/31 00:43:46 bds: Killing PID '32534'
2014/07/31 00:43:46 bds: Deleting file 'out_6.txt'
2014/07/31 00:43:46 bds: Deleting file 'out_9.txt'
2014/07/31 00:43:46 bds: Deleting file 'out_5.txt'
2014/07/31 00:43:46 bds: Killing PID '32546'
2014/07/31 00:43:46 bds: Deleting file 'out_4.txt'
2014/07/31 00:43:46 bds: Deleting file 'out_8.txt'
2014/07/31 00:43:46 bds: Killing PID '32597'
2014/07/31 00:43:46 bds: Killing PID '32557'
2014/07/31 00:43:46 bds: Deleting file 'out_7.txt'
2014/07/31 00:43:46 bds: Killing PID '32575'
2014/07/31 00:43:46 bds: Killing PID '32610'
2014/07/31 00:43:46 bds: Deleting file 'out_10.txt'
```
				You can see how `bds` cleans up all stale files and kills all processes.
				When this is executed in a cluster, the appropriate `qdel`, `canceljob` or similar command is issued (depending on the type of cluster used).
	
### Disappearing tasks
				 Sometimes tasks just disappear from clusters. 

				Sometimes clusters fail in ways that the cluster management system is unable to detect, let alone report the error.
				It can happen that tasks disappear without any trace from the cluster (this is not as rare as you may think, particularly when executing thousands of tasks per pipeline).
				For this reason, `bds` performs active monitoring, to ensure that tasks are still alive.
				If any task "mysteriously disappears", `bds` reports the problem and considers the task as failed.
			

			<!-- ================================================== -->
			
# BDS Command line options 
				 BigDataScript (`bds`) command line arguments. 
					
				Running the bds command without any arguments shows a help message
```
$ bds
BigDataScript 0.999i (build 2015-03-28), by Pablo Cingolani

Usage: BigDataScript [options] file.bds

Available options: 
  [-c | -config ] bds.config     : Config file. Default : /Users/pcingola/.bds/bds.config
  [-checkPidRegex]               : Check configuration's 'pidRegex' by matching stdin.
  [-d | -debug  ]                : Debug mode.
  -dryRun                        : Do not run any task, just show what would be run. Default: false
  [-extractSource]               : Extract source code files from checkpoint (only valid combined with '-info').
  [-i | -info   ] checkpoint.chp : Show state information in checkpoint file.
  [-l | -log    ]                : Log all tasks (do not delete tmp files). Default: false
  -noReport                      : Do not create any report.
  -noReportHtml                  : Do not create HTML report.
  -noRmOnExit                    : Do not remove files marked for deletion on exit (rmOnExit). Default: false
  [-q | -queue  ] queueName      : Set default queue name.
  -quiet                         : Do not show any messages or tasks outputs on STDOUT. Default: false
  -reportHtml                    : Create HTML report. Default: true
  -reportYaml                    : Create YAML report. Default: false
  [-r | -restore] checkpoint.chp : Restore state from checkpoint file.
  [-s | -system ] type           : Set system type.
  [-t | -test   ]                : Run user test cases (runs all test* functions).
  [-v | -verbose]                : Be verbose.
  -version                       : Show version and exit.
  [-y | -retry  ] num            : Number of times to retry a failing tasks.
  -pid <file>                    : Write local processes PIDs to 'file'
```
				<table class="table table-striped">
					    Option short    Option long     Meaning    
					    -c     -config     Path to bds.config file (most of the times no config file is needed)    
					           -checkPidRegex     Check configuration's 'pidRegex' by matching stdin.    
					    -d     -debug     Debug mode, shows detailed information for debugging scripts or debugging bds itself.    
					           -dryRun      Do not run any task, just show what would be run. This mode is used when you just want to test your script's logic without executing any tasks.    
					           -extractSource     Extract source code files from checkpoint (only valid combined with '-info').   
					    -i     -info     Show state information in checkpoint file. Prints variables, scopes, etc.   
					    -l     -log     Log all tasks (do not delete tmp files).   
					           -noReport   Do not create any report.   
					           -noReportHtml   Do not create HTML report.   
					           -noRmOnExit     Do not remove files marked for deletion on exit (rmOnExit).    
					    -q     -queue     Set default cluster's queue name.    
					           -quiet     Do not show any messages or tasks outputs on STDOUT. This means that only the output from `print` (and other print-like bds statements), statements will be shown on the console    
					           -reportHtml     Create HTML report. Create an HTML report (only if at least one task is executed). By default this option is activated. 
					           -reportYaml     Create YAML report. Create a YAML report (only if at least one task is executed).  
					    -r     -restore     Restore from a a checkpoint and continue execution.   
					    -s     -system     Define the 'system' type the script is running on (e.g. cluster type)    
					    -t     -test     Perform all tests in a script (i.e. run all functions that are called "test*")    
					    -v     -verbose     Be verbose (show more information)    
					           -version     Show version number and exit.   
					    -y     -retry     Number of times to retry a failing tasks.    
					           -pid     Write local processes PIDs to 'file'. Under normal circumstances, you should never use this command line option (unless you are debugging bds itself).    
				</table>
			

			<!-- ================================================== -->
# BDS Config file 
				 BigDataScript's config file allows customizing `bds`'s behavior.

				 
				BigDataScript's config file is usually located in `$HOME/.bds/bds.config`.
				Running `bds` without any arguments shows the config's file default location.
				You can provide an alternative path using command line option `-c`.
				
				
				The config file is roughly divided into sections.
				It is not required that parameters are in specific sections, we just do it to have some order.
				We explain the parameters for each section below.
				
				
				###Default parameters
				This section defines default parameters used in running tasks (such as system type, number of CPUs, memory, etc.).
				Most of the time you'd rather keep options unspecified but it can be convenient to set `system = local` in 
				your laptop and `system = cluster` in your production cluster.

				<table class="table table-striped">
					 
						 Parameter 
						  Comments / examples  
					 
					 
						  mem  
						  Default memory in bytes (negative number means unspecified)  
					 
					 
						  node   
						  Default execution node (empty means unspecified)   
					 
					 
						  queue   
						  Add default queue name (empty means unspecified)   
					 
					 
						  retry   
						  Default number of retries when a task fails (0 means no retry). 
							 Upon failire, a task is re-executed up to 'retry' times. 
							 I.e. a task is considered failed only after failing 'retry + 1' times.
						 
					 
					 
						  system  
						  Default system type. If unspecified, the default system is 'local' (run tasks on local computer)  
					 
					 
						  timeout   
						  Task timeout in seconds (default is one day)   
					 
					 
						  walltimeout   
						  Task's wall-timeout in seconds (default is one day). 
							 Wall timeout includes all the time that the task is waiting to be executed. 
							 I.e. the total amount of time we are willing to wait for a task to finish.
							 For example if walltimeout is one day and a task is queued by the cluster 
							 system for one day (and never executed), it will timeout, even if the task 
							 was never run.
						 
					 
					 
						  taskShell  
						  Shell to be used when running a `task` (default '/bin/sh -e')
							 **WARNING**: Make sure you use "-e" or some command line option that stops execution when an error if found.
						 
					 
					 
						  sysShell   
						  Shell to be used when running a `sys` (default '/bin/sh -e -c')
							 **WARNING**: Make sure you use "-e" or some command line option that stops execution when an error if found.
							 **WARNING**: Make sure you use "-c" or some command line option that allows providing a script
						 
					 
				</table>

				
				###Cluster options 
				This section defines parameters to customize `bds` to run tasks on your cluster.

				<table class="table table-striped">
					 
						 Parameter 
						  Comments / examples  
					 
					 
						  pidRegex  
						  
							Regex used to extract PID from cluster command (e.g. qsub). 
							
							
							When `bds` dispatches a task to the cluster management system (e.g. running 'qsub' command), it expects the cluster system to inform the jobID.
							Typically cluster systems show jobIDs in the first output line.
							This regex is used to match that jobID.
							
							
							Default, use the whole line
							Note: Some clusters add the domain name to the ID and 
      							then never use it again, some other clusters add 
      							a message (e.g. 'You job ...')
							
							
							Examples:
```
pidRegex = "(.+).domain.com"
pidRegex = "Your job (\\S+)"
```

						 
					 
					 
						  clusterRunAdditionalArgs  
						 
							These command line arguments are added to every cluster 'run' command (e.g. 'qsub')
							The string is split into spaces (regex: '\s+') and added to the cluster's run command.
							
							For instance the following configuration:
							
							``` clusterRunAdditionalArgs = -A accountID -M user@gmail.com ```
							will cause four additional arguments `{ '-A', 'accountID', '-M', 'user@gmail.com' }` to 
							be added immediately after 'qsub' (or similar) command used to run tasks on a cluster.
						 
					 
					 
						 clusterKillAdditionalArgs 
						 
						These command line arguments are added to every cluster 'kill' command (e.g. 'qdel')
						Same rules as 'clusterRunAdditionalArgs' apply
						 
					 
					 
						 clusterStatAdditionalArgs 
						 
						These command line arguments are added to every cluster 'stat' command (e.g. 'qstat')
						Same rules as 'clusterRunAdditionalArgs' apply
						 
					 
					 
						 clusterPostMortemInfoAdditionalArgs 
						 
						These command line arguments are added to every cluster 'post mortem info' command (e.g. 'qstat -f')
						Same rules as 'clusterRunAdditionalArgs' apply
						 
					 
				</table>

				
				###SGE Cluster options 
				This section defines parameters to customize `bds` to run tasks on a Sun Grid Engine cluster.
				
				

				**IMPORTANT:**	In SGE clusters it is important to enable `ENABLE_ADDGRP_KILL=true` to the `execd_params` parameter of `qconf -sconf`.
									Otherwise SGE might not be able to kill `bds` subprocesses running on slave nodes if this option is not enabled.
									So, if you don't activate `ENABLE_ADDGRP_KILL=true` killing processes may not work in SGE clusters, nodes will continue to run tasks even after they've been killed either Ctrl-C to bds or by a direct `qdel` command (the cluster reports them as finished, but they might still be running in the slave node).
				
				

				<table class="table table-striped">
					 
						 Parameter 
						  Comments / examples  
					 
					 
						  sge.pe  
						 
						Parallel environment in SGE (e.g. 'qsub -pe mpi 4')
						
						Note on SGE's parallel environment ('-pe'):
						
						  The defaults were set to be compatible with **StarCluster**.
						  Parallel environment defines how 'slots' (number of cpus requested) 
						  are allocated. **StarCluster** by default sets up a parallel environment, called “orte”, 
						  that has been configured for OpenMPI integration within SGE and has a number of slots 
						  equal to the total number of processors in the cluster.
						  See details `qconf -sp orte`:
```
pe_name            orte
slots              16
user_lists         NONE
xuser_lists        NONE
start_proc_args    /bin/true
stop_proc_args     /bin/true
allocation_rule    $round_robin
control_slaves     TRUE
job_is_first_task  FALSE
urgency_slots      min
accounting_summary FALSE
```
						
						  Notice the `allocation_rule = $round_robin`.  This defines how to assign slots to a job. By 
						  default StarCluster configures round_robin allocation. This means that if a job requests 8 
						  slots for example, it will go to the first machine, grab a single slot if available, move to 
						  the next machine and grab a single slot if available, and so on wrapping around the cluster 
						  again if necessary to allocate 8 slots to the job.
						
						  You can also configure the parallel environment to try and localize slots as much as 
						  possible using the "fill_up" allocation rule and job_is_first_task of TRUE.
						
						  To configure: qconf -mp orte
 
					 
					 
						  sge.mem  
						  Parameter for requesting amount of memory in qsub (e.g. `qsub -l mem 4G`)  
					 
					 
						  sge.timeout  
						  Parameter for timeout in qsub (e.g. `qsub -l h_rt 24:00:00`)  
					 
				</table>
				

				###Generic Cluster options 
				Cluster **generic** invokes user defined scripts for manupulating tasks.
				This allows the user to customize scripts for particular cluster environments (e.g. environments not currently supported by `bds`)
				
				
				**Note**: You should either provide the script's full path or the scripts should 
					  be in your PATH
				
				
				**Note**: These scripts "communicate" with bds by printing information on STDOUT. The 
					  information has to be printed in a very specific format. Failing to adhere 
					  to the format will cause bds to fail in unexpected ways.
				
				
				**Note**: You can use command path starting with '~' to indicate `$HOME` dir or '.' to 
					  indicate path relative to config file's dir
				
				
				<table class="table table-striped">
					 
						 Parameter 
						  Comments / examples  
					 
					 
						  clusterGenericRun  
						 
							The specified script is executed when a task is submitted to the cluster
							
							
							**Script's expected output**:
								The script MUST print the cluster's jobID AS THE FIRST LINE. 
								Make sure to flush STDOUT to avoid other lines to be printed out of order.
							
							
							**Command line arguments**:
								<ol>
									<li> Task's timeout in seconds. Negative number means 'unlimited' (i.e. let the cluster system decide)
									<li> Task's required CPUs: number of cores within the same node.
									<li> Task's required memory in bytes. Negative means 'unspecified' (i.e. let the cluster system decide)
									<li> Cluster's queue name. Empty means "use cluster's default"
									<li> Cluster's STDOUT redirect file. This is where the cluster should redirect STDOUT.
									<li> Cluster's STDERR redirect file. This is where the cluster should redirect STDERR
									<li> Cluster command and arguments to be executed (typically is a "bds -exec ...").
								</ol>
							
							
							**Example:** For examples on how to build this script, take a look at `config/clusterGeneric*` directory in the source code.
						 
					 
					 
						  clusterGenericKill   
						 
							The specified script is executed in order to kill a task
							
							
							**Script's expected output**:
								None
							
							
							**Command line arguments**:
								jobId: This is the jobId returned as the first line in 'clusterGenericRun' 
									  script (i.e. the jobID provided by the cluster management system)
							
							
							**Example:** For examples on how to build this script, take a look at `config/clusterGeneric*` directory in the source code.
						 
					 
					 
						  clusterGenericStat  
						 
							The specified script is executed in order to show the jobID of all jobs currently scheduled in the cluster
							
							
							**Script's expected output**:
								This script is expected to print all jobs currently scheduled or 
								running in the cluster (e.g. qstat), one per line. The FIRST column 
								should be the jobID (columns are space or tab separated). Other 
								columns may exist (but are currently ignored).
							
							
							**Command line arguments**:
								None
							
							
							**Example:** For examples on how to build this script, take a look at `config/clusterGeneric*` directory in the source code.
						 
					 
					 
						  clusterGenericPostMortemInfo   
						 
							The specified script is executed in order to get information of a recently 
							finished jobId. This information is typically used for debuging and is added to bds's output.
							
							
							**Script's expected output**:
								The output is not parsed, it is stored and later shown 
								in bds's report. Is should contain information relevant 
								to the job's execution (e.g. `qstat -f $jobId` or `checkjob -v $jobId`)
							
							
							**Command line arguments**:
								jobId: This is the jobId returned as the first line in 'clusterGenericRun' 
								script (i.e. the jobID provided by the cluster management system)
							
							
							**Example:** For examples on how to build this script, take a look at `config/clusterGeneric*` directory in the source code.
						 
					 
				</table>

				###SSH Cluster options 
				Cluster **shh** creates a virtual cluster using several nodes access via shh.
				
				
				
				<table class="table table-striped">
					 
						 Parameter 
						  Comments / examples  
					 
					 
						  ssh.nodes  
						  
							This defines the userName and nodes to be accessed via ssh.
							
							
							Examples:
							
							<ul>
								<li> A trivial 'ssh' cluster composed only of the localhost accesed via ssh (useful for debugging)
```
ssh.nodes = user@localhost
```
								<li> Some company's servers used as an ssh cluster
```
ssh.nodes = user@lab1-1company.com, user@lab1-2company.com, user@lab1-3company.com, user@lab1-4company.com, user@lab1-5company.com
```
								<li> A StarCluster run on Amazon AWS
```
ssh.nodes = sgeadmin@node001, sgeadmin@node002, sgeadmin@node003, sgeadmin@node004, sgeadmin@node005, sgeadmin@node006
```
							
						 
				</table>
			
		

		<!-- Docs nav
		================================================== -->
		<div class="col-md-3">
			<div class="bs-docs-sidebar hidden-print hidden-xs hidden-sm" role="complementary" data-spy="affix">
				<ul class="nav bs-docs-sidenav" data-spy="affix">
					<li><a href="#install"> Download &amp; Install</a></li>
					<li><a href="#intro"> Introduction</a></li>
					<li><a href="#hello"> Hello world </a></li>
					<li><a href="#language"> Language </a>
						<ul class="nav">
							<li><a href="#language-special"> Special pourpose </a></li>
							<li><a href="#language-type"> Data types </a></li>
							<li><a href="#language-string"> String </a></li>
							<li><a href="#language-array"> Array </a></li>
							<li><a href="#language-map"> Map </a></li>
							<li><a href="#vars"> Predefined variables </a></li>
						
					</li>

					<li><a href="#task"> Creating data pipelines </a>
						<ul class="nav">
							<li><a href="#task-depend"> Task dependencies</a></li>
							<li><a href="#task-wait"> Wait </a></li>
							<li><a href="#task-depop"> Dependency operator </a></li>
							<li><a href="#task-autodep"> Automatic dependency </a></li>
							<li><a href="#task-goal"> 'dep' and 'goal' </a></li>
						
					</li>

					<li><a href="#sys"> Sys </a></li>
					<li><a href="#taskExpr"> Task </a></li>
					<li><a href="#waitExpr"> Wait </a></li>
					<li><a href="#dep"> Dependency operator </a></li>
					<li><a href="#goal"> Goals </a></li>
					<li><a href="#remote"> Remote files </a></li>
					<li><a href="#par"> Parallel execution </a></li>
					<li><a href="#check"> Checkpoints </a></li>

					<li><a href="#test"> Test cases </a></li>
					<li><a href="#debugger"> Debugger (built in) </a></li>
					<li><a href="#cmdLineParse"> Command line parsing </a></li>
					<li><a href="#autoHelp"> Automatic help</a></li>
					<li><a href="#autoLog"> Logging </a></li>
					<li><a href="#cleanup"> Cleanup </a></li>

					<li><a href="#commandLine"> BDS command line </a></li>
					<li><a href="#config"> BDS config file </a></li>
