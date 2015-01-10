#!/usr/bin/perl

#-------------------------------------------------------------------------------
# BDS generic cluster example
#
# This is a trivial example of the 'cluster generic' interface implementation.
# The commands implemented in this example simply pass the propper arguments 
# to 'qsub'.
# This is intended as a toy example, since bds can do this directly (but 
# it's a good starting point to extend your own implementation).
#
# The script is called when a task is killed
#
# Script's output: 
#     None
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
# Execute cluster command to kill task
#---
`qdel $jobId`;

# OK
exit(0);

