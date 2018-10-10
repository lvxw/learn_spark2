#!/bin/bash
###############################################################################
#Script:        wordCount_mr.sh
#Author:        吕学文<2622478542@qq.com>
#Date:          2018-10-08
#Description:
#Usage:         wordCount_mr.sh
#Jira:
###############################################################################

#设置脚本运行环境和全局变量
function set_env(){
    cd `cd $(dirname $0)/../.. && pwd`
    source etc/sparkML_python.conf
    source etc/template/common.conf

}

#设置日、周、月的数据输入、输出路径
function init(){
    root_path=`pwd`

    export PYTHONPATH=${root_path}/:${SPARK2_INSTALL}/python/
    export SPARK_HOME=${SPARK2_INSTALL}

    app_name='app_name=wordCount'
    input_dir='input_dir=/tmp/lvxw/1'
    output_dir='output_dir=/tmp/lvxw/bbbbbbbbbbbbbb'

    $HADOOP_INSTALL/bin/hadoop fs -rm -r -f /tmp/lvxw/bbbbbbbbbbbbbb
}

function execute_mr(){

    $SPARK2_INSTALL/bin/spark-submit \
    --py-files ${root_path}/files/sharedFile.zip \
    --master yarn \
    --deploy-mode cluster \
    ${root_path}/com/test/business/template/wordCount.py ${RUN_PATTERN} ${app_name} ${input_dir} ${output_dir}
}

set_env
init
execute_mr