#!/usr/bin/perl

#-------------------------------------------------------------------------------
# BDS generic cluster example
#
# This is a trivial example of the 'cluster generic' interface implementation.
# The commands implemented in this example do NOT really submit 
# tasks to a cluster, the tasks are run locally. 
# This is intended as a toy example and also used for test-cases.
#
# The following command is executed in order to get information of a recently 
# finished jobId. This information is typically used for debuging and it added 
# to bds's output.
#
# Script's output: 
#     The output is not parsed, it is stored and later shown 
#     in bds's report. Is should contain information relevant 
#     to the job's execution (e.g. "qstat -f $jobId" or 
#     "checkjob -v $jobId")
#
# Command line arguments: 
#     jobId: This is the jobId returned as the first line in 'clusterGenericRun' 
#           script (i.e. the jobID provided by the cluster management system)
#
#                                                                Pablo Cingolani
#-------------------------------------------------------------------------------

#---
# Parse command line arguments
#---
$jobId = shift @ARGV;

#---
# Execute cluster command to show task details
# Note: In this case the 'cluster' is just the localhost, so 
#       no information is available after a process dies
#---
print "No information avaialble on taks $jobId\n";

# OK
exit(0);

