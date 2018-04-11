
main:
new string{string}  # string{} a
var a             


pushs bye           # stack: bye
pushs hi            # stack: hi, bye
load a              # stack: a, hi, bye
setmap              # a{hi} = bye
pop

pushs hi            # z := a{'hi'}
load a
refmap
var z
pop

