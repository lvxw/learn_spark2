package com.test.util

import java.io.File

import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.sql.SparkSession

object FileUtils {
  def deleteDir(dir: File): Unit = {
    if(!dir.exists()){
      return
    }
    val files = dir.listFiles()
    files.foreach(f => {
      if (f.isDirectory) {
        deleteDir(f)
      } else {
        f.delete()
      }
    })
    dir.delete()
  }

  def deleteHdfsDir (sparkSession: SparkSession,outputDir:String):Unit= {
    val path = new Path(outputDir)
    val hadoopConf = sparkSession.sparkContext.hadoopConfiguration
    val hdfs = FileSystem.get(hadoopConf)
    if(hdfs.exists(path)){
      hdfs.delete(path,true)
    }
  }
}