#!/bin/sh
sacct --format=User,JobID,account,Timelimit,elapsed,ReqMem,MaxRss,ExitCode -j $1
