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
object WordCount4 extends BaseProgram {

  private val rdd = sparkSession.sparkContext.textFile(inputDir,3)

  val re = rdd.flatMap(_.split(","))
  re.foreach(println(_))

  override def setSerializedClass():Unit = {
    val serializedClassArr:Array[Class[_]] = Array(classOf[Array[String]],classOf[TaskCommitMessage])
    sparkConf.registerKryoClasses(serializedClassArr)
  }
}
