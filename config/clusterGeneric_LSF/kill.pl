#!/usr/bin/perl

#-------------------------------------------------------------------------------
# BDS generic cluster example
#
# This is an example of the 'cluster generic' interface implementation for LSF.
# The commands implemented in this example simply pass the propper arguments 
# to bkill.
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
#
# LSF Implementation: Rick Farouni
#-------------------------------------------------------------------------------

#---
# Parse command line arguments
#---
die "Error: Missing arguments.\nUsage: kill.pl jobId\n" if $#ARGV < 0 ;
$jobId = join(' ', @ARGV);

#---
# Execute cluster command to kill task
#---
$exitCode = system "bkill $jobId";

# OK
exit($exitCode);

