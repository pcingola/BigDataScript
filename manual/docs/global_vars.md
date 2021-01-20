# Predefined global variables 
BigDataScript provides some predefined variables.
                
Whenever you run a BigDataScript program you have several predefined variables:
                     
Variable name                        | Meaning  
-------------------------------------|---------------------------------------------------------------------------------------------------------------------------------
`bool allowEmpty=false`              | Default values for `task`. If true, empty files are allowed in task's outputs. This means that a task producing empty files does not result in program termination and checkpointing.
'string[] args`                      | Arguments used to invoke the program, i.e. all command line options after program name (`bds [options] prog.bds args ...`).   
`bool canFail=false`                 | Default values for `task`. If true, a task is allowed to fail. This means that a failed task execution does not result in program termination and checkpointing.
`int cpus=1`                         | Default values for `task`. Number of CPU (cores) used by the process.
`int cpusLocal`                      | Number of cores in the computer running the script   
`int mem=0`                          | Default values for `task`. Maximum amount of memory in bytes used by the process (0 means no restrictions or use cluster default)
`string node`                        | Default values for `task`. If possible this task should be executed on a particular cluster node. This option is only used for cluster systems and ignored on any other systems.
`string ppwd`                        | Canonical (physical) path to directory where the program is being executed.   
`string programName`                 | The program's name   
`string programPath`                 | The program's path   
`int programPid`                     | The program's process ID (Unix `PID`)   
`string queue`                       | Default values for `task`. Queue name of preferred execution queue (only for cluster systems).
`int retry=0`                        | Default values for `task`. Number of times a task can be re-executed until it's considered failed.
`string system`                      | Default values for `task`. System type to execute tasks (e.g. `local`, `sge`, `slurm`, `aws`, etc.)
`string taskName`                    | Default values for `task`. Assign a task name. This adds a label to the task as well as the taskId returned by `task` expression. Task ID is used to create log files related to the task (shell script, STDOUT, STDERR and exitCode files) so those file names are also changed. This makes it easier to find tasks in the final report and log files (it has no effect other than that). Note: If taskName contains non-allowed characters, they are sanitized (replaced by `_`).
`int timeout=0`                      | Default values for `task`. Time in seconds that a task is allowed to execute (e.g. when running on a cluster). Ignored if zero or less. If process runs more than `timeout` seconds, it is killed. Zero means no limit.
`int walltimeout=0`                  | Default values for `task`. Time in seconds since the task is dispatched to the processing environment. E.g. in busy clusters a task can spend a long time being scheduled (cluster's PENDING state) until the task is run (cluster's RUNNING state), `walltimeout` limits the sum of those times (as oposed to `timeout` that only limits the RUNNIG state time). Zero means no limit.
Environment variables                | All shell environment variables at the moment of invocation (e.g. `HOME`, `PWD`, etc.)  
`int K, M, G, T, P`                  | Kilo, Mega, Giga, Tera, Peta (2^10, 2^20, 2^30, 2^40 and 2^50 respectively)  
`real E, PI`                         | Euler's constant (2.718281...) and Pi (3.1415927...)   
`int minute, hour, day, week`        | Number of seconds in a minute, hour, day and week respectively  
