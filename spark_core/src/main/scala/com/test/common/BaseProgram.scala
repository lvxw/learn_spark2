package com.test.common

import com.test.util.StringUtils
import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SparkSession}

import scala.collection.mutable.ArrayBuffer

class BaseProgram extends App {
  lazy val paramMap:Map[String,Any] = StringUtils.jsonStrToMap(args)
  var runPattern:String = _
  var inputDir:String = _
  var outputDir:String = _
  var dataDate:String = _
  val appName = this.getClass.getSimpleName.replace("$", "")

  val runPatternList = List("local","test","public")
  var sparkConf:SparkConf = _

  def initParams():Unit ={
    runPattern = paramMap.getOrElse("run_pattern","").toString
    inputDir = paramMap.getOrElse("input_dir","").toString
    outputDir = paramMap.getOrElse("output_dir","").toString
    dataDate = paramMap.getOrElse("data_date","").toString
  }

  def initSparkConf():Unit = {
    sparkConf = new SparkConf()
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .set("spark.kryo.registrationRequired", "true")
      .setAppName(appName)
    if(runPattern == runPatternList(0)){
      sparkConf.setMaster("local[*]")
    }else if(runPattern == runPatternList(1)){
      System.setProperty("HADOOP_USER_NAME", "hadoop")
      sparkConf.setMaster("yarn-client")
      sparkConf.set("SPARK_HOME", "/home/hadoop/soft/spark2")
//      sparkConf.set("mapreduce.framework.name", "yarn")
//      sparkConf.set("yarn.resourcemanager.hostname", "artemis-02")
//      sparkConf.set("yarn.nodemanager.aux-services", "mapreduce_shuffle")
//      sparkConf.set("yarn.resourcemanager.address", "artemis-02:8032")
//      sparkConf.set("yarn.resourcemanager.scheduler.address", "artemis-02:8030")
//      sparkConf.set("yarn.resourcemanager.resource-tracker.address", "artemis-02:8035")
//      sparkConf.set("yarn.resourcemanager.admin.address", "artemis-02:8033")
//      sparkConf.set("yarn.resourcemanager.webapp.address", "artemis-02:8088")
//      sparkConf.setJars(List("spark_core/target/original-SparkCore.jar"))
//      sparkConf.set("spark.yarn.jars", "hdfs://artemis-02:9000/spark/jars/*.jar")
    }
    setSerializedClass()
  }

  def readCsvToDataFrame(inputDir:String,indexArr:Array[Int],fileNameArr:Array[String]): DataFrame ={
    val colArr = indexArr.map("_c"+_)
    sparkSession.read.csv(inputDir)
      .select(colArr(0),colArr.slice(1,colArr.size):_*)
      .toDF(fileNameArr:_*)
  }

  def readCsvToDataFrame(inputDir:String=inputDir,indexAndNameMap:Map[Int,String]): DataFrame ={
    val indexArrBuf = new ArrayBuffer[String]()
    val fieldNameArrBuf = new ArrayBuffer[String]()
    for((index,fieldName) <- indexAndNameMap){
      indexArrBuf += ("_c"+index)
      fieldNameArrBuf += fieldName
    }
    sparkSession.read.csv(inputDir)
      .select(indexArrBuf(0),indexArrBuf.slice(1,indexArrBuf.size):_*)
      .toDF(fieldNameArrBuf:_*)
  }

  def loadDataFromHive(databaseAndTable:String,fieldName:Array[String]): DataFrame ={
    fieldName.mkString(",")
    val hql = s"select ${fieldName.mkString(",")} from ${databaseAndTable}"
    sparkSession.sql(hql)
  }


  /**
    * 子类必须重写该方法，此处只是示例
    */
  def setSerializedClass():Unit = {
    sparkConf.registerKryoClasses(Array(classOf[String],classOf[Int]))
  }

  initParams()
  initSparkConf()
//  val sparkSession = SparkSession.builder().enableHiveSupport().config(sparkConf).getOrCreate()

  val sparkSession = SparkSession.builder().config(sparkConf).getOrCreate()
}
