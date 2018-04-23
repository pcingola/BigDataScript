
# f(int x) return x + 1
f(int x) -> int:
load x
pushi 1
addi
ret 

main:
# z := 0
pushi 0
var z
pop

# z = f(7)
pushi 7
call 'f(int x) -> int'
           # Function call:
           #    - create new scope
           #    - add arguments as scope variables
           #    - push PC to call-stack
load z
set
