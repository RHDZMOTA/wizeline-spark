package com.rhdzmota

import com.rhdzmota.config.{Context, Settings}
import org.apache.spark.sql._
import com.rhdzmota.model._

object Alimazon extends Context {
  import spark.implicits._

  val readPath: String  = Settings.Data.read
  val writePath: String = Settings.Data.write

  def loadData: Dataset[AlimazonRow] =
      spark.sparkContext.textFile(readPath).toDS.map(AlimazonRow.fromString)

  def countOrdersByProduct: Dataset[Product] =
    loadData.groupByKey(row => row.productId).count.map({case (key, value) => Product(key, value)})

  def main(args: Array[String]): Unit = {
    val result: Dataset[Product] = countOrdersByProduct.orderBy($"orders".desc).cache()
    result.show()
    result.coalesce(1).write.csv(writePath)
    spark.stop()
  }
}
