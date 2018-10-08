package com.test.business

import com.test.common.BaseProgram
import org.apache.spark.sql.DataFrame

/**
  *
  * @param args (IDE 本地测试时，参数为：
        {
          \"input_dir\":\"spark_core/tmp/logs/test/1\",
          \"output_dir\":\"spark_core/tmp/out/test\",
          \"run_pattern\":\"local\"
        }
  */
object WordCount extends BaseProgram {

  private val dataFrame: DataFrame = readCsvToDataFrame(indexAndNameMap=Map(0 -> "name",2->"score"))

  dataFrame.createTempView("person")
  private val df2: DataFrame = sparkSession.sql("select * from person")
  df2.show()

  override def setSerializedClass():Unit = {
    val serializedClassArr:Array[Class[_]] = Array(classOf[Array[String]])
    sparkConf.registerKryoClasses(serializedClassArr)
  }
}
