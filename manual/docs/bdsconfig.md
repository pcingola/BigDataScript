# BDS Config file 

The `config.bds` file allows customizing `bds`'s behavior.

The config file is usually located in `$HOME/.bds/bds.config`.
Running `bds` without any arguments shows the config's file default location.
You can provide an alternative path using command line option `-c`.

The config file is roughly divided into sections.
It is not required that parameters are in specific sections, we just do it to have some order.
We explain the parameters for each section below.


###Default parameters
This section defines default parameters used in running tasks (such as system type, number of CPUs, memory, etc.).
Most of the time you'd rather keep options unspecified but it can be convenient to set `system = local` in 
your laptop and `system = cluster` in your production cluster.

Parameter   | Comments / examples  
------------|-----------------------------------------------------------------
mem         | Default memory in bytes (negative number means unspecified)  
node        | Default execution node (empty means unspecified)   
queue       | Add default queue name (empty means unspecified)   
retry       | Default number of retries when a task fails (0 means no retry). Upon failire, a task is re-executed up to 'retry' times. I.e. a task is considered failed only after failing 'retry + 1' times.
system      | Default system type. If unspecified, the default system is 'local' (run tasks on local computer)  
timeout     | Task timeout in seconds (default is one day)   
walltimeout | Task's wall-timeout in seconds (default is one day). Wall timeout includes all the time that the task is waiting to be executed. I.e. the total amount of time we are willing to wait for a task to finish. For example if walltimeout is one day and a task is queued by the cluster system for one day (and never executed), it will timeout, even if the task was never run.
taskShell   | Shell to be used when running a `task` (default `/bin/bash -eu\nset -o pipefail\n`). **WARNING**: Make sure you use "-e" or some command line option that stops execution when an error if found.
sysShell    | Shell to be used when running a `sys` (default `/bin/bash -euo pipefail -c`). **WARNING**: Make sure you use "-e" or some command line option that stops execution when an error if found. **WARNING**: Make sure you use "-c" or some command line option that allows providing a script


###Cluster options 
This section defines parameters to customize `bds` to run tasks on your cluster.

Parameter                           | Comments / examples  
------------------------------------|-----------------------------------------------------------------
pidRegex                            | Regex used to extract PID from cluster command (e.g. qsub). When `bds` dispatches a task to the cluster management system (e.g. running 'qsub' command), it expects the cluster system to inform the jobID. Typically cluster systems show jobIDs in the first output line. This regex is used to match that jobID. Default, use the whole line. **Note:** Some clusters add the domain name to the ID and then never use it again, some other clusters add a message (e.g. 'You job ...'). Examples: `pidRegex = "(.+).domain.com"` and  `pidRegex = "Your job (\\S+)"`
clusterRunAdditionalArgs            | These command line arguments are added to every cluster 'run' command (e.g. 'qsub'). The string is split into spaces (regex: '\s+') and added to the cluster's run command. E.g.: `clusterRunAdditionalArgs = -A accountID -M user@gmail.com` will cause four additional arguments `{ '-A', 'accountID', '-M', 'user@gmail.com' }` to be added immediately after 'qsub' (or similar) command used to run tasks on a cluster.
clusterKillAdditionalArgs           | These command line arguments are added to every cluster 'kill' command (e.g. 'qdel'). Same rules as 'clusterRunAdditionalArgs' apply.
clusterStatAdditionalArgs           | These command line arguments are added to every cluster 'stat' command (e.g. 'qstat'). Same rules as 'clusterRunAdditionalArgs' apply.
clusterPostMortemInfoAdditionalArgs | These command line arguments are added to every cluster 'post mortem info' command (e.g. 'qstat -f'). Same rules as 'clusterRunAdditionalArgs' apply.


###SGE Cluster options 
This section defines parameters to customize `bds` to run tasks on a Sun Grid Engine cluster.

**IMPORTANT:**	In SGE clusters it is important to enable `ENABLE_ADDGRP_KILL=true` to the `execd_params` parameter of `qconf -sconf`.
Otherwise SGE might not be able to kill `bds` subprocesses running on slave nodes if this option is not enabled.
So, if you don't activate `ENABLE_ADDGRP_KILL=true` killing processes may not work in SGE clusters, nodes will continue to run tasks even after they've been killed either Ctrl-C to bds or by a direct `qdel` command (the cluster reports them as finished, but they might still be running in the slave node).

Parameter   | Comments / examples  
------------|-----------------------------------------------------------------
sge.pe      | Parallel environment in SGE (e.g. 'qsub -pe mpi 4'). 
sge.mem     | Parameter for requesting amount of memory in qsub (e.g. `qsub -l mem 4G`)  
sge.timeout | Parameter for timeout in qsub (e.g. `qsub -l h_rt 24:00:00`)  


**Note on SGE's parallel environment ('-pe'):**

