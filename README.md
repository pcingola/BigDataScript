

# bds

A simple script-language for "Big Data" piplines.

Description and handbooks:

	http://pcingola.github.com/BigDataScript/


								Pablo Cingolani


# Install

Run the `scripts/install.sh` script

# Build Jar

### Option 1: Ant

Just run the ant command:
```
ant
```

### Option 2: Maven

These are used to build the JAR file

**Build executable jar with dependencies**

Jar with dependencias in directory 'target', e.g. `target/bds-2.3-jar-with-dependencies.jar`
```
mvn clean assembly:assembly
```


**Copy depdencies**

Libraries are copied to: `target/dependency`
```
mvn dependency:copy-dependencies
```

# Mesos

### Install
```
brew update
brew install mesos
```

Dir      : /usr/local/Cellar/mesos/0.22.1/
LibMesos : /usr/local/Cellar/mesos/0.22.1/lib/libmesos.dylib

### Install libraries

```
# Build mesos form source
wget http://www.apache.org/dist/mesos/0.22.1/mesos-0.22.1.tar.gz
tar -zxf mesos-0.22.1.tar.gz
./configure
make

# Copy libraries to BigDataScript/lib dir
cp src/java/target/mesos-0.22.1.jar ~/workspace/BigDataScript/lib/
cp src/java/target/protobuf-java-2.5.0.jar ~/workspace/BigDataScript/lib/

# Eclipse, 
#	Open Package explorer, go to 'lib' dir, right-click on the JAR files -> Build Path -> Add to build path
# 	Open Package explorer, go to "Referenced Libraries", right-click on "mesos-0.22.1.jar" -> Properties -> Native Libraries -> Add "/usr/local/Cellar/mesos/0.22.1/lib"
```

### Run Mesos master

```
/usr/local/sbin/mesos-master --registry=in_memory --ip=127.0.0.1
```

### Run Mesos slave

```
sudo /usr/local/sbin/mesos-slave --master=127.0.0.1:5050
```

Master's http server: `http://localhost:5050`


### Run Java framework 
***Exits after successfully running some tasks.***

```
./src/examples/java/test-framework 127.0.0.1:5050
```

# bds VM assembly

Some examples of 'bds vm' assembly

```
#--------------------------------------
# bds code:
#     a = 4
#--------------------------------------

pushi 4
load a
set
```

```
#--------------------------------------
# bds code:
#     a = 2 + 3
#--------------------------------------

pushi 2
pushi 3
addi
load a
set
```


```
#--------------------------------------
# bds code:
#     a[i] = b[j] + 7
#--------------------------------------
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


```
#--------------------------------------
# bds code:
#     a{'hi' + 1} = 'bye'
#--------------------------------------
            # Stack
pushs 'bye'
pushs 'hi'
pushi 1
adds        # 'hi1', 'bye'
load a      # a, 'hi1', 'bye'
refdict     # a{'hi1'}, 'bye'
set         # -
```


```
#--------------------------------------
# bds code:
#     z.a[7]{'hi'} = 42    
# where: z is an object
#--------------------------------------
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


```
#--------------------------------------
# bds code:
#     int f(int x) return x+1
#     z = f(7)
#--------------------------------------
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
