#!/bin/sh -e


cd `dirname $0`
dir=`pwd -P`
cd -

$dir/install.sh

cd $HOME
tar -cvzf $dir/../distro/bds_`uname`.tgz .bds


