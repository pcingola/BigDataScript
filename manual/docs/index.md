#Introduction
BigDataScript is intended as a scripting language for big data pipeline. 

###What?
BigDataScript is a cross-system scripting language for working with big data pipelines in computer systems of different sizes and capabilities. 

###Why?
Working with heavyweight computation and big data pipelines involves making use of several specialized programs. 
Those specialized routines need to be scheduled, called and coordinated; their progress need to be tracked and their results logged. 
That is the job of another script or program. This is when BigDataScript becomes extremely handy.

Developing traditional shell scripts or small programs to coordinate data pipelines presents a fundamental dilemma. 
It is not cross-platform, it simply does not work on all environments or it needs adaptations and re-work for the same thing to work on a laptop, server, server farm, cluster and cloud. 
Often it is simply not possible. 
Because of that, developing big data pipelines for a different environment is time consuming. 
The behaviour on the target environment cannot be assumed to be an exact extrapolation of the results obtained on the development environment. 
This not only is a waste of time, money and energy, it is also reliable source of frustration.

BigDataScript is the solution to the problem.

With BigDataScript, creating jobs for big data is as easy as creating a shell script and it runs seamlessly on any computer system, no matter how small or big it is. 
If you normally use specialized programs to perform heavyweight computations, then BigDataScript is the glue to those commands you need to create a reliable pipeline.

###How?

**Benefits of BigDataScript**

* **Reduced development time** Spend less time debugging your work on big systems with a huge data volumes. Now you can debug the same jobs using a smaller sample on your computer. Get immediate feedback, debug, fix and deploy when it's done. Shorter development cycles means better software.
* **System independent** Cross-system, seamless execution, the same program runs on a laptop, server, server farm, cluster or cloud.  No changes to the program required. Work once.
* **Easy to learn** The syntax is intuitive and it resembles the syntax of most commonly used programming languages. Reading the code is easy as pi.
* **Automatic Checkpointing** If any task fails to execute, BigDataScript creates a checkpoint file, serializing all the information from the program. Want to restart were it stopped? No problem, just resume the execution from the checkpoint.  
* **Automatic logging** Everything is logged (<code>-log</code> command line option), no explicit actions required. Every time you execute a system command or a task, BigDataScript logs the executed commands, stdout &amp; stderr and exit codes.  
* **Clean stop with no mess behind** You have a BigDataScript running on a terminal and suddenly you realized there is something wrong... Just hit Ctrl-C. All scheduled tasks and running jobs will be terminated, removed from the queue, deallocated from the cluster. A clean stop allows you to focus on the problem at hand without having to worry about restoring a clean state.  
* **Task dependencies** In complex pipelines, tasks usually depend on each other.  BigDataScript provides ways to easily manage task dependencies.  
* **Avoid re-work** Executing the pipeline over and over should not re-do jobs that were completed successfully and moreover are time consuming.  Task dependency based on timestamps is a built-in functionality, thus making it easy to avoid starting from scratch every time.
* **Built in debugger** Debugging is an integral part of programming, so it is part of <code>bds</code> language.  Statements <code>breakpoint</code> and <code>debug</code> make debugging part of the language, instead of requiring platform specific tools.
* **Built in test cases facility** Code testing is performed in everyday programming, so testing is built in <code>bds</code>.
	

### Paper & Citations 

If you are using BigDataScript in an academic environment, please cite our <a href="https://doi.org/10.1093/bioinformatics/btu595">paper</a>:
```
BigDataScript: A scripting language for data pipelines 
P. Cingolani; R. Sladek; M. Blanchette
Bioinformatics 2014;
doi: 10.1093/bioinformatics/btu595
```

### A word about performance
BigDataScript is meant to be used in the context or heavyweight computations.
Potential delays incurred by BigDataScript should not affect the overall time.

Think about it this way: If you are invoking a set of programs to perform big data computations, these programs usually take hours or days to run.
The fact that BigDataScript takes a few milliseconds more to invoke those programs, really doesn't make any difference.


### Why is it called "BigDataScript" 
Because that's the lamest name I could find.

### Disclaimer 
BigDataScript is experimental and under heavy development. Use at your own risk.
Know side effect include: computer explosions, instant decapitation, spontaneous human combustion, and dead kittens.


