#!/bin/bash -eu
set -o pipefail

cd '/Users/kqrw311/workspace/BigDataScript'
# Download commands
bds -download "http://pcingola.github.io/BigDataScript/index.html" "/tmp/bds/http/pcingola/github/io/BigDataScript/index.html"
bds -download "http://pcingola.github.io/BigDataScript/about.html" "/tmp/bds/http/pcingola/github/io/BigDataScript/about.html"

# SYS command. line 7
cat /tmp/bds/http/pcingola/github/io/BigDataScript/index.html /tmp/bds/http/pcingola/github/io/BigDataScript/about.html > tmp_remote_08.txt
# Checksum: 5b57de94
