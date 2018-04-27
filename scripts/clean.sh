#!/bin/sh

# Delete temp files
rm -rvf \
	grammars/output/classes \
	grammars/*class \
	hs_err_pid*.log \
	checkpoint_*.bds.* \
	run*.bds.* \
	tmp*.txt \
	cmdLineOptions_*.bds.* \
	*.pid \
	z.bds.* \
	graph_*.bds.* \
	*.html \
	*.dag.js \
	cluster101_20* \
	bds.pid.* \
	*.report.html \
	in*.txt \
	out*.txt \
	failOnce.* \
	test/tmp*.txt \
	test/checkpoint_*.bds.* \
	test/run*.bds.* \
	test/tmp*.txt \
	test/cmdLineOptions_*.bds.* \
	test/*.pid \
	test/z.bds.* \
	test/*.html \
	test/*.dag.js \
	report_*.bds.20* \
	`find . -iname "*.chp"` \

