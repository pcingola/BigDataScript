#!/usr/bin/perl

#-------------------------------------------------------------------------------
# BDS generic cluster example
#
# This is an example of the 'cluster generic' interface implementation for LSF.
# The commands implemented in this example simply pass the propper arguments 
# to bsub
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
#
# LSF Implementation: Rick Farouni
#-------------------------------------------------------------------------------

use POSIX;
use IPC::Open2;

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
$cmd = join(' ', @ARGV);

# Now @ARGS contains the command line to execute in the cluster
$cmd = join(' ', @ARGV);

$qsub = "bsub ";

if( $cpus > 0 ) {
	$qsub .= "-n $cpus ";
}

if( $mem > 0 ) {
	$mem = ceil($mem/1000000); # MB
	$qsub .= "-R rusage[mem=$mem] ";
}

if( $timeout > 0 ) {
	$timeout = ceil($timeout/60); # minute
	$qsub .= "-W $timeout ";
}

if ( $queue ne "" ) {
	$qsub .= "-q $queue "
}

$pid = open2(QSUB_IN, QSUB_OUT, " $qsub $cmd ");
@result_split = split('<|>', <QSUB_IN>); # creates an @answer array
print @result_split[1] . "\n" ;
close QSUB_OUT;
close QSUB_IN;

# OK
exit(0);

