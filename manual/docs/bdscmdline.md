# `bds` Command line options 

Running the bds command without any arguments shows a help message
```
$ bds
...
Usage: BigDataScript [options] file.bds

Available options: 
  [-c | -config ] bds.config     : Config file. Default : /Users/pcingola/.bds/bds.config
  [-checkPidRegex]               : Check configuration's 'pidRegex' by matching stdin.
  [-d | -debug  ]                : Debug mode.
  -dryRun                        : Do not run any task, just show what would be run. Default: false
  [-extractSource]               : Extract source code files from checkpoint (only valid combined with '-info').
  [-i | -info   ] checkpoint.chp : Show state information in checkpoint file.
  [-l | -log    ]                : Log all tasks (do not delete tmp files). Default: false
  -noReport                      : Do not create any report.
  -noReportHtml                  : Do not create HTML report.
  -noRmOnExit                    : Do not remove files marked for deletion on exit (rmOnExit). Default: false
  [-q | -queue  ] queueName      : Set default queue name.
  -quiet                         : Do not show any messages or tasks outputs on STDOUT. Default: false
  -reportHtml                    : Create HTML report. Default: true
  -reportYaml                    : Create YAML report. Default: false
  [-r | -restore] checkpoint.chp : Restore state from checkpoint file.
  [-s | -system ] type           : Set system type.
  [-t | -test   ]                : Run user test cases (runs all test functions).
  [-v | -verbose]                : Be verbose.
  -version                       : Show version and exit.
  [-y | -retry  ] num            : Number of times to retry a failing tasks.
  -pid <file>                    : Write local processes PIDs to 'file'
```

Option short  |  Option long     | Meaning    
--------------|------------------|---------------------------------------------------------------------------------------
-c            | -config          | Path to bds.config file (most of the times no config file is needed)    
              | -checkPidRegex   | Check configuration's 'pidRegex' by matching stdin.    
-d            | -debug           | Debug mode, shows detailed information for debugging scripts or debugging bds itself.    
              | -dryRun          | Do not run any task, just show what would be run. This mode is used when you just want to test your script's logic without executing any tasks.    
              | -extractSource   | Extract source code files from checkpoint (only valid combined with '-info').   
-i            | -info            | Show state information in checkpoint file. Prints variables, scopes, etc.   
-l            | -log             | Log all tasks (do not delete tmp files).   
              | -noReport        | Do not create any report.   
              | -noReportHtml    | Do not create HTML report.   
              | -noRmOnExit      | Do not remove files marked for deletion on exit (rmOnExit).    
-q            | -queue           | Set default cluster's queue name.    
              | -quiet           | Do not show any messages or tasks outputs on STDOUT. This means that only the output from `print` (and other print-like bds statements), statements will be shown on the console    
              | -reportHtml      | Create HTML report. Create an HTML report (only if at least one task is executed). By default this option is activated. 
              | -reportYaml      | Create YAML report. Create a YAML report (only if at least one task is executed).  
-r            | -restore         | Restore from a a checkpoint and continue execution.   
-s            | -system          | Define the 'system' type the script is running on (e.g. cluster type)    
-t            | -test            | Perform all tests in a script (i.e. run all functions that are called "test")    
-v            | -verbose         | Be verbose (show more information)    
              | -version         | Show version number and exit.   
-y            | -retry           | Number of times to retry a failing tasks.    
              | -pid             | Write local processes PIDs to 'file'. Under normal circumstances, you should never use this command line option (unless you are debugging bds itself).    

