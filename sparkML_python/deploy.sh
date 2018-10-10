#!/bin/bash
###############################################################################
#Script:        deploy.sh
#Author:        吕学文<2622478542@qq.com>
#Date:          2018-10-10
#Description:   将项目打成sparkML_python-yyyyMMdd.tar.gz
#Usage:
#Jira:
###############################################################################

#设置脚本运行环境和全局变量
function set_env(){
    cd `cd $(dirname $0) && pwd`
}

function init(){
    data_date=`date  +%Y%m%d`

    rm -rf target
    mkdir -p target/sparkML_python
}

function execute(){
    cp -r bin target/sparkML_python/
    cp -r etc target/sparkML_python/
    cp -r files target/sparkML_python/
    cp -r src/* target/sparkML_python/

    if [[ $# -eq 0 ]]
    then
        rm -rf target/sparkML_python/etc/sparkML_python.conf
        cp -rp target/sparkML_python/etc/sparkML_python.conf.test target/sparkML_python/etc/sparkML_python.conf
    else
        if [[ $1 = 'test' ]]
        then
            cp -rp target/sparkML_python/etc/sparkML_python.conf.test target/sparkML_python/etc/sparkML_python.conf
        elif [[ $1 = 'product' ]]
        then
            cp -rp target/sparkML_python/etc/sparkML_python.conf.product target/sparkML_python/etc/sparkML_python.conf
        fi
    fi

    cd target
    find . -type f -exec dos2unix {} \; > /dev/null

    cd sparkML_python
    zip -r files/sharedFile.zip com
    cd ../

    tar -zcvf sparkML_python-${data_date}.tar.gz sparkML_python
}

set_env
init
execute $*