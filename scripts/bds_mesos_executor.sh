#!/usr/bin/env bash

#-------------------------------------------------------------------------------
#
# Mesos executor for bds
#
#
#															Pablo Cingolani 2015
#-------------------------------------------------------------------------------

#---
# Get BDS directories
#---
if [ "$USER" = "root" ]
then
	# Use /usr/local/bds path instead of /root/.bds
	BDS_HOME="/usr/local/bds"
elif [ -z "$HOME" ]
then
	# Home variable not set? Try using BASH_SOURCE
	BDS_HOME_MESOS=`dirname ${BASH_SOURCE[0]}`
	BDS_HOME=`dirname $BDS_HOME_MESOS`
else
	# Use default 'bds' dir
	BDS_HOME=$HOME/.bds
fi
BDS_MESOS_DIR=$BDS_HOME/mesos

# BDS executable is also a JAR file
BDS_JAR=$BDS_HOME/bds

#---
# Mesos libraries
#---
ls -al $BDS_MESOS_DIR
PROTOBUF_JAR=`ls $BDS_MESOS_DIR/protobuf*.jar | tail -n 1`
MESOS_JAR=`ls $BDS_MESOS_DIR/mesos*.jar | tail -n 1`

# Path to native library
if [ -z "$MESOS_NATIVE_LIBRARY" ]
then
	# Guess from bds' home dir
	MESOS_NATIVE_LIB_DIR="$BDS_HOME/lib"
else
	# Use path provided by mesos
	MESOS_NATIVE_LIB_DIR=`dirname $MESOS_NATIVE_LIBRARY`
fi


#---
# Check that libraries are installed
#---
echo "User                 :"`whoami`
echo "Script               :$0"
echo "BDS_HOME             :$BDS_HOME"
echo "BDS_MESOS_DIR        :$BDS_MESOS_DIR"
echo "BDS_JAR              :$BDS_JAR"
echo "MESOS_JAR            :$MESOS_JAR"
echo "MESOS_NATIVE_LIB_DIR :$MESOS_NATIVE_LIB_DIR"
echo "PROTOBUF_JAR         :$PROTOBUF_JAR"

test ! -e ${PROTOBUF_JAR} && \
  echo "Cannot find protobuf library ${PROTOBUF_JAR}" && \
  exit 1

test ! -e ${MESOS_JAR} && \
  echo "Cannot find mesos library ${MESOS_JAR}" && \
  exit 1

test ! -e ${MESOS_NATIVE_LIB_DIR} && \
  echo "Cannot find mesos native library directory ${MESOS_NATIVE_LIB_DIR}" && \
  exit 1

if [ ! "$(ls -A $MESOS_NATIVE_LIB_DIR)" ]; then
  echo "Cannot find mesos native library in directory ${MESOS_NATIVE_LIB_DIR}" && \
  exit 1
fi

#---
# Execute bds (Mesos) executor
#---
exec java -cp $PROTOBUF_JAR:$MESOS_JAR:$BDS_JAR \
  -Djava.library.path=$MESOS_NATIVE_LIB_DIR \
  ca.mcgill.mcb.pcingola.bigDataScript.mesos.BdsMesosExecutor "${@}"

