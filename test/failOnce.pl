#!/usr/bin/perl

$file = "failOnce.txt";
if( -e $file ) {
	print "failOnce.pl: OK\n";
	exit 0;
}

`touch $file`;
print "failOnce.pl: FAIL\n";
exit 1;
