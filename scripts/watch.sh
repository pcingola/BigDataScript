#!/bin/sh

watch -d "ps x -o  \"%p %P %r %y %x %a\" | grep -e sleep -e bds -e /bin/sh | grep -v grep"
