#!/bin/sh

# Delete temp files
rm -rvf \
	grammars/output/classes \
	grammars/*class \
	checkpoint_*.bds.* \
	run*.bds.* \
	z.bds.* \
	cluster101_20* \
	bds.pid.* \
	*.report.html \
	in.txt \
	out?.txt \
	z.pid \
	test/*chp \
	failOnce.* \
	test/tmp*.txt \
	tmp*.txt

