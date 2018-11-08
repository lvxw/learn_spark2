package com.test.common

import com.hadoop.mapreduce.LzoTextInputFormat
import com.test.util.{FileUtils, ParamUtils}
import org.apache.hadoop.io.{LongWritable, Text}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable.ArrayBuffer

class BaseProgram extends App {
  lazy val paramMap:Map[String,String] = ParamUtils.jsonStrToMap(args.mkString(" ")).asInstanceOf[Map[String,String]]
  var runPattern:String = _
  var inputDir:String = _
  var outputDir:String = _
  var dataDate:String = _
  val appName = this.getClass.getSimpleName.replace("$", "")

  val runPatternList = List("local","test","public")
  var sparkConf:SparkConf = _

  def init():Unit ={
    System.setProperty("scala.time","")
    delayedInit(() => sparkSession.stop())
  }

  def initParams():Unit ={
    runPattern = paramMap.getOrElse("run_pattern","")
    inputDir = paramMap.getOrElse("input_dir","")
    outputDir = paramMap.getOrElse("output_dir","")
    dataDate = paramMap.getOrElse("data_date","")
  }

  def initSparkConf():Unit = {
    sparkConf = new SparkConf()
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
//      .set("spark.kryo.registrationRequired", "true")
      .setAppName(appName)
    if(runPattern == runPatternList(0)){
      sparkConf.setMaster("local[*]")
    }
    setSerializedClass()
  }

  def getInitRDD(sc:SparkContext,inputDir:String):RDD[String]={
    if(runPattern == runPatternList(2)){
      sc.newAPIHadoopFile(inputDir,  classOf[LzoTextInputFormat], classOf[LongWritable], classOf[Text]).map{value =>
        value._2.toString
      }
    }else{
      sc.textFile(inputDir)
    }
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

  /**
    * 子类必须重写该方法，此处只是示例
    */
  def setSerializedClass():Unit = {
    sparkConf.registerKryoClasses(Array(classOf[String],classOf[Int]))
  }

  init()
  initParams()
  initSparkConf()
  val sparkSession = SparkSession.builder().config(sparkConf).getOrCreate()
  FileUtils.deleteHdfsDir(sparkSession,outputDir)
}
