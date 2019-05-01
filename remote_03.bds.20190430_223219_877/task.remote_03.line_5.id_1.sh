#!/bin/bash -eu
set -o pipefail

cd '/home/pcingola/workspace/BigDataScript'
# Download commands
bds -download "http://pcingola.github.io/BigDataScript/index.html" "/tmp/bds/http/pcingola/github/io/BigDataScript/index.html"

# SYS command. line 5
cat /tmp/bds/http/pcingola/github/io/BigDataScript/index.html > tmp_remote_03.txt
# Checksum: 10b2633c
