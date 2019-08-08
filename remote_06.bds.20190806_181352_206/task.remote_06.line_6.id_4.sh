#!/bin/bash -eu
set -o pipefail

cd '/Users/pcingola/workspace/BigDataScript'
# Download commands
bds -download "http://pcingola.github.io/BigDataScript/index.html" "/tmp/bds/http/pcingola/github/io/BigDataScript/index.html"
bds -download "http://pcingola.github.io/BigDataScript/about.html" "/tmp/bds/http/pcingola/github/io/BigDataScript/about.html"

# SYS command. line 6
cat http://pcingola.github.io/BigDataScript/index.html http://pcingola.github.io/BigDataScript/about.html > tmp_remote_06.txt
# Checksum: 979d6944
