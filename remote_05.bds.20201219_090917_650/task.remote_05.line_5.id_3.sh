#!/bin/bash -eu
set -o pipefail

cd '/Users/kqrw311/workspace/BigDataScript'
# Download commands
bds -download "http://pcingola.github.io/BigDataScript/index.html" "/tmp/bds/http/pcingola/github/io/BigDataScript/index.html"

# SYS command. line 5
cat '/tmp/bds/http/pcingola/github/io/BigDataScript/index.html' > tmp_remote_05.txt
# Checksum: 3ed2e22
