#!/bin/sh -e

cd `dirname $0`
dir=`pwd -P`
cd -

# Install files
$dir/install.sh
cp -vf config/bds.config $HOME/.bds/	# Make sure default config file is used

cd $HOME
mkdir -p $dir/../distro/
tar -cvzf $dir/../distro/bds_`uname`.tgz .bds


