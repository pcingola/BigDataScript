#!/bin/sh

nodes=`cat /etc/hosts | tr -s " " | cut -f 2 -d " " | grep "^node" | tr "\n" " "`

#---
# Get data
#---

#wget http://www.mcb.mcgill.ca/~pcingola/bds_pipeline.tgz
#tar -xvzf bds_pipeline.tgz

#---
# Mount /bdsdata partition
#---

# for n in $nodes
# do
# 	echo Node: $n
# 
# 	echo "/bdsdata $n(async,no_root_squash,no_subtree_check,rw)" >> /etc/exports
# 	exportfs -a
# 
# 	ssh $n mkdir /bdsdata
# 	ssh $n "mount master:/bdsdata /bdsdata"
# done

#---
# Install golang
#---
yes | apt-get install golang

#---
# Install Java 7 
#---

yes | add-apt-repository ppa:webupd8team/java
yes | apt-get update
yes | apt-get install oracle-java7-installer

for n in $nodes
do
	yes | ssh $n add-apt-repository ppa:webupd8team/java
	yes | ssh $n apt-get update
	yes | ssh $n apt-get install oracle-java7-installer
done

#---
# Build bds
#---
cd /bdsdata
mkdir workspace
cd workspace
git clone https://github.com/pcingola/BigDataScript.git
cd BigDataScript/
./scripts/install.sh

cp -rvf $HOME/.bds /home/sgeadmin/
chown -Rvf sgeadmin: /home/sgeadmin/.bds

cd /home/sgeadmin/.bds
ln -s /home/sgeadmin/workspace/BigDataScript/bds.config

#---
# Add PATH to bash_profile
#---
echo 'export PATH=$PATH:$HOME/tools/samtools'            >> $HOME/.bash_profile
echo 'export PATH=$PATH:$HOME/tools/samtools/bcftools/'  >> $HOME/.bash_profile
echo 'export PATH=$PATH:$HOME/tools/bwa'                 >> $HOME/.bash_profile
echo 'export PATH=$PATH:$HOME/tools/tabix'               >> $HOME/.bash_profile
echo 'export PATH=$PATH:$HOME/.bds/'                     >> $HOME/.bash_profile

cp .bash_profile /home/sgeadmin/
chown sgeadmin: /home/sgeadmin/.bash_profile

#---
# Create links
#---
cd /home/sgeadmin/
ln -s /bdsdata/bds_pipeline
ln -s /bdsdata/snpEff
ln -s /bdsdata/tools
ln -s /bdsdata/workspace

#---
# Change owner
#---
cd /bdsdata
chown -Rvf sgeadmin: .

