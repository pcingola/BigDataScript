
# Data types 

BDS is a statically typed language that has simple data types. 
The intention is to avoid runtime errors.

The usual basic types are defined and `bds` also offers extensible data types (classes).

Type                   | Meaning
-----------------------|---------------------------------------------------------------------------
string                 | A string (same a Java's String)  
int                    | A 64 bit integer number (same a Java's long)  
real                   | A 64 bit IEEE 754 number (same as Java's double)  
bool                   | A boolean value, can be 'true' or 'false' (same as Java's boolean) 
Arrays, List, Stacks   | These are all the same, just a different way to call a list of elements 
Maps                   | Maps are hashes (a.k.a. dictionaries that have `string` keys. 
Class                  | An object definition

### Strings 

There are several basic methods defined for strings:

Return type | Method / Operator                        | Meaning
------------|------------------------------------------|-----------------------------------------------
 string     | s = s1 + s2                              | Concatenate strings. 
 string     | s += s2                                  | Append to  string. 
 bool       | string.endsWith(string str)              | True if string ends with str 
 bool       | string.isEmpty()                         | True if the string is empty 
 int        | string.indexOf(string str)               | Index of the first occurrence of str in string 
 int        | string.lastIndexOf(string str)           | Index of the last occurrence of str in string 
 int        | string.length()                          | String's length  
 string     | string.replace(string str1,string str2)  | A new string replacing 'str1' with 'str2' 
 bool       | string.parseBool()                       | Parse a bool  
 int        | string.parseInt()                        | Parse an int number  
 real       | string.parseReal()                       | Parse a real number  
 string[]   | string.split(string regex)               | Split using a regular expression  
 bool       | string.startsWith(string str)            | True if string starts with str 
 string     | string.substr(int start)                 | Substring from start to end of string 
 string     | string.substr(int start,int end)         | Substring from start to end 
 string     | string.toLower()                         | Return a lower case version of the string 
 string     | string.toUpper()                         | Return an upper case version of the string 
 string     | string.trim()                            | Trim spaces at the beginnig and at the end 

### Strings as files

Strings can be used in several different ways.
For instance, it is common that a string can represent a file name (path) in a script.
So you can use 'file' related methods on string.
E.g.:
```
string f = "in.txt"
if( f.canRead() ) {
    print (" Can read file $f\n" )
}
```

Here `f` is a string, but it has a method `canRead()` which returns true if f is a file and it can be read.

**String (as file) methods**


 Return type   | Method                                      | Meaning
---------------|---------------------------------------------|------------------------------------------------------------------------------------
 string        | string.baseName()                           | File's base name      
 string        | string.baseName(string ext)                 | File's base name, remove extention 'ext'      
 string        | string.download()                           | Donwload data from URL (string). Returns local file name (empty string if failed)   
 bool          | string.download(string file)                | Donwload data from URL to 'file'. Returns true if succeeded.   
 bool          | string.canRead()                            | True if file has read permission      
 bool          | string.canWrite()                           | True if file has write permission     
 bool          | string.canExec()                            | True if file has execution permission      
 void          | string.chdir()                              | Change current directory  
 bool          | string.delete()                             | Delete file      
 string[]      | string.dir()                                | List files in a directory ('ls')     
 string[]      | string.dir(string regex)                    | List files matching a 'glob' (regular expression for files)  
 string        | string.dirName()                            | File's directory name      
 string[]      | string.dirPath()                            | List files using canonical paths      
 string[]      | string.dirPath(string regex)                | List files, matching a 'glob' (regular expression for files), using canonical paths      
 string        | string.extName()                            | File's extension      
 bool          | string.exists()                             | True if file exists      
 bool          | string.isDir()                              | True if it's a directory      
 bool          | string.isFile()                             | True if it's a file      
 bool          | string.mkdir()                              | Create dir ('mkdir -p')     
 string        | string.path()                               | Absolute path to file      
 string        | string.pathCanonical()                      | Canonical path to file      
 string        | string.pathName()                           | Absolute dir
 string        | string.read()                               | Read the whole file into a string      
 string[]      | string.readLines()                          | Read the whole file and split the lines      
 string        | string.removeExt()                          | Remove file extension   
 string        | string.removeExt(string ext)                | Remove file extension, only if it matches the provided one   
 bool          | string.rm()                                 | Delete a file   
 bool          | string.rmExit()                             | Remove a file when execution finishes (thread execution).  
 int           | string.size()                               | File size in bytes    
 string        | string.swapExt(string newExt)               | Swap file extension   
 string        | string.swapExt(string oldExt,string newExt) | Swap file extension, only if extension matches the provided 'oldExt'  
 string        | string.upload()                             | Upload data to URL (string). Returns true if succeeded.   
 bool          | string.upload(string file)                  | Upload data from 'file' to URL. Returns true if succeeded.   
 string        | string.write(string file)                   | Write string to 'file'   
                
### Strings as tasks

Strings can also be used to refer to tasks.
When a task is created, the `task` expression returns a task ID, which is a string.
This task ID can be used for task operations, for instance:
```
tid := task echo Hello
wait tid
```
Here the `wait` statement will wait until the task "echo Hello" finishes executing.


More task related methods:

Return type | Method              | Meaning
------------|---------------------|------------------------------------------------------
bool        | string.isDone()     | True if the task finished  
bool        | string.isDoneOk()   | True if the task finished without errors   
string      | string.stdout()     | A string with all the STDOUT generated from this task   
string      | string.stderr()     | A string with all the STDERR generated from this task   
int         | string.exitCode()   | Exit code  
string[]    | getTasksDone()      | Return a list of all task IDs that have finished (either succesfully or with errors)
string[]    | getTasksRunning()   | Return a list of all task IDs that are running
string[]    | getTasksToRun()     | Return a list of all task IDs that have not yet started to run (e.g. waiting to be executed)

### Arrays, List, Stacks 

Arrays, list and stacks are all the same thing.
You can create a list of strings simply by declaring:
```
string[] arrayEmpty
string[] array = ["one", "two", "three"]
```

Similarly, a list of ints is just
```
int[] listIntEmpty
int[] primes = [2, 3, 5, 7, 11, 13, 17, 19, 23, 29]
```
                
** Methods **

Returns              | Method             | Meaning 
---------------------|--------------------|-----------------------------------------------------------------------------------------
                     | +=                 | Append element(s) at the end of the list   
Element added        | add(X)             | Add `X` to the end of the list   
Element added        | add(int idx,X)     | Add `X` to position `idx` in the list   
Same list            | delete()           | Delete all files in the list (assumes list elements are file names). Same as `list.rm()`  
int                  | count(X)           | Count number of occurrences of `X` in the list   
bool                 | has(X)             | Does the list contain `X`?   
First element        | head()             | Get first element   
int                  | indexOf(X)         | Position of element `X` in the list   
bool                 | isEmpty()          | 'true' if the list is empty   
string               | join()             | A string joining all elements of the list (separator ' ')   
string               | join(string sep)   | A string joining all elements of the list (separator 'sep')   
Last element         | pop()              | Get last element and remove it from the list   
Element pushed       | push()             | Add at the end of the list   
Element to remove    | remove(X)          | Remove element `X` from the list   
Element to remove    | removeIdx(int idx) | Remove element at position `idx` from the list   
New reversed list    | reverse()          | Create a new list and reverse it  
Same list            | rm()               | Delete all files (assumes list elements are file names)   
Same list            | rmOnExit()         | Delete all files when current thread finishes execution (assumes list elements are file names)   
int                  | size()             | Return the number of elements in the list   
New sorted list      | sort()             | Create a new list sorting the elements of this list  
List                 | tail()             | Create a new list with all but the first element  

**Iterating on an array/list**
You can iterate on an array simply by doing
```
$ cat z.bds 
string[] array = ["one", "two", "three"]

for( string val : array ) { print("Value: $val\n") }

$ bds /z.bds
Value: one
Value: two
Value: three
```

### Maps 
Maps are hashes that have `string` as keys.
You can create a map simply by declaring:
```
string{} mstr    # This maps string keys to string values

mstr{"Hello"} = "Bye"
mstr{"Bonjour"} = "Au revoir"
mstr{"Hola"} = "Adios"
```

or a map of real numbers
```
real{} mre   # This maps string keys to real values
mre{"one"}   = 1.0
mre{"two"}   = 2.0
mre{"e"}     = 2.7182818
mre{"three"} = 3.0
mre{"pi"}    = 3.1415927
```
 
** Methods **

Returns | Method             | Meaning 
--------|--------------------|---------------------------------------
bool    | hasKey(string key) | True if the key is in the map
bool    | hasValue(value)    | True if 'value' is in the map
list    | keys()             | A sorted list of all keys in the map
bool    | remove(key)        | Remove `key` from this map
int     | size()             | Number of elements in this map
list    | values()           | A sorted list of all values in the map

**Iterating on a map**
You can iterate over all values in a map, simply by doing
```
$ cat z.bds 
string{} mstr = { "Hello" => "Bye", "Bonjour" => "Au revoir", "Hola" => "Adios" }

for(string v : mstr ) { 
   print("Values : $v\n") 
}

$ bds z.bds
Values : Adios
Values : Au revoir
Values : Bye

```

If you want to iterate on keys instead of values, you can do this:
```
$ cat z.bds 
string{} mstr = { "Hello" => "Bye", "Bonjour" => "Au revoir", "Hola" => "Adios" }

for(string k : mstr.keys() ) { 
    print("Key : $k\tValue : " + mstr{k} + "\n") 
}

$ bds z.bds
Key : Bonjour    Value : Au revoir
Key : Hello    Value : Bye
Key : Hola    Value : Adios
```

### Classes
Bds has some basic object oriented model that help to modularize complex data analysis pipelenes.
Has you may expect, classes can contiain fields (class variables) and methods (class functions).
```
class A {
	string name
	int value
}
```

**`new` operator**
To create a new object you use the operator `new` followed by the class name and parameters for the constructor method.
A constructor method has the same name as the class and returns `void`.
If no constructor is provided in the class definition, a default (empty) method is created.
E.g.:
```
a := new A()    # Create object 'A' and invoke empty (default) constructor
```

Example of a constructor with parameters:
```
class A {
	int x

	# Constructor
	void A(int x) {
		this.x = x
	}
}

a := new A(42)
println "a: $a"
```
The output of this program would be (by default printing an object shows the fields):
```
a: { x: 42 }
```

**Inheritance**: A class can inherit from another class using `extends` keywors in the class definition. 

```
class A {
	int x
	void A(int x) { this.x = x }
}

class B extends A {
	int y=17
}

b := new B()
println "b: $b"
```
The output of this program is:
```
b: { x: 0, y: 17 }
```
