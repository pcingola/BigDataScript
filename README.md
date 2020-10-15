

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

