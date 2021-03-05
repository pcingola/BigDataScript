#!/bin/bash

for p in \
		index.html \
		manual/index.html \
		manual/404.html \
		manual/site/data_types/index.html \
		manual/site/wait/index.html \
		manual/site/index.html \
		manual/site/task_detached/index.html \
		manual/site/task_aws/index.html \
		manual/site/bds/index.html \
		manual/site/test_cases/index.html \
		manual/site/building_bds/index.html \
		manual/site/global_vars/index.html \
		manual/site/checkpoints/index.html \
		manual/site/404.html \
		manual/site/cmdline/index.html \
		manual/site/pipelines/index.html \
		manual/site/language/index.html \
		manual/site/about/index.html \
		manual/site/special/index.html \
		manual/site/bds_vm/index.html \
		manual/site/sys/index.html \
		manual/site/cleanup/index.html \
		manual/site/bdsconfig/index.html \
		manual/site/download/index.html \
		manual/site/task/index.html \
		manual/site/debugger/index.html \
		manual/site/remote_files/index.html \
		manual/site/functions/index.html \
		manual/site/search.html \
		manual/site/hello/index.html \
		manual/site/bdscmdline/index.html \
		manual/site/cloud_aws/index.html \
		manual/site/logging/index.html \
		manual/site/task_imp/index.html \
		manual/site/dep_op/index.html \
		manual/site/help/index.html \
		manual/site/goal/index.html \
		manual/site/par/index.html \
		manual/search.html 
	do
		echo "PAGE: '$p'"
		echo "<meta http-equiv=\"refresh\" content=\"0; URL=http://pcingola.github.com/bds/$p\" />" > $p
	done