The defaults were set to be compatible with **StarCluster**.
Parallel environment defines how 'slots' (number of cpus requested) 
are allocated. **StarCluster** by default sets up a parallel environment, called “orte”, 
that has been configured for OpenMPI integration within SGE and has a number of slots 
equal to the total number of processors in the cluster.
See details `qconf -sp orte`:
```
pe_name            orte
slots              16
user_lists         NONE
xuser_lists        NONE
start_proc_args    /bin/true
stop_proc_args     /bin/true
allocation_rule    $round_robin
control_slaves     TRUE
job_is_first_task  FALSE
urgency_slots      min
accounting_summary FALSE
```

Notice the `allocation_rule = $round_robin`.  This defines how to assign slots to a job. By 
default StarCluster configures round_robin allocation. This means that if a job requests 8 
slots for example, it will go to the first machine, grab a single slot if available, move to 
the next machine and grab a single slot if available, and so on wrapping around the cluster 
again if necessary to allocate 8 slots to the job.

You can also configure the parallel environment to try and localize slots as much as 
possible using the "fill_up" allocation rule and job_is_first_task of TRUE.

To configure: qconf -mp orte

###Generic Cluster options 
Cluster **generic** invokes user defined scripts for manupulating tasks.
This allows the user to customize scripts for particular cluster environments (e.g. environments not currently supported by `bds`)


**Note**: You should either provide the script's full path or the scripts should be in your PATH


**Note**: These scripts "communicate" with bds by printing information on STDOUT.
The information has to be printed in a very specific format. Failing to adhere to the format will cause bds to fail in unexpected ways.

**Note**: You can use command path starting with '~' to indicate `$HOME` dir or '.' to indicate path relative to config file's dir


Parameter                     | Comments / examples  
------------------------------|-----------------------------------------------------------------
clusterGenericRun             | The specified script is executed when a task is submitted to the cluster
clusterGenericKill            | The specified script is executed in order to kill a task
clusterGenericStat            | The specified script is executed in order to show the jobID of all jobs currently scheduled in the cluster
clusterGenericPostMortemInfo  | The specified script is executed in order to get information of a recently finished jobId. This information is typically used for debuging and is added to bds's output.


#### Generic cluster: `clusterGenericRun`

**Script's expected output**:
The script MUST print the cluster's jobID AS THE FIRST LINE. 
Make sure to flush STDOUT to avoid other lines to be printed out of order.

**Command line arguments**:

- Task's timeout in seconds. Negative number means 'unlimited' (i.e. let the cluster system decide)
- Task's required CPUs: number of cores within the same node.
- Task's required memory in bytes. Negative means 'unspecified' (i.e. let the cluster system decide)
- Cluster's queue name. Empty means "use cluster's default"
- Cluster's STDOUT redirect file. This is where the cluster should redirect STDOUT.
- Cluster's STDERR redirect file. This is where the cluster should redirect STDERR
- Cluster command and arguments to be executed (typically is a "bds -exec ...").

**Example:** For examples on how to build this script, take a look at `config/clusterGeneric*` directory in the source code.

#### Generic cluster: `clusterGenericKill`

**Script's expected output**:
None

**Command line arguments**:
jobId: This is the jobId returned as the first line in 'clusterGenericRun' 
script (i.e. the jobID provided by the cluster management system)

**Example:** For examples on how to build this script, take a look at `config/clusterGeneric*` directory in the source code.

#### Generic cluster: `clusterGenericStat`

The specified script is executed in order to show the jobID of all jobs currently scheduled in the cluster

**Script's expected output**:
This script is expected to print all jobs currently scheduled or 
running in the cluster (e.g. qstat), one per line. The FIRST column 
should be the jobID (columns are space or tab separated). Other 
columns may exist (but are currently ignored).

**Command line arguments**:
None

**Example:** For examples on how to build this script, take a look at `config/clusterGeneric*` directory in the source code.

#### Generic cluster: `clusterGenericPostMortemInfo`

**Script's expected output**:
The output is not parsed, it is stored and later shown 
in bds's report. Is should contain information relevant 
to the job's execution (e.g. `qstat -f $jobId` or `checkjob -v $jobId`)

**Command line arguments**:
jobId: This is the jobId returned as the first line in 'clusterGenericRun' 
script (i.e. the jobID provided by the cluster management system)

**Example:** For examples on how to build this script, take a look at `config/clusterGeneric*` directory in the source code.

###SSH Cluster options 
Cluster **shh** creates a virtual cluster using several nodes access via shh.

Parameter | Comments / examples  
----------|-----------------------------------------------------------------
ssh.nodes | This defines the userName and nodes to be accessed via ssh.


Examples:

A trivial 'ssh' cluster composed only of the localhost accesed via ssh (useful for debugging)
```
ssh.nodes = user@localhost
```

Some company's servers used as an ssh cluster
```
ssh.nodes = user@lab1-1company.com, user@lab1-2company.com, user@lab1-3company.com, user@lab1-4company.com, user@lab1-5company.com
```

A StarCluster run on Amazon AWS
```
ssh.nodes = sgeadmin@node001, sgeadmin@node002, sgeadmin@node003, sgeadmin@node004, sgeadmin@node005, sgeadmin@node006
```


</table>



