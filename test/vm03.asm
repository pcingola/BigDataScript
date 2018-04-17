
main:
# string{} a
new string{string}
var a             
pop

# a{'hi'} = 'bye'
pushs bye
pushs hi
load a
setmap
pop

# z := a{'hi'}
pushs hi
load a
refmap
var z
pop

