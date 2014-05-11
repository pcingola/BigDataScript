#!/usr/bin/env bash

echo SCRIPT test-executor

# This script uses MESOS_SOURCE_DIR and MESOS_BUILD_DIR which come
# from configuration substitutions.
MESOS_SOURCE_DIR=/home/pcingola/tools/mesos
MESOS_BUILD_DIR=/home/pcingola/tools/mesos

# Locate Java from environment or use configure discovered location.
JAVA_HOME=${JAVA_HOME-/usr/lib/jvm/java-8-oracle}
JAVA=${JAVA-${JAVA_HOME}/bin/java}

# Use colors for errors.
. ${MESOS_SOURCE_DIR}/support/colors.sh

PROTOBUF_JAR=${MESOS_BUILD_DIR}/protobuf-2.5.0.jar

test ! -e ${PROTOBUF_JAR} && \
  echo "${RED}Failed to find ${PROTOBUF_JAR}${NORMAL}" && \
  exit 1

MESOS_JAR=${MESOS_BUILD_DIR}/src/mesos-0.18.0.jar

test ! -e ${MESOS_JAR} && \
  echo "${RED}Failed to find ${MESOS_JAR}${NORMAL}" && \
  exit 1

EXAMPLES_JAR=${MESOS_BUILD_DIR}/src/examples.jar

test ! -e ${EXAMPLES_JAR} && \
  echo "${RED}Failed to find ${EXAMPLES_JAR}${NORMAL}" && \
  exit 1

ECLIPSE_BIN=$HOME/workspace/BigDataScript/bin

exec ${JAVA} -cp ${PROTOBUF_JAR}:${MESOS_JAR}:${ECLIPSE_BIN} \
  -Djava.library.path=${MESOS_BUILD_DIR}/src/.libs \
  ca.mcgill.mcb.pcingola.bigDataScript.mesos.BdsMesosExecutor "${@}"
