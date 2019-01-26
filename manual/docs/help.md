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
