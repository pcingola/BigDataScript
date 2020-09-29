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
