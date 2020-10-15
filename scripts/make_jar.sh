#1/bin/bash -eu
set -o pipefail

VERSION="2.3"

cd bds

mvn clean compile assembly:assembly

