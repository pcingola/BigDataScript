#!/usr/bin/env bash

echo SCRIPT test-executor

BDS_HOME=$HOME/.bds
BDS_MESOS_DIR=$BDS_HOME/mesos

# Java libraries
PROTOBUF_JAR=`ls $BDS_MESOS_DIR/protobuf*.jar | tail -n 1`
MESOS_JAR=`ls $BDS_MESOS_DIR/mesos*.jar | tail -n 1`

# BDS executable is also a JAR file
BDS_JAR=$BDS_HOME/bds

# Make sure you copied Mesos's native library here
MESOS_NATIVE_LIB="$BDS_HOME/lib"

# Check that libraries are installed
test ! -e ${PROTOBUF_JAR} && \
  echo "Cannot find protobuf library ${PROTOBUF_JAR}" && \
  exit 1

test ! -e ${MESOS_JAR} && \
  echo "Cannot find mesos library ${MESOS_JAR}" && \
  exit 1

test ! -e ${MESOS_NATIVE_LIB} && \
  echo "Cannot find mesos native library directory ${MESOS_NATIVE_LIB}" && \
  exit 1

if [ ! "$(ls -A $MESOS_NATIVE_LIB)" ]; then
  echo "Cannot find mesos native library in directory ${MESOS_NATIVE_LIB}" && \
  exit 1
fi

# Execute Bds executor
exec java -cp $PROTOBUF_JAR:$MESOS_JAR:$BDS_JAR \
  -Djava.library.path=$MESOS_NATIVE_LIB \
  ca.mcgill.mcb.pcingola.bigDataScript.mesos.BdsMesosExecutor "${@}"
