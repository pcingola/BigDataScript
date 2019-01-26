# Wait 

Task coordination mechanisms rely on waiting for some tasks to finish before starting new ones.

As we mentioned several times, task execution order is not guaranteed.
File <a href="bds/test_13.bds">test_13.bds</a>
```
#!/usr/bin/env bds

for( int i=0 ; i < 10 ; i++ ) task echo BEFORE $i
for( int i=0 ; i < 10 ; i++ ) task echo AFTER $i
```

```
$ ./test_13.bds
BEFORE 0
BEFORE 4
BEFORE 3
BEFORE 2
BEFORE 1
BEFORE 5
BEFORE 7
BEFORE 6
BEFORE 8
AFTER 1
AFTER 0
BEFORE 9	<-- !!!
AFTER 6
AFTER 5
AFTER 4
AFTER 3
AFTER 2
AFTER 7
AFTER 8
AFTER 9
```
If a task must be executed after another task finishes, we can introduce a `wait` statement.
File <a href="bds/test_13.bds">test_13.bds</a>
```
#!/usr/bin/env bds

for( int i=0 ; i < 10 ; i++ ) task echo BEFORE $i

wait    # Wait until ALL scheduled tasks finish
print("We are done waiting, continue...\n")

for( int i=0 ; i < 10 ; i++ ) task echo AFTER $i

```

Now, we are sure that all tasks 'AFTER' really run after 'BEFORE'
```
$ ./test_14.bds 
BEFORE 0
BEFORE 2
BEFORE 1
BEFORE 4
BEFORE 3
BEFORE 5
BEFORE 6
BEFORE 7
BEFORE 8
BEFORE 9
We are done waiting, continue...
AFTER 0
AFTER 1
AFTER 2
AFTER 3
AFTER 4
AFTER 5
AFTER 6
AFTER 7
AFTER 8
AFTER 9
```
We can also wait for a specific task to finish by providing a task ID `wait taskId`, e.g.:
```
string tid = task echo Hi
wait tid	# Wait only for one task
```

Or you can wait for a list of tasks. 
For instance, in this program, we create a list of two task IDs and `wait` on the list:
```
string[] tids

for( int i=0 ; i < 10 ; i++ ) {
	# Tasks that wait a random amount of time
	int sleepTime = randInt( 5 )
	string tid = task echo BEFORE $i ; sleep $sleepTime ; echo DONE $i

	# We only want to wait for the first two tasks
	if( i < 2 ) tids.add(tid)
}

# Wait for all tasks in the lists (only the first two tasks)
wait tids
print("End of wait\n")
```

When we run it, we get:
```
$ bds z.bds
BEFORE 2
BEFORE 0
BEFORE 7
BEFORE 5
BEFORE 6
BEFORE 4
BEFORE 3
BEFORE 1
DONE 0
DONE 3
DONE 4
DONE 5
DONE 6
DONE 7
BEFORE 8
BEFORE 9
DONE 1
End of wait		<- Wait finished here
DONE 2
DONE 8
DONE 9
```

**Note:** There is an implicit `wait` statement at the end of the program. 
So a program does not exit until all tasks have finished running.

