# -*- coding: UTF-8 -*-

import sys
from com.test.common.BaseProgram import BaseProgram


'''
    IDE 本地测试时，参数为：
        run_pattern=local
        app_name=wordCount
        input_dir=tmp/logs/test/1
        output_dir=tmp/out/test
'''
if __name__ == '__main__':

    program = BaseProgram(sys.argv)
    program.getSparkConf()
    program.getSparkSession()

    df = program.sparkSession().read.csv(program.paramDir['input_dir']).toDF("name", "age", "score")
    df.registerTempTable("Student")

    result = program.sparkSession().sql("select name,age from Student limit 2")
    result.show()

    result.write.csv(program.paramDir['output_dir'])
    sys.exit()
