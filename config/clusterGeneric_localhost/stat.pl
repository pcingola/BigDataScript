#!/usr/bin/perl

#-------------------------------------------------------------------------------
# BDS generic cluster example
#
# This is a trivial example of the 'cluster generic' interface implementation.
# The commands implemented in this example do NOT really submit 
# tasks to a cluster, the tasks are run locally. 
# This is intended as a toy example and also used for test-cases.
#
# This script is executed in order to show the jobID of all jobs currently 
# scheduled in the cluster
#
# Script's output: 
#     This script is expected to print all jobs currently scheduled or 
#     running in the cluster (e.g. qstat). One per line. The FIRST column 
#     should be the jobID (columns are spce or tab separated). Other 
#     columns may exists (but are currently ignored).
#
# Command line arguments: 
#     None
#
#                                                                Pablo Cingolani
#-------------------------------------------------------------------------------

#---
# Execute cluster command to show all tasks
# Note: In this example the 'cluster' is just localhost
#---
system "ps -a";

# OK
exit(0);

