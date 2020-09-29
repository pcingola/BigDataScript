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

