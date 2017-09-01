#!/usr/bin/env perl

#-------------------------------------------------------------------------------
# BDS SLURM example
#
# This is a trivial example of the 'cluster generic' interface implementation
# for SLURM. The commands implemented in this example simply pass the proper
# arguments to the SLURM sbatch command.
#
# The script is called when a task is submitted to the cluster.
#
# Script's output:
#     The script MUST print the cluster's job ID AS THE FIRST LINE.
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
#                                                                Andrew Perry
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
$jobName = "bds_" . $ARGV[0];

# SLURM default is time in minutes, we round up to the nearest minute
$timeout = int(($timeout + 0.5)/ 60);

# SLURM default is megabytes, we round up to the nearest megabyte be safe
$mem = int(($mem + 0.5)/ 1048576);

#---
# Create command line arguments for sbatch
#---

$sbatch = "sbatch --parsable --no-requeue --output $saveStdout --error $saveStderr --job-name=$jobName ";
$sbatch .= " --partition=$queue " if( $queue ne '' );

if( $cpus > 0 ) {
        $sbatch .= " --ntasks-per-node=$cpus ";
}

if( $mem > 0 ) {
        $sbatch .= " --mem=$mem ";
}

if( $timeout > 0 ) {
        $sbatch .= " --time=$timeout ";
}


#---
# Execute 'sbatch' command
#---
$pid = open SBATCH, "| $sbatch";
die "Cannot run command '$sbatch'\n" if ! kill(0, $pid); # Check that process exists
print SBATCH "#!/bin/sh\n$cmd\n";	# Send cluster's task via sbatch's STDIN
close SBATCH;

# OK
exit(0);

