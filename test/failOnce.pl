#!/usr/bin/perl

$file = "failOnce.txt";
if( -e $file ) {
	print "OK\n";
	exit 0;
}

`touch $file`;
print "FAIL\n";
exit 1;
