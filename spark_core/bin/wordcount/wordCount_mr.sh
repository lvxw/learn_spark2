#!/bin/bash
###############################################################################
#Script:        wordCount_mr.sh
#Author:        吕学文<lvxw@fun.tv>
#Date:          2018-10-08
#Description:
#Usage:         wordCount_mr.sh
#Jira:
###############################################################################

#设置脚本运行环境和全局变量
function set_env(){
    cd `cd $(dirname $0)/../.. && pwd`
    source bin/init_context_env.sh day $1
}

#设置日、周、月的数据输入、输出路径
function init(){
    hdfs_test_input=/tmp/lvxw/1

    hdfs_test_output=/tmp/lvxw/xxxxxxxxx
}

function execute_mr(){
    $SPARK2_INSTALL/bin/spark-submit --class com.test.business.WordCount \
    --master yarn \
    --deploy-mode cluster \
    --conf spark.default.parallelism=10 \
    --driver-memory 1G \
    --executor-memory 1G \
    --driver-cores 1 \
    --num-executors  2 \
    --executor-cores 2 \
    jar/SparkCore.jar \
    "{\"input_dir\":\"${hdfs_test_input}\", \
      \"output_dir\":\"${hdfs_test_output}\", \
      \"run_pattern\":\"${RUN_PATTERN}\"
    }"
}

set_env $1
init
execute_mr