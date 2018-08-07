# Predefined global variables 
BigDataScript provides some predefined variables.
                
Whenever you run a BigDataScript program you have several predefined variables:
                     
Variable name                        | Meaning  
-------------------------------------|---------------------------------------------------------------------------------------------------------------------------------
`allowEmpty`                         | Default values for `task`
`canFail`                            | Default values for `task`
`cpus`                               | Default values for `task`
`mem`                                | Default values for `task`
`timeout`                            | Default values for `task`
`node`                               | Default values for `task`
`queue`                              | Default values for `task`
`retry`                              | Default values for `task`
`system`                             | Default values for `task`
`taskName`                           | Default values for `task`
`timeout`                            | Default values for `task`
`walltimeout`                        | Default values for `task`
string `programName`                 | The program's name   
string `programPath`                 | The program's path   
string[] `args`                      | Arguments used to invoke the program, i.e. all command line options after program name (`bds [options] prog.bds args ...`).   
string `ppwd`                        | Canonical (physical) path to directory where the program is being executed.   
int `cpusLocal`                      | Number of cores in the computer running the script   
Environment variables                | All shell environment variables at the moment of invocation (e.g. `HOME`, `PWD`, etc.)  
int `K, M, G, T, P`                  | Kilo, Mega, Giga, Tera, Peta (2^10, 2^20, 2^30, 2^40 and 2^50 respectively)  
real `E, PI`                         | Euler's constant (2.718281...) and Pi (3.1415927...)   
int `minute, hour, day, week`        | Number of seconds in a minute, hour, day and week respectively  
