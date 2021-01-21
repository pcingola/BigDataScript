# Tasks on AWS

You can set `bds` tasks to run on the Amazon cloud (i.e. an EC2 instance).
This is achieved by using `system='aws'`.

### WARNING

**WARNING: Using `bds` with cloud can incur in costs, unexpected expenses
You need to thoroughly review cloud resources used by your `bds` programs to make sure you are not incurring into unwanted or unexpected expenses.**

**`bds` does not make any warranties, use at your own risk.**

### Pre-requisites

You need all the following to run tasks on AWS:

- Amazon AWS access: Obviously you need an AWS account
- Privileges: The AWS security role should have access to the following services
    - EC2: Create, run and terminate EC2 instances
    - S3: Read, write and delete objects from an S3 bucket 
    - SQS: Create, send messages and delete SQS queues
- EC2 image (AMI): An image with `bds` installed (i.e. in order to run `bds` tasks on an instance, you need an image that is capable of running `bds`)

## Example walk-through

### Example walk-through: Program and AWS parameters

The next "toy example" shows how to run a simple tasks on AWS.


Parameters for the EC2 instance are set in `taskResources` hash.
In this example, the task will run on an instance in `us-east-1` region.
It is also assumed that the EC2 image has been setup and `bds` is properly installed in that image.
Obviously, you should replace the AMI number `ami-123456abcdef` with your own image ID.
Similarly, all the other parameters in `taskResources` should be set properly according to your account.

```
#!/usr/bin/env bds

# These hash called 'taskResources' contains the parameters we need to run a task on AWS
# WARNING: You need to replace ALL this parameters to use your account's settings
taskResources := { \
    'region' => 'us-east-1' \
    , 'instanceType' => 't3a.medium' \
    , 'imageId' => 'ami-123456abcdef' \
    , 'securityGroupIds' => 'sg-987654321abc' \
    , 'subnetId' => 'subnet-192837465fed' \
    , 'instanceProfile' => 'AWS_BDS_INSTANCE_ROLE' \
}

# This task is run on an AWS instance!
task(system := 'aws') {
    sys echo HI
    sys for i in `seq 10`; do echo "count: \$i"; sleep 1; done
    sys echo BYE
}

println "After"     # This message is show after the task is scheduled (the instance being requested in the background)
wait                # Wait until the task finishes (i.e. the instance finishes running)
println "Done"      # This message is shown after the task finished running
```

### Example walk-through: Running the program

OK, let's run this example program and analyze each step of the `bds` output.

We run using the `-v` command line options, so we'll see more verbose output and `-log` to get the outputs logged to files.

