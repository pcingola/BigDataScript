
# Task

Proper taks only contains `sys` commands:

```
# Task with inline sys
task sys_cmd_inline

# Task with options, single sys
task( ... ) sys ...

# Task with multiple sys
task( ... ) {
	sys ...
	sys ...
	sys ...
	sys ...
}
```

# Improper tasks

Have non-sys expressions


```
# Improper task: Unconditional
task {
	println "asdfaf"
	func(a, b, c)
}
```

```
# Improper task: Conditional (deepndencies or other boolean expressions)
task([out1, out2, ..., outM] <- [in1, in2,.. inN], boolCond1, ... , boolCondN) {
	println "asdfaf"
	func(a, b, c)
}
```

In this case any of the files in the conditionals `[out1, out2, ..., outM] <- [in1, in2,.. inN]` could be remote.
E.g.
```
in := 's3://mybucket/in.txt'
out := 's3://mybucket/out.txt'
task(out <- in) {
	print "in: $in\tout: $out"	# We expect the "real" paths to be printed here
	sys cat $in > $out			# We expect bds to download the file, perform the 'sys' locally and then upload the results
}
```

The `task` could also reuqire a prelude (from `bds.config`)

```
	scopepush

	# taskOptions commands: Evalueate boolean expressions and
	# leave inputs / outputs dependencies list in the stack

	# TODO: Store the input / output dependencies to a hidden variable name
	# TODO: Create a checkpoint
	# TODO: Replace 'sys' commands by 'shell' (which is a 'sys' that translates inputs/outputs dependencies)

taskLabelIDFalse:
	pushs ''
taskLabelIdEnd:
	scopepop
```

# `shell` vm opcode 

`shell` is an intermediate between 'sys' and 'task' vm opcodes.
It is used to execute 'sys' bds expressions within an improper `task` in bds code.

`shell` shoud:
- collapse all consecutive `sys` commands into one
- create a shell batch file
- add the prelude from config
- translate remote inputs/outputs 
- execute shell file (immediately)
- enforce timeouts
- redirect stdout/stderr


### Cloud task

bds needs to:

1. create an sqs quque (tmp)
1. create an instace (per task)
1. create startup script: `bds -sqs -exec
1. bds deletes the sqs quque (on exit)
1. Terminate all instances (on exit)

### Command line: `bds -sqs -exec`

Sends messages via an SQS (or simmilar) service.
Message types:
1. Start: Process started
1. StdOut / StdErr capture
1. Alive. Every N seconds (default 60), configurable
1. Exit code 

If messages cannot be sent, the process should terminate and terminate the instance


# Solutions



