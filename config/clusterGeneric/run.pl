#!/usr/bin/perl

#-------------------------------------------------------------------------------
# BDS generic cluster example
#
# This is a trivial example of the 'cluster generic' interface implementation.
# The commands implemented in this example simply pass the propper arguments 
# to qsub, qdel or qstat commands.
# This is intended as a toy example, since bds can do this directly (but 
# it's a good starting point to extend your own implementation).
#
# The script is called when a task is submitted to the cluster
#
# Script's output:
#     The script MUST print the cluster's jobID AS THE FIRST LINE. 
#     Make sure to flush STDOUT to avoid other lines to be printed out of order.
#
# Command line arguments:
#     1) Task's timeout in seconds. Negative number means 'unspecified'
#     2) Tasks required CPUs: number of cores within the same node.
#     3) Task's required memory in bytes. Negative means 'unspecified'
#     4) Cluster's queue name. Empty means "use cluster's default"
#     5) Cluster's STDOUT redirect file.
#     6) Cluster's STDERR redirect file.
#     7) Cluster command and arguments to be executed
#
#                                                                Pablo Cingolani
#-------------------------------------------------------------------------------

#---
# Parse command line arguments
#---
die "Error: Missing arguments.\nUsage: run.pl timeout cpus mem queue saveStdout saveStderr cmd arg1 ... argN\n" if $#ARGV < 6 ;

$timeout = shift @ARGV;
$cpus = shift @ARGV;
$mem = shift @ARGV;
$queue = shift @ARGV;
$saveStdout = shift @ARGV;
$saveStderr = shift @ARGV;

# Now @ARGS contains the command line to execute in the cluster
$cmd = join(' ', @ARGV);

#---
# Create command line arguments for qsub
#---

# Resources
$res = "";

if( $cpus > 0 ) {
	$res .= "nodes=1:ppn=$cpus";
}

if( $mem > 0 ) { 
	$res .= "," if $res ne '';
	$res .= "mem=$mem"; 
}

if( $timeout > 0 ) { 
	$res .= "," if $res ne '';
	$res .= "walltime=$timeout";
}

$qsub = "qsub ";
$qsub .= "-q $queue" if( $queue ne '' );
$qsub .= "-l $res" if( $res ne '' );

#---
# Execute 'qsub' command
#---
$pid = open QSUB, "| $qsub";
die "Cannot run command '$qsub'\n" if ! kill(0, $pid); # Check that process exists
print QSUB "$cmd\n";	# Send cluster's task via qsub's STDIN
close QSUB;

# OK
exit(0);

