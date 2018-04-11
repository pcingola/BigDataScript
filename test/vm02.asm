
main:
new string[]
var a             # string[] a
pop

pushs hi          # stack: hi
pushi 0           # stack: 0, hi
load a            # stack a, 0, hi
setlist           # a[0] = hi
pop

pushi 0           # z := a[0]
load a
reflist
var z
pop
