# bds Virtual Machine

Under the hood, `bds` runs a simple virtual machine called `bdsVm`.
This virtual machine has a set of instructions (i.e. assembly) and the respective OpCodes (analogous to a real CPU).
So, any `bds` program is compiled to `bdsVm` assembly and then executed by the `bdsVm`.

### Why bdsVm?

The main reason for using a custom virtual machine is to be able to perform **absolute serializetion** (i.e. checkpoints).
Other virtual machines (or CPUs) do not have this feature, so we needed to create out own VM mainly due to the need to generate and recover from checkpoints.

Checkpoints quite important in `bds` and are used in many operations, most notably when performing "improper tasks".
For instance when trying to continue execution on another server in the Cloud.

### bdsVm model

The `bdsVm` is a [stack machine](https://en.wikipedia.org/wiki/Stack_machine).
That means that operations are performed on a "stack" instead of using "registers".
The main reason for using a stack model is simplicity, it is quite easy to implement a virtual machine using a stack model.
As opposed to other models, stack machines can be slower.

### bdsVm information

Runing `bds` using the "debug" flag (`-d`), will show the corresponsing assembly language compilation.

For example, here is a simple `bds` program:
```
a := 2
b := 40
c := a + b
println "c=$c"
```

If we execute it using `-d` we'll see:
```
$ bds -d z.bds
...
# Assembly: Start
     0    node 1258
# ProgramUnit : int a := 2
main:
     2    scopepush
     3    node 1260
# VariableInitImplicit : a := 2
     5    pushi 2
     7    varpop 'a'
     9    node 1263
# VariableInitImplicit : b := 40
    11    pushi 40
    13    varpop 'b'
    15    node 1266
# VariableInitImplicit : c := a + b
    17    load 'a'
    19    load 'b'
    21    addi
    22    varpop 'c'
    24    node 1271
# Println : println "c=$c"
    26    pushs 'c='
    28    load 'c'
    30    adds
    31    println
    32    halt
```

The comment lines (starting with `#`) show exactly the `bds` code that created the corresponding assembly instructions.

For example the line `VariableInitImplicit : a := 2` means that there was a variable declared implicitly (i.e. the variable type is inferred from the initialization value)
The corresponding assembly code is:
```
# VariableInitImplicit : a := 2
     5    pushi 2		# Push the value '2' to the stack
     7    varpop 'a'	# Create a variable called 'a' and set the value from the latest element in the stack (i.e. '2')
```


The `node` instructions refer to the node number in the Abstract Syntax Tree (AST) that was created from the Lexer+Parser.
Essentially, they are only references to the exact program file and line number and they are just used to know which file/line number we are corrently executing.
This is used for printing error messages if something fails during execution.


### bdsVm state

If you run a program with `-d` command line option, `bds` will also show the execution details:
For each assembly instruction, `bds -d` will show:
- Current `bds` code being executed
- Current `bdsVm` instruction being executed
- Current stack

Example, using the same example program as before:
```
a := 2
b := 40
c := a + b
println "c=$c"
```

If we execute using `-d`, we can see the stack whenever it changes
Note that the output has been edited for clarity and some comments added to clarify the `bdsVm` assembly:
```
1  main:
2    scopepush                # Create a new variable's scope
3    node 1260
4                             # VariableInitImplicit : a := 2
5    pushi 2                  # stack: [2 ]
7    varpop 'a'               # Create a new variable 'a' and set using latest stack entry ('2')
9    node 1263
10                            # VariableInitImplicit : b := 40
11    pushi 40                # stack: [40 ]
13    varpop 'b'              # Create a new variable 'b' and set using latest stack entry ('40')
15    node 1266
16                            # VariableInitImplicit : c := a + b
17    load 'a'                # Load variable's "a" value to the stack. stack: [2 ]
19    load 'b'                # Load variable's "b" value to the stack. stack: [2, 40 ]
21    addi                    # Add two integers values from stack, push result to stack. stack: [42 ]
22    varpop 'c'              # Create a new variable 'c' and set using latest stack entry ('42')
24    node 1271
25                            # Println : println "c=$c"
26    pushs 'c='              # Push string "c=" to the stack. stack: ['c=' ]
28    load 'c'                # Load variable's "c" value to the stack. stack: ['c=', 42 ]
30    adds                    # Add two strings (concatenate) and push to the stack. stack: ['c=42' ]
31    println                 # Print latest value from the stack
32    halt                    # Halt bdsVm execution
```

## `bdsVm` examples

Here are some "easy" examples of how `bds` code is compiled into `bdsVm` assembly

### bds code: `a = 4`
```
pushi 4
load a
set
```

### bds code: `a = 2 + 3`
```
pushi 2
pushi 3
addi
load a
set
```

### bds code: `a[i] = b[j] + 7`
```
            # Stack
push 7
load j
load b      # b, j, 7
reflist     # b[j], 7
addi
load i      # i, b[j]+7
load a      # a, i, b[j]+7
reflist     # a[i], b[j]+7
set         # -
```


### bds code: `a{'hi' + 1} = 'bye'`
```
            # Stack
pushs 'bye'
pushs 'hi'
pushi 1
adds        # 'hi1', 'bye'
load a      # a, 'hi1', 'bye'
refdict     # a{'hi1'}, 'bye'
set         # -
```

### bds code: ` z.a[7]{'hi'} = 42`
In this case `z` is an object
```
            # Stack
pushi 42    # 42
pushs 'hi'  # 'hi', 42
pushi 7     # 7, 'hi', 42
pushs 'a'   # 'a', 7, 'hi', 42
load z      # z, 'a', 7, 'hi', 42
reffield    # z.a, 7, 'hi', 42
reflist     # z.a[7], 'hi', 42
refdict     # z.a[7]{'hi}, 42
set
```

### bds code: Function call

bds code:
```
int f(int x) {
	return x+1
}

z = f(7)
```

bdsVm code:
```
f:
load x
push 1
addi       # x+1 is the return value
ret        # Return from function:
           #    - Remove scope (restore old scope)
           #    - pop PC from call-stack (jump to that position)

main:
pushi 7
call f     # Function call:
           #    - create new scope
           #    - add arguments as scope variables
           #    - push PC to call-stack
load z
set
```

