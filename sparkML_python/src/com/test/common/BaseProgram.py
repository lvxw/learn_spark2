# -*- coding: UTF-8 -*-

from pyspark.sql import SparkSession
from pyspark import SparkConf
from com.test.util.FileUtils import findRootPath
from com.test.util.FileUtils import deleteDir


class BaseProgram:
    rootPath = findRootPath()
    paramDir = {}
    runPattern = ["local", "test", "public"]
    isLocalRunPattern = None
    sparkConf = None
    sparkSession = None

    def __init__(self, arg_arr):
        for i in range(1, len(arg_arr)):
            kv = arg_arr[i].split("=")
            self.paramDir[kv[0]] = kv[1]
        self.isLocalRunPattern = self.paramDir['run_pattern'] == self.runPattern[0]
        if self.isLocalRunPattern:
            self.paramDir['input_dir'] = self.rootPath+self.paramDir['input_dir']
            self.paramDir['output_dir'] = self.rootPath+self.paramDir['output_dir']

    def getSparkConf(self):
        conf = SparkConf().setAppName(self.paramDir['app_name'])
        if self.isLocalRunPattern:
            conf.setMaster("local[*]")
            deleteDir(self.paramDir['output_dir'])

        self.sparkConf = conf

    def getSparkSession(self):
        session = SparkSession.builder.config(conf=self.sparkConf).getOrCreate
        self.sparkSession = session
