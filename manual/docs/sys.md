# Sys 
Local, immediate command execution.  

A few rules about `sys` expression:

* Everything after `sys` until the end of the line is interpreted to be a command
* If the line ends with a backslash, next line is interpreted as part of the same command (same as in a shell script)
* Variables are interpolated. E.g. ` sys echo Hello $person` will replace '$person' by the variable's name before sending it to the OS for execution.
* BDS immediately executes the OS command in the local machine and waits until the command finishes execution.
* If the command exits with an error condition, then `bds` creates a checkpoint, and exits with a non-zero exit code.
* No resource accounting is performed, the command is executed even if all CPUs are busy executing tasks.

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

* Everything after a `sys` keyword until the end of the line is considered part of the command to be executed. So multiple shell commands can be separated by semicolon
```
sys echo Hello ; echo Bye
```

* Multi-line statements are allowed, by using a backslash at the end of the line (same as a shell script)
```
sys echo HeLLo \
	| tr [A-Z] [a-z] \
	| grep hell
```

* `sys` returns the STDOUT of the command: File <a href="bds/test_12.bds">test_12.bds</a>
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

* Characters are passed literally to the interpreting shell. 
For example, when you write '\t' it is NOT converted to a tab character before sending it to the shell (no escaping is required):
```
$ cat z.bds
sys echo -e "Hello\tWorld" | awk '{print $1 "\n" $2}'

$ bds z.bds
$ bds z.bds
Hello
World

```
