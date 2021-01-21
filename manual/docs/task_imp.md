# Improper tasks

In the previous chapter, we talked about `task` ("proper tasks") which consist of `sys` commands (i.e. shell commands).
As we mentioned, these tasks can be executed on different systems: local, cluster, cloud, etc.

It is also possible to execute arbitraty `bds` code within a task, we call these "improper tasks".
For example:
```
a := 42
task(cpus := 1) {
	# This can be any bds code
	println "Hello, a=$a"
}
```

### How it works

In case of an "improper task", `bds` will create a checkpoint containing an absolute serialization of the whole program execution state.
This works similar to a `checkpoint` statement.
Then a task will be scheduled to execute `bds` restoring from this checkpoint.
As a result, the program executing the task will "continue" in the next instruction, i.e. executing the code within the `task` statments.

Note: An `exit` statement is implicitly added in the checkpoint to avoid the task to execute beyond the statements within `task`.
This means that the code saved to the checkpoint file created by the improper task would be:

```
a := 42
task(cpus := 1) {
	# This can be any bds code
	println "Hello, a=$a"	# Note: Variable's "a" value is visible because we inherit the full program state from the checkpoint
	exit                    # Note: This 'exit' statment is added only for the checkpoint to be executed by the task
}
```

### Features

Improper `task` can have all the same feature as "regular" tasks:

- Task dependencies using files
- Task dependencies using task IDs
- Task parameters, such as `cpus`, `mem`, `system`, etc.
- Task names
- Multiple task dependencies, even on different `systems`
- `dep` & `goal`: You can also create improper `dep` and `goal`

