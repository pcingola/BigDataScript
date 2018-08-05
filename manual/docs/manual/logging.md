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
				

				

			

