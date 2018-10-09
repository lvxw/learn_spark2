# -*- coding: UTF-8 -*-

import shutil
import os


def deleteDir(path):
    shutil.rmtree(path, ignore_errors=True)


def findRootPath():
    cur_path = os.path.abspath(os.path.dirname(__file__)).replace("\\", "/")
    root_path = cur_path[:cur_path.find("sparkML_python/")+len("sparkML_python/")]
    return root_path
