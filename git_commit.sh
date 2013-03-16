#!/bin/sh -e

./clean.sh

git add .
git commit -a -m "Project updated"
git push origin

