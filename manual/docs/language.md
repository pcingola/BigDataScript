# Language 
Learning BigDataScript language (`bds`) is almost trivial, all the statements and expression and data types do what you expect. 

BigDataScript is really simple and you should be able to code within a few minutes.
This section is intended as a reference, so just glance through it.


### Comments
The usual statements are available
```
// Single line comment

# Another single line comment

/*
   Multi-line comment
*/
```

### Statements
Statements can be terminated either by semicolon or by a new line.
```
# Two statements
print "Hi\n"; print "Bye\n";

# Two statements, same as before but using lines instead of semicolon
print "Hi\n" 
print "Bye\n"
```

### break
Breaks from current loop
```
for( int i=0 ; i < 10 ; i++ ) {
    if( i == 5 ) break;	   // Finish when we reach 5
}
```

### breakpoint
Inserts a debugging breakpoint. I.e. when the statement is executed, `bds` switches execution to debug mode (STEP) 
```
breakpoint "Program execution will switch do debug mode here!\n"
```

### continue
Continue at the end of the current loop
```
for( int i=0 ; i < 10 ; i++ ) {
    if( i == 5 ) continue;	// Skip value 5
}
```

### debug
Show a debug message on STDERR only if `bds` is running in 'debug' mode (otherwise the statement is ignored).
```
debug "Show this message only if we are in debug mode!\n"
```

### error
Show an error message and exit the program
```
if( num <= 0 )	warning "Number MUST be positive\n"
```

### exit
Exit program, optional expression calculates an exit value.
```
exit 1
```

### for
Similar to C or Java `for` loops
```
for( int i=0 ; i < 10 ; i++ ) print("$i\n")
```
					or
```
for( int i=0 ; i < 10 ; i++ ) {
    print("$i\n")
}
```

### for (lists)
Java-like for iterator on lists
```
string[] mylist

// ... some code to populate the list

for( string s : mylist ) print("$s\n")
```

### if / else
It does exactly what you expect
```
if( i < 10 )	print("Less than ten\n")
```
					or
```
if( i < 10 ) {
    print("Less than ten\n")
} else if( i <= 20 ) {
    print("Between ten and twenty\n")
} else {
    print("More than twenty\n")
}
```

### include
Include source code from another file
```
include "mymodule"

// ... use functions from 'mymodule.bds'
```

### kill
Kill a task
```
kill taskId
```

### print / println
Print to sdtout
```
print "Show this mesage without a new line at the end."
println "This one gets a new line at the end."
```

### return
Return from a function. Optional expression is a return value.
```
// Define a function
int twice(int n) {
	return( 2 * n )
}
```

### switch
Switch statements are similar to multiple `if / else if` statements
```
in := 'x'
out := 1

switch( in ) {
    case 'a': 
        out *= 3
        break

    case 'z'+'x':   # Note that the 'case' expressions are evaluated at run time (you can even call functions here)
        out *= 5    # Note that this falls through to "case 'b'"

    case 'b':
        out *= 7
        break

    default:        # You can define 'default' anywhere (no need to do it after 'case')
        out *= 100
}
```

### Variable assignment
` var = expr ` evaluates expression 'expr' and assign result to 'var'
```
i = j + 1
s = "Hello " + world
```

### Variable assignment (multiple)
` ( var1, var2, ..., varN ) = expr ` evaluates expression 'expr' (which must return a list) and assign results to 'var1', 'var2', etc. If the list size is less than the number of variables, variables are assigned default values (e.g. '0' for int). If the list has more values, they are ignored.
```
(name, value) = line.split('\t')
```

### Variable declarations
Declare variable 'var' as type 'type'
```
int i      # 'i' is an 64 bit int variable
real r     # 'r' is a double-precision floating-point number
string s   # 's' is a string
```

`type varName = expr` declares variable 'var' as type 'type', evaluate expression and assign result to initialize 'var'.
```
int i = 42
real r = 3.1415927
string s = "Hello!"
```

` varName := expr ` declares variable 'var', use type inference, evaluate expression 'expr' and assign result to initialize 'var'
```
i := 42
r := 3.1415927
s := "Hello!"
```

### Ternary operator 
` expr ? exprTrue : exprFalse` Evaluate 'expr', if true evaluate and return 'exprTrue', otherwise evalaute and return 'exprFalse'
```
sign = ( i >= 0 ? 1 : -1 )
```

### warning
Show a warning message
```
if( num <= 0 )	warning "Number should be positive\n"
```

### while
Typical `while` iterator
```
while( i < 10 ) i++
```

### Function definition

A simple, and useless, example:
```
// Define a function
int sumPositive(int n) {
    if( n <= 0 )	return 0

    int sum = 0
    for( int i=0 ; i <= n ; i++ ) sum = sum + i
    return sum
}

// Function definition in one line
int twice(int n)    return( 2 * n )

// Main
n := 5
print("The sum is : " + sumPositive( twice(n) ) + "\n" )
```
Obviously, if you run it
```
$ bds z.bds 
The sum is : 55
```

