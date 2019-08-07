#!/bin/bash -eu
set -o pipefail

cd '/home/pcingola/workspace/BigDataScript'
# Download commands
bds -download "http://pcingola.github.io/BigDataScript/index.html" "/tmp/bds/http/pcingola/github/io/BigDataScript/index.html"
bds -download "http://pcingola.github.io/BigDataScript/about.html" "/tmp/bds/http/pcingola/github/io/BigDataScript/about.html"

# SYS command. line 7
cat http://pcingola.github.io/BigDataScript/index.html http://pcingola.github.io/BigDataScript/about.html > tmp_remote_07.txt
# Checksum: 20d609d
