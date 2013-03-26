#!/bin/sh -e

./scripts/clean.sh

git add .
git commit -a -m "Project updated"
git push origin

