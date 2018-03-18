#!/bin/bash -e

cd '/home/pcingola/workspace/BigDataScript'
# Download commands
bds -download "http://pcingola.github.io/BigDataScript/index.html" "/tmp/bds/http/pcingola/github/io/BigDataScript/index.html"
bds -download "http://pcingola.github.io/BigDataScript/about.html" "/tmp/bds/http/pcingola/github/io/BigDataScript/about.html"
 cat /tmp/bds/http/pcingola/github/io/BigDataScript/index.html /tmp/bds/http/pcingola/github/io/BigDataScript/about.html > tmp_remote_07.txt
# Checksum: 613352aa
