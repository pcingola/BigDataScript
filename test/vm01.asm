
f(int x) -> int:
load x
pushi 1
addi       # x+1 is the return value
ret 

main:
pushi 0
var z
pushi 7
call 'f(int x) -> int'
           # Function call:
           #    - create new scope
           #    - add arguments as scope variables
           #    - push PC to call-stack
load z
set
