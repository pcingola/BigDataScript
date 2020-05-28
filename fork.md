
# Fork

A fork is a completely sepaarte process with not means of comunication with the parent.
The child process can live in some computer, another computer, another cluser (or HPC), or another cloud/s

Examples
``` 
	pid := fork(system := ..., cpus := ..., mem := ..., ...) 
	pid := fork(taskOpts := ...) 
	
	if( pid ) {
		// Parent process
		// pid = task id?
	} else {
		// Forked child
	}
```

### Implementataion

1. Params or globals define 'task-like' specs for the systems to run a forked process
1. Create a new checklpoint (tmp file)
1. Checkpoint file can be on local file system, shared filesystem, cloud bucket
	1. TODO: How does bds find the bucket & prefix?
		- Config file defines defaults
		- CloudOpts overisdes defaults
		- Error if a reuired option is missing
	1. TODO: How de we define a path for the checkpoint?
		- CloudTask.bucket + CloudTask.forkChpPrefix
1. Dispatch a new pseudo-task, reference chekpoint
	- Local: New process rund command `bds -fork ...`
	- Cluster: qsub ... bds -fork ...
	- Ssh: New remote process ...
	- Aws: 
		- New instance: Terminate on shutdown
		- startup command ('--user-data'): ` bds -fork ... ; shutdown -h now`
	- Google Cloud:
		- Same as AWS
	
### Return value

PID is an empty string for the child process, a non-empty string for the parent.
TODO: What dows the PID mean? Does it even need a meaning?

### Command line: `bds -fork ...`

bds command line option `-fork`
```
	bds -fork path_to_checkpoint
```
In this case, bds knows that it is starting from a checkpoint, after a fork command (this is a child fo a fork).
The checkpoint could be local or remote (e.g. bucket)

TODO: Do we need to write outputs or logs back to "$bucket/$prefix"? No, but this makes everything hard to debug...


