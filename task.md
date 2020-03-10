
# Task

Pure taks only contains `sys` commands:

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


# Impure tasks


