# Hello world
As we all know, showing that we can print "Hello world" is more important than showing that the language is Turing complete.  
					
Create a simple program and execute it. File <a href="bds/test_01.bds">test_01.bds</a>
```
#!/usr/bin/env bds

print "Hello world\n"
```

```
$ ./test_01.bds 
Hello world
```

### Run a shell command
This time we do it by running a system command (` echo `), using bds' `sys` expression. 
A `sys` executes the command immediately in the local computer and waits until the command finishes.
Everything after `sys` until the end of the line is interpreted as an OS command.
File <a href="bds/test_02.bds">test_02.bds</a>
```
#!/usr/bin/env bds

sys echo Hello world
```

Run it:
```
$ ./test_02.bds 
hello world
```

### Run a shell command on a cluster
now let's run the same in as a 'task'. tasks schedule the system command for execution (either locally, on a cluster, etc.).
File <a href="bds/test_03.bds">test_03.bds</a>
```
#!/usr/bin/env bds

task echo Hello world
```

Just run the script to execute tasks locally
```
$ ./test_03.bds
Hello world
```

You can also execute on a cluster, for instance, if you are on a cluster's head node, just run:
```
$ bds -s cluster ./test_03.bds
Hello world
```
Note that in order to execute on another architecture (cluster), we did not change the bds program, we just added a command line option.

### Cluster, cloud, datacenter or local computer
Programs can be executed on different computer systems of different sizes without changing the code.

<iframe width="640" height="390" src="http://www.youtube.com/embed/WnwBIa4G-mE" frameborder="0" allowfullscreen></iframe>


