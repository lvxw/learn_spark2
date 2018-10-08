package com.test.util

import org.apache.spark.sql.SparkSession
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path


object HdfsFileUtils {
 def deleteDir (sparkSession: SparkSession,outputDir:String):Unit= {
   val path = new Path(outputDir)
   val hadoopConf = sparkSession.sparkContext.hadoopConfiguration
   val hdfs = FileSystem.get(hadoopConf)
   if(hdfs.exists(path)){
     hdfs.delete(path,true)
   }
 }
}
