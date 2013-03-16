#!/bin/sh

git add .
git commit -a -m "Html pages updated"
git push origin gh-pages

echo "Pages:"
echo "   http://pcingola.github.com/BigDataScript/"
