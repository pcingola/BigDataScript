#!/bin/sh

killall sleep 
killall bds 

./scripts/make.sh 
bds test/z.bds

