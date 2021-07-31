#!/usr/bin/python3

import os

target = "res/web"
target_len_inc = len(target) + 1

for curdir, _, files in os.walk(target):
    curdir = curdir[target_len_inc:]
    print("dirs.add(\"{}\");".format(curdir))
    for file in files:
        print("files.add(\"/{}/{}\");".format(curdir,file))
