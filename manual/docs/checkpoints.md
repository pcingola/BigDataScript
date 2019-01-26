# Checkpoints 
`bds` can save the full state of a running script to a file and restart execution from that point 

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

