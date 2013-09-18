#!/bin/sh

# Make
./scripts/make.sh

# Copy to cluster
scp -C -r $HOME/.bds/bds $HOME/.bds/include eq8302@ehs.grid.wayne.edu:.bds/