```
$ bds -log -v z.bds

00:00:00.004	Bds 3.0b (build 2021-01-14 14:34), by Pablo Cingolani
Before
After
```
These are just the `println` statements from our example program.
The message `After` is shown after the task statement, so at this point the task was scheduled for execution (but it's not running yet). 

```
00:00:00.484	INFO : Creating AWS SQS queue 'bds_20210121_090423_234750293cafe'
```
This indicates that an SQS queue was created.
This SQS queue is used to communicate task StdOut, StdErr, and task exit status to the computer running the `bds` program (e.g. your laptop)
Since the instance can be inaccessible from our network and vice-versa (e.g. we could be running the script on our laptop behind a corporate firewall), SQS is used to communicate both ends.


```
00:00:04.229	INFO Cmd 'z.bds.20210121_090423_293/task.z.line_16.id_1.337e55c1b5c62785': Created EC2 instance: 'i-0d5931b8e30fba349', for task 'z.bds.20210121_090423_293/task.z.line_16.id_1.337e55c1b5c62785'
```

The instance was created and the task will be executed in that instance.
Note that the instance will be automatically terminated when the task finishes executing.

```

00:01:01.835	INFO : Writing report file 'z.bds.20210121_090423_293.report.html'
00:01:05.810	INFO : Tasks [CloudAws[13]]		Pending: 0	Running: 1	Done: 0	Failed: 0
		| PID                 | Task state        | Task name                            | Dependencies | Task definition                                                          |
		| ------------------- | ----------------- | ------------------------------------ | ------------ | ------------------------------------------------------------------------ |
		| i-0d5931b8e30fba349 | running (RUNNING) | task.z.line_16.id_1.337e55c1b5c62785 |              | echo HI; for i in `seq 10`; do echo "count: $i"; sleep 1; done; echo BYE |
```

The instance usually takes over a minute to startup.
We run using `bds -v ...`, in verbose mode `bds` will show a report of all tasks running (roughly) every one minute.
In this case, there is only one task.

```
HI
count: 1
count: 2
count: 3
count: 4
count: 5
count: 6
count: 7
count: 8
count: 9
count: 10
BYE
```

These are the output lines from the task, which is running on the EC2 instance.
Note that even though we are not connected to the instance, we can the task's Stdout because it's sent via an SQS queue. 
After the task finished, the instance is terminated.
You can check on the AWS management console that the instance changes state first to "Shutting down" and later to "Terminated".

```
Done
```
This was the `println "Done"` statement from our program, since the statement was after a `wait` statement, this is shown after the task finished running. 

```
00:01:36.518	INFO : Writing report file 'z.bds.20210121_090423_293.report.html'
00:01:36.524	INFO : Writing report file 'z.bds.20210121_090423_293.report.yaml'
00:01:36.529	INFO : Deleting AWS SQS queue 'az-ngs-seqauto_20210121_090423_74c7f1be2f3ccca5', url: 'https://sqs.us-east-1.amazonaws.com/671016219382/az-ngs-seqauto_20210121_090423_74c7f1be2f3ccca5'
```

The last messages show that a report was created and the SQS queue was deleted.

### Example walk-through: Looking to the log files

When the `bds` script is run using `-log` command line option, files will be created to log every task.
In the previous example there was only one task, here are the files and details:

```
# Go to the log direcotory (created by 'bds -log ...')
$ cd z.bds.20210121_090423_293

# List files (added comments for each file)
$ ls
task.z.line_16.id_1.337e55c1b5c62785.ec2_request_response.i-0d5931b8e30fba349.txt
task.z.line_16.id_1.337e55c1b5c62785.startup_script.sh
task.z.line_16.id_1.337e55c1b5c62785.exitCode
task.z.line_16.id_1.337e55c1b5c62785.stdout
task.z.line_16.id_1.337e55c1b5c62785.stderr
task.z.line_16.id_1.337e55c1b5c62785.sh
```

Let's review each file:
- Task STDOUT (`task.z.line_16.id_1.337e55c1b5c62785.stdout`): The standard output from the task is logged in this file. Note that these are messages sent by the EC2 instance via SQS.
```
$ cat task.z.line_16.id_1.337e55c1b5c62785.stdout
HI
count: 1
count: 2
count: 3
count: 4
count: 5
count: 6
count: 7
count: 8
count: 9
count: 10
BYE
```
- Task STDERR (`task.z.line_16.id_1.337e55c1b5c62785.stderr`): The standard error from the task is logged in this file. Note that these are messages sent by the EC2 instance via SQS. IMPORTANT: If there is no STDERR, this file is not created.
```
# This file was not created because the task did not have any output to STDERR
$ cat task.z.line_16.id_1.337e55c1b5c62785.stderr
cat: task.z.line_16.id_1.337e55c1b5c62785.stderr: No such file or directory
``` 
- Task exit code (`task.z.line_16.id_1.337e55c1b5c62785.exitCode`): This is the exit code from the task
```
# Exit code '0' means that the task run succesfully
$ cat task.z.line_16.id_1.337e55c1b5c62785.exitCode
0
```
- EC2 request file (`task.z.line_16.id_1.337e55c1b5c62785.ec2_request_response.i-0d5931b8e30fba349.txt`): This file logs the detailed request parameters when requesting the EC2 instance.
```
$ cat task.z.line_16.id_1.337e55c1b5c62785.ec2_request_response.i-0d5931b8e30fba349.txt
RunInstancesResponse(Groups=[], Instances=[Instance(AmiLaunchIndex=0, ImageId=ami-123456abcdef, InstanceId=i-0d5931b8e30fba349, InstanceType=t3a.medium, LaunchTime=...
```
- Task script (`task.z.line_16.id_1.337e55c1b5c62785.sh`): This is "raw" script defined in the task.
```
$ cat task.z.line_16.id_1.337e55c1b5c62785.sh
#!/bin/bash -eu
set -o pipefail

cd '/home/myuser/bds/example'

# SYS command. line 17
echo HI
# SYS command. line 18
for i in `seq 10`; do echo "count: $i"; sleep 1; done
# SYS command. line 19
echo BYE
# Checksum: 3a69f4dc
```
- Startup script (`task.z.line_16.id_1.337e55c1b5c62785.startup_script.sh`): This is the startup script provided to the instance (EC2 "user data" parameter). This is how the instance knows how to execute the task. we'll discuss this in the next sub-section


### Example walk-through: The instance startup script

Whenever `bds` creates an instance, it must also instruct the instance on what to do (i.e. how to execute the task).
This is achieved using a start-up script (a.k.a. "used data" if you are familiar with the `aws ec2 run-instances` command).

In the example, we saw that there was a "startup script" file created (`task.z.line_16.id_1.337e55c1b5c62785.startup_script.sh`).

Here is the startup script, we'll analyze each part:
```
#!/bin/bash -eu
set -o pipefail

function exit_script {
  shutdown -h now
}

export HOME='/root'
trap exit_script EXIT
echo "INFO: Starting script '$0'"

mkdir -p '/home/myuser/bds/example/z.bds.20210121_090423_293'
#	#!/bin/bash -eu
#	set -o pipefail
#
#	cd '/home/myuser/bds/example'
#
#	# SYS command. line 17
#	echo HI
#	# SYS command. line 18
#	for i in `seq 10`; do echo "count: $i"; sleep 1; done
#	# SYS command. line 19
#	echo BYE
#	# Checksum: 3a69f4dc

grep '^#	' "$0" | cut -c 3- > '/home/myuser/bds/example/z.bds.20210121_090423_293/task.z.line_16.id_1.337e55c1b5c62785.startup_script_instance.sh'
chmod u+x '/home/myuser/bds/example/z.bds.20210121_090423_293/task.z.line_16.id_1.337e55c1b5c62785.startup_script_instance.sh'
bds exec -stdout '/home/myuser/bds/example/z.bds.20210121_090423_293/task.z.line_16.id_1.337e55c1b5c62785.stdout' -stderr '/home/myuser/bds/example/z.bds.20210121_090423_293/task.z.line_16.id_1.337e55c1b5c62785.stderr' -exit '/home/myuser/bds/example/z.bds.20210121_090423_293/task.z.line_16.id_1.337e55c1b5c62785.exit' -taskId 'z.bds.20210121_090423_293/task.z.line_16.id_1.337e55c1b5c62785' -awsSqsName 'https://sqs.us-east-1.amazonaws.com/671016219382/az-ngs-seqauto_20210121_090423_74c7f1be2f3ccca5' -timeout '86400' '/home/myuser/bds/example/z.bds.20210121_090423_293/task.z.line_16.id_1.337e55c1b5c62785.startup_script_instance.sh'

echo "INFO: Finished script '$0'"
```

The script has three main sections:

1. Capture any error (trap) and make sure the instance is shut down if any error occurs or when the script finishes successfully. Since the instances are run with "terminate on shutdown" option enabled, this forces the instance to be terminated (so you don't get charged by an instance that has finished or failed to run the task)
1. Copy the task lines defined in the `bds` program, into a new file and change mode to allow execution
1. Execute the file that runs the task using `bds ... -awsSqsName ...` which redirect STDOUT, STDERR (and exit code) to the SQS queue
1. Script finishes

Let's see some details of each part of the startup script:

- Capture any error (trap) and make sure the instance is shut down if any error occurs or when the script finishes successfully. Since the instances are run with "terminate on shutdown" option enabled, this forces the instance to be terminated (so you don't get charged by an instance that has finished or failed to run the task)
```
#!/bin/bash -eu           # This makes sure that bash exits if there are any errors
set -o pipefail           # Tell bash to exit even if the errors are in piped command

function exit_script {    # This function is used to shutdown the instance 
  shutdown -h now
}

export HOME='/root'       # The startup script is executed by the instance at startup time as 'root' user, HOME variable is not defined yet so we define it
trap exit_script EXIT     # Trap any EXIT signal and execute 'exit_script' funtion (which shut downs the instance)
```
- Copy the task lines defined in the `bds` program, into a new file and change mode to allow execution
```
mkdir -p '/home/myuser/bds/example/z.bds.20210121_090423_293'   # Create a directory (same location as in the original bds program)
```

The lines below are the script lines defined by the bds program (see "task line").
Note that they are the exact same lines as defined in the task script (`task.z.line_16.id_1.337e55c1b5c62785.sh`), but they have a `#\t` prepended to each line
```
#	#!/bin/bash -eu
#	set -o pipefail
#
#	cd '/home/myuser/bds/example'
#
#	# SYS command. line 17
#	echo HI
#	# SYS command. line 18
#	for i in `seq 10`; do echo "count: $i"; sleep 1; done
#	# SYS command. line 19
#	echo BYE
#	# Checksum: 3a69f4dc
```
Next, there is a `grep` command to extract all the lines starting with `#\t` to a new file.
So this writes all the line defined in the original task to a new script file and makes it executable
``` 
grep '^#	' "$0" | cut -c 3- > '/home/myuser/bds/example/z.bds.20210121_090423_293/task.z.line_16.id_1.337e55c1b5c62785.startup_script_instance.sh'   
chmod u+x '/home/myuser/bds/example/z.bds.20210121_090423_293/task.z.line_16.id_1.337e55c1b5c62785.startup_script_instance.sh'
```

- Execute the file that runs the task using `bds ... -awsSqsName ...` which redirect STDOUT, STDERR (and exit code) to the SQS queue
```
bds exec -stdout '/home/myuser/bds/example/z.bds.20210121_090423_293/task.z.line_16.id_1.337e55c1b5c62785.stdout' -stderr '/home/myuser/bds/example/z.bds.20210121_090423_293/task.z.line_16.id_1.337e55c1b5c62785.stderr' -exit '/home/myuser/bds/example/z.bds.20210121_090423_293/task.z.line_16.id_1.337e55c1b5c62785.exit' -taskId 'z.bds.20210121_090423_293/task.z.line_16.id_1.337e55c1b5c62785' -awsSqsName 'https://sqs.us-east-1.amazonaws.com/671016219382/az-ngs-seqauto_20210121_090423_74c7f1be2f3ccca5' -timeout '86400' '/home/myuser/bds/example/z.bds.20210121_090423_293/task.z.line_16.id_1.337e55c1b5c62785.startup_script_instance.sh'

echo "INFO: Finished script '$0'"
```

- Script finishes: Several things happen whenever the startup script finishes executing:
    - After the last line is successfully executed, the script will exit, i.e. issue an `EXIT` signal.
    - This signal is trapped (see previous `trap exit_script EXIT` line) and the `exit_script` function will be invoked.
    - The `exit_script` function performs a `shutdown -h now` which shuts down the instance.
    - Since the instance was run using "terminate on shutdown" behaviour (i.e. `instance-initiated-shutdown-behavior='shutdown'`), the instance will be terminated.   

## AWS tasks

In the next sub-sections we cover some details on AWS tasks resources, EC2 parameters, task dependencies, etc.
  
### AWS resource cleanup

`bds` will try to clean up AWS resources when the program ends successfully or when it is interrupted (e.g. you press `Ctr-C` on the terminal running the script).
This means that, under normal circumstances, EC2 instances will be terminated, SQS queues deleted, etc.

Unfortunately, there is no way to guarantee that the cleanup will be performed or that it will succeed.
You can easily imagine a situations where the `bds` script is unable to clean up AWS resources, for instance:
- It is running on a laptop and you accidentally close the laptop
- The server where the `bds` script is running loses internet connection
- The `bds` script is killed with `kill -9 ...`, thus it has no chance to send API messages to AWS to clean up. 
 
**IMPORTANT:** There is no warranty on cleaning resources. `bds` attempt to cleanup resources on AWS, but this is a "best effort" approach. 

**WARNING:** In order to avoid unnecessary AWS costs, you should always monitor AWS resources used by `bds`. 

### StdOut / StdErr

As we've already mentioned, the instance executing a Task will send bith STDOUT and STDERR to an SQS queue so that the original program can show them on the main terminal.
 
It is important to understand that if a task generates many output lines, this could potentially generate millions of SQS messages.
Not only this could generates costs due to SQS messages, but also can create high network traffic and delay the task significantly (since the process must wait for all those messages to be sent).

A simple option would be to redirect STDOUT/STDERR to a file (or to `/dev/null`) when not needed, e.g.:
```
task( system := 'aws' ) {
    sys command_with_lots_of_output > /dev/null 2>&1
}
```  
 
### Improper tasks

AWS tasks can also be "improper", this means that they can execute arbitrary `bds` code.
In this case, as always happens in improper tasks, the whole program state will be accessible to the task runningin the instance:

```
task( system := 'aws' ) {
    println "Executing standard bds code in this task"
    for(int i=0 ; i < 10 ; i++ ) {
        println "Count: $i"
    }
    println "End of task, the EC2 instance will terminate after this"
}
```

### Dependencies

Tasks executed on AWS can have dependencies, as any other tasks.
Usually dependencies are files on S3.

In this example you see two tasks dependent on each other:
1. The main bds script creates an "input" file on S3 (`in := "s3://my_bds_test_bucket/exmaple/in.txt"`)
1. The first task (`task(out1 <- in) ...`) reads the input file, adds some data and writes the result to `out1` output file (`out1 := "s3://my_bds_test_bucket/exmaple/out1.txt"`)
1. The second task (`task(out2 <- out1) ...`) reads the `out1` file, adds some more data and writes the result to `out2` output file (`out2 := "s3://my_bds_test_bucket/exmaple/out2.txt"`)

```
system = 'aws'

# These hash called 'taskResources' contains the parameters we need to run a task on AWS
# WARNING: You need to replace ALL this parameters to use your account's settings
taskResources := { \
    'region' => 'us-east-1' \
    , 'instanceType' => 't3a.medium' \
    , 'imageId' => 'ami-123456abcdef' \
    , 'securityGroupIds' => 'sg-987654321abc' \
    , 'subnetId' => 'subnet-192837465fed' \
    , 'instanceProfile' => 'AWS_BDS_INSTANCE_ROLE' \
}

# Input and output files on S3
in := "s3://my_bds_test_bucket/exmaple/in.txt"
out1 := "s3://my_bds_test_bucket/exmaple/out1.txt"
out2 := "s3://my_bds_test_bucket/exmaple/out2.txt"

# Create input file
in.write(inTxt)

# Task 1
println "Before task1"
task(out1 <- in) {
    println "Start: Task1 improper"
    inFileTxt := in.read().trim()
    println "Input text: '$inFileTxt'"
    out1.write("OUT1: '$inFileTxt'")
    println "End: Task1 Improper"
}
println "After task1"

# Task 2
println "Before task2"
task(out2 <- out1) {
    sys echo "Start: Task2"
    sys echo 'OUT2' > '$out2'
    sys cat '$out1' >> '$out2'
    sys echo 'Input:'
    sys cat '$out1'
    sys echo
    sys echo "End: Task2"
}
println "After task2"

wait
println "Done"
```

Each of these two tasks are executed in different EC2 instances.
There is a dependency: the second needs `out1` which is created by the first task.
So the second task will not be executed until the first task finishes successfully, i.e. the second instance will only be created when the first instance finishes executing the first tasks and the output file `out1` is created.

### Detached tasks

A "detached" task is a task that is run independently from `bds`.
The original `bds` program can finish and the detached task continue running.
In this case it will continue in an AWS EC2 instance.

Here is an example with a "detached" task (i.e. `detached := true`) 
```
system = 'aws'

# These hash called 'taskResources' contains the parameters we need to run a task on AWS
# WARNING: You need to replace ALL this parameters to use your account's settings
taskResources := { \
    'region' => 'us-east-1' \
    , 'instanceType' => 't3a.medium' \
    , 'imageId' => 'ami-123456abcdef' \
    , 'securityGroupIds' => 'sg-987654321abc' \
    , 'subnetId' => 'subnet-192837465fed' \
    , 'instanceProfile' => 'AWS_BDS_INSTANCE_ROLE' \
}

# This detached AWS taks will continue executing after the bds script finishes
task(system := 'aws', detached := true) {
    sys echo HI
    sys for i in `seq 60`; do echo "count: \$i"; sleep 1; done
    sys echo BYE
}
println "After"
wait
println "Done"
```

The task continues running in the EC2 instance, even though the `bds` script finishes immediately after the instance has been requested.

### `dep` and `goal`

As any other tasks, AWS tasks can also be defined using `dep` and `goal` statements, e.g.:

Here is an example that we saw before (two dependent tasks) using `dep` and `goal`: 
```
system = 'aws'

# These hash called 'taskResources' contains the parameters we need to run a task on AWS
# WARNING: You need to replace ALL this parameters to use your account's settings
taskResources := { \
    'region' => 'us-east-1' \
    , 'instanceType' => 't3a.medium' \
    , 'imageId' => 'ami-123456abcdef' \
    , 'securityGroupIds' => 'sg-987654321abc' \
    , 'subnetId' => 'subnet-192837465fed' \
    , 'instanceProfile' => 'AWS_BDS_INSTANCE_ROLE' \
}

# Input and output files on S3
in := "s3://my_bds_test_bucket/exmaple/in.txt"
out1 := "s3://my_bds_test_bucket/exmaple/out1.txt"
out2 := "s3://my_bds_test_bucket/exmaple/out2.txt"

# Create input file
in.write(inTxt)

# Dep 1
println "Before task1"
dep(out1 <- in) {
    println "Start: Dep1 improper"
    inFileTxt := in.read().trim()
    println "Input text: '$inFileTxt'"
    out1.write("OUT1: '$inFileTxt'")
    println "End: Dep1 Improper"
}
println "After task1"

# Dep 2
println "Before task2"
dep(out2 <- out1) {
    sys echo "Start: Dep2"
    sys echo 'OUT2' > '$out2'
    sys cat '$out1' >> '$out2'
    sys echo 'Input:'
    sys cat '$out1'
    sys echo
    sys echo "End: Dep2"
}
println "After task2"

# Goal
println "Goal: '$out2'"
goal out2

wait
println "Done"
```

### AWS task parameters

Any task running on AWS requires many parameters to be set.
Most parameters can be set in the `tataskResources` hash we've seen in the examples.

Here is a list of all the parameters for AWS tasks:

`tataskResources` entry       | Meaning  
------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
region                        | AWS region where the EC2 instance should run          
bucket                        | Bucket name, the bucket can sometimes used to store data by `bds`
instanceType                  | AWS EC2 instance type (e.g. t3a.medium)
imageId                       | Image used to create the instance (AMI ID). The image must have `bds` installed 
securityGroupIds              | A comma separated list of security groups IDs 
subnetId                      | A comma separated list of subnet IDs
instanceProfile               | Profile (role name) to use for the instance
s3tmp                         | S3 temporary path, this is used to store temporary data (e.g. checkpoints for improper tasks)
keepInstanceAliveAfterFinish  | If this is set to `true`, the instance will NOT be terminated after the task is finished. This is used for loggging into the instance and debugging AWS tasks

Other task parameters can also be used (`timeout`, `taskName`, `allowEmpty`, etc.).
Here are some parameters specific to AWS tasks:

Variable              | Meaning  
----------------------|--------------------------------------------------------------------
cloudQueueNamePrefix  | Is non-empty, the name will be used as a prefix for the SQS quque
system                | Must be set to `'aws'` for a task to be executed in an AWS EC2 instance

### Instance request retry

Assuming that all parameters are set correctly, an EC2 instance request can still fail for many different reasons, e.g.:
- AWS doesn't have availability of a specific instance type in the requested region
- There are no more IPs in the network
- You've reached some limit in any EC2 related parameter (total number of instances, total disk, etc)
 
In any case when `bds` cannot launch an EC2 instance it will retry several times, waiting some random time between each try
The retry algorithm is:
```
START_FAIL_MAX_ATTEMPTS = 50
START_FAIL_SLEEP_RAND_TIME = 60

for i in 1 .. START_FAIL_MAX_ATTEMPTS:
    - parse_ec2_instance_parametres     # Parse hash tataskResources
    - randomly_select_subnetId          # If more than one is specified as a comma separated list of values
    - request_ec2_instance              # Request instance to AWS
    - if succeess: return OK            # Success, instance created
    - wait_random_time                  # Reuqest failed, wait up to START_FAIL_SLEEP_RAND_TIME seconds
```

**Note:** Randomly selecting a subnet from a comma separated list in `tataskResources{'subnetId'}`, allows you to randomly create instances on different zones within a region.
  