# Automatic command line parsing 
No need to manually parse command line options for your scripts, `bds` does it for you.

`bds` automatically parse any command line line argument that starts with "-" and assigns the value to the corresponding variable global variable (if the variable exists).

Example (<a href="bds/test_20.bds">test_20.bds</a>):
```
#!/usr/bin/env bds

in := "in.txt"
print("In file is '$in'\n")
```

If we run this, we get
```
$ ./test_20.bds 
In file is 'in.txt'
```

Now we pass a command line argument `-in another_file.txt`, and `bds` automatically parses that command line option replacing the value of variable `in`
```
$ ./test_20.bds -in another_file.txt
In file is 'another_file.txt'
```

This feature also works for other data types (`int`, `real`, `bool` or lists). 

###Flags
In case of `bool` if the option is present, the variable is set to `true`.
File <a href="bds/test_21.bds">test_21.bds</a>
```
#!/usr/bin/env bds

bool flag
print("Variable flag is $flag\n")
```

```
$ ./test_21.bds
Variable flag is false
```

```
$ ./test_21.bds -flag
Variable flag is true
```

Or you can specify the value (`true` or `false` which is useful to set to `false` a bool that is by default `true`:
File <a href="bds/test_21b.bds">test_21b.bds</a>
```
#!/usr/bin/env bds

flagOn  := true
flagOff := false
print("flagOn = $flagOn\nflagOff = $flagOff\n")
```

So in this example we can reverse the defaults by running this (note that we can use `-flagOff` instead of `-flagOff true `):
```
$ ./test_21b.bds -flagOn false -flagOff true 
flagOn = false
flagOff = true
```
Note that we can use `-flagOff` instead of `-flagOff true`.


###Lists
You can also apply this to a list of strings.
In this case, all command line arguments following the `-listName` will be included in the list (up to the next argument starting with '-').

E.g.: Note that list `in` is populated using `in1.txt in2.txt in3.txt` and `out` is set to `zzz.txt`
File <a href="bds/test_22.bds">test_22.bds</a>
```
#!/usr/bin/env bds

in  := ["in.txt"]
out := "out.txt"
ok	:= false

print("In : $in\n")
print("Out: $out\n")
print("OK : $ok\n")
```

```
$ ./test_22.bds  -ok -in in1.txt in2.txt in3.txt -out zzz.txt
In : [in1.txt, in2.txt, in3.txt]
Out: zzz.txt
OK : true
```

