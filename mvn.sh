#!/bin/bash

# Build package
mvn package

# Copy depdencies
# Libraries are copied to: target/dependency
mvn dependency:copy-dependencies

# Build executable jar with dependencies
# Jar with dependencias in directory 'target'
#     target/bds-2.3-jar-with-dependencies.jar
mvn clean assembly:assembly

# Create project template
mvn archetype:generate \
	-DgroupId=org.bds \
	-DartifactId=bds \
	-DarchetypeArtifactId=maven-archetype-quickstart \
	-DarchetypeVersion=1.4 \
	-DinteractiveMode=false
