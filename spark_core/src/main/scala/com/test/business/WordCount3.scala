package com.test.business

import com.test.common.BaseProgram
import org.apache.spark.internal.io.FileCommitProtocol.TaskCommitMessage

/**
  *
  * @param args (IDE 本地测试时，参数为：
        {
          \"input_dir\":\"spark_core/tmp/logs/test/1\",
          \"output_dir\":\"spark_core/tmp/out/test\",
          \"run_pattern\":\"local\"
        }
  */
object WordCount3 extends BaseProgram {

  private val rdd = sparkSession.sparkContext.textFile(inputDir,3)

  val re = rdd.flatMap(_.split(","))
    .mapPartitionsWithIndex { (index, it) =>
      println("分区："+index)
      var result = List[(String, Int)]()
      it.foreach { e =>
        result = result.::((e, 1))
      }
      result.toIterator
    }.groupByKey()
    .map{x =>
      (x._1,x._2.sum)
    }

  re.foreach(println(_))

  override def setSerializedClass():Unit = {
    val serializedClassArr:Array[Class[_]] = Array(classOf[Array[String]],classOf[TaskCommitMessage])
    sparkConf.registerKryoClasses(serializedClassArr)
  }
}
