#!/bin/sh

# Delete temp files
rm -rvf \
	*.pid \
	*.html \
	*.dag.js \
	*.report.html \
	bds.pid.* \
	checkpoint_*.bds.* \
	cmdLineOptions_*.bds.* \
	failOnce.* \
	grammars/output/classes \
	grammars/*class \
	graph_*.bds.* \
	hs_err_pid*.log \
	in*.txt \
	remote*.bds.* \
	run*.bds.* \
	tmp*.txt \
	out*.txt \
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
	z.bds.* \
	`find . -iname "*.chp"` \
