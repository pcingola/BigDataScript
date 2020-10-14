#!/bin/bash

mvn archetype:generate \
	-DgroupId=org.bds \
	-DartifactId=bds \
	-DarchetypeArtifactId=maven-archetype-quickstart \
	-DarchetypeVersion=1.4 \
	-DinteractiveMode=false
