package com.rhdzmota

import java.sql.Timestamp
import java.time.LocalDateTime

import com.rhdzmota.config.{Context, Settings}
import org.apache.spark.sql._
import org.apache.spark.sql.functions._
import com.rhdzmota.model._
import com.rhdzmota.extras.ops._

object App extends Context {
  import spark.implicits._
  import Math._
  import Time._

  val readPath: String  = Settings.Data.read

  val alimazon: Dataset[AlimazonRow] =
      spark.sparkContext.textFile(readPath).toDS.map(AlimazonRow.fromString).cache()

  val bestSellingHours: Dataset[HourRow] =
    alimazon.map(_.withHour).groupByKey(_.hour)
      .agg(
        mean("total").as[Double],
        min("total").as[Double],
        max("total").as[Double])
      .map({case (h, av, mn, mx) => HourRow(h, av.round(2), mn, mx)})
      .orderBy(asc("averageSpending"))

  val discount: Dataset[DiscountRow] =
    alimazon.filter(Timestamp.valueOf(LocalDateTime.now()).minusMonths(6) before _.timestamp)
      .groupByKey(_.productId)
      .agg(
        sum("quantity").alias("unitsSold").as[Long],
        count("quantity").alias("numberOrders").as[Long],
        sum("total").alias("totalSales").as[Double])
      .orderBy(desc("totalSales")).head(10).toSeq
      .map({case (id, unitsSold, numberOrders, totalSales) => DiscountRow(
        discount = 0.10,
        productId = id,
        totalProductSold = unitsSold,
        totalRegisteredSales = totalSales,
        totalOrders = numberOrders)}).toDS
      .orderBy(asc("totalProductSold"))

  val clientDistr: Dataset[(Int, Long, Long)] =
    alimazon.groupByKey(_.quantity)
      .agg(
        countDistinct("clientId").alias("clientsCount").as[Long],
        sum("quantity").alias("productsCount").as[Long]).cache()
      .orderBy(asc("clientsCount"))

  def main(args: Array[String]): Unit = {
    bestSellingHours.coalesce(1).write.csv(Settings.Data.Write.bestSellingHours)
    discount.coalesce(1).write.csv(Settings.Data.Write.discount)
    clientDistr.write.csv(Settings.Data.Write.clientDistr)
    spark.stop()
  }
}
