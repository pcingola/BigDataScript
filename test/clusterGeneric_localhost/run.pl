#!/usr/bin/perl

#-------------------------------------------------------------------------------
# BDS generic cluster example
#
# This is a trivial example of the 'cluster generic' interface implementation.
# The commands implemented in this example do NOT really submit 
# tasks to a cluster, the tasks are run locally. 
# This is intended as a toy example and also used for test-cases.
#
# The script is called when a task is submitted to the cluster
#
# Script's output:
#     The script MUST print the cluster's jobID AS THE FIRST LINE. 
#     Make sure to flush STDOUT to avoid other lines to be printed out of order.
#
# Command line arguments:
#     1) Task's timeout in seconds. Negative number means 'unlimited'
#     2) Tasks required CPUs: number of cores within the same node.
#     3) Task's required memory in bytes. Negative means 'unspecified'
#     4) Cluster's queue name. Empty means "use cluster's default"
#     5) Cluster's STDOUT redirect file.
#     6) Cluster's STDERR redirect file.
#     7) Cluster command and arguments to be executed (typically is a "bds -exec ...").
#
#                                                                Pablo Cingolani
#-------------------------------------------------------------------------------

#---
# Parse command line arguments
#---
$timeout = shift @ARGV;
$cpus = shift @ARGV;
$mem = shift @ARGV;
$queue = shift @ARGV;
$saveStdout = shift @ARGV;
$saveStderr = shift @ARGV;

# Now @ARGS contains the command line to execute in the cluster
$cmd = join(' ', @ARGV);

#---
# Fist line MUST show PID
#
# Note: We should be showing the command's PID instead 
#       of this scripts's PID (but this is a simplified example).
#
# Note: Just to show that the command is executed here, we prepend
#       CLUSTERGENERIC_LOCALHOST to the PID
#---
print "CLUSTERGENERIC_LOCALHOST_$$\n\n";

#---
# Execute command. 
#
# Note: In this case the 'cluster' is just the localhost, so
#       we ignore all resources and run the command
#
# Note: We should be checking the exit value and setting this 
#       script's exit value accordingly (but this is a simplified 
#       example)
#---
system "$cmd";

# OK
exit(0);

