#!/bin/bash -eu
set -o pipefail

cd '/Users/kqrw311/workspace/BigDataScript'
# Download commands
bds -download "https://az-ngs-seqauto-test.s3.us-east-2.amazonaws.com/tmp/bds/remote_34/in" "/tmp/bds/s3/az_ngs_seqauto_test/tmp/bds/remote_34/in"

# SYS command. line 14
echo "RUNNING TASK"
# SYS command. line 15
cat s3://az-ngs-seqauto-test/tmp/bds/remote_34/in > /var/folders/s9/y0bgs3l55rj_jkkkxr2drz4157r1dz/T//remote_34.out
# SYS command. line 16
echo "OUTPUT:"
# SYS command. line 17
cat /var/folders/s9/y0bgs3l55rj_jkkkxr2drz4157r1dz/T//remote_34.out
# SYS command. line 18
echo
# Checksum: e71af4db
