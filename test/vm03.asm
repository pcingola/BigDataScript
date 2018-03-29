
main:
new string{string}  # string{} a
store a             

pushs bye           # stack: bye
pushs hi            # stack: hi, bye
load a              # stack: a, hi, bye
setmap              # a{hi} = bye

pushs hi            # z := a{'hi'}
load a
refmap
store z

