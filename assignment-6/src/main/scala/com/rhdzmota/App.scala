package com.rhdzmota

import com.rhdzmota.config.{Context, Settings}
import org.apache.spark.sql.{Dataset, Row}
import org.apache.spark.sql.expressions.{Window, WindowSpec}
import org.apache.spark.sql.functions._

object App extends Context {
  import spark.implicits._

  val readPath: String  = Settings.Data.read
  val writePath: String = Settings.Data.write

  val orders: Dataset[Row] = spark.read.json(readPath).cache()
  val products: Dataset[Row] = orders
    .groupBy(
      year($"timestamp").alias("year"),
      weekofyear($"timestamp").alias("week"),
      substring($"id",0,2).alias("category"),
      $"product_id")
    .agg(
      sum($"total").alias("total"),
      sum($"quantity").alias("quantity")).cache()

  val topMostProducts: Dataset[Row] = {
    val windowDesc: WindowSpec = Window
      .partitionBy($"year", $"week", $"category")
      .orderBy(desc("total"))

    val orderedProductsDesc: Dataset[Row] = products.withColumn("order", row_number over windowDesc)
    orderedProductsDesc.filter($"order" <= 5).repartition($"category").cache
  }

  val top5Most: Dataset[Row] = topMostProducts
      .groupBy($"year", $"week".alias("week_num"), $"category".alias("prod_cat"))
      .agg(sum($"quantity").alias("total_qty_top5"), sum($"total").alias("total_spent_top5"))

  val topLeastProducts: Dataset[Row] = {
    val windowAsc = Window.partitionBy($"year", $"week", $"category").orderBy(asc("total"))
    val orderedProductsAsc: Dataset[Row] = products.withColumn("order", row_number over windowAsc)
    orderedProductsAsc.filter($"order" <= 5).repartition($"category").cache
  }

  val top5Least: Dataset[Row] = topLeastProducts
      .groupBy($"year", $"week".alias("week_num"), $"category".alias("prod_cat"))
      .agg(sum($"quantity").alias("total_qty_top5"), sum($"total").alias("total_spent_top5"))


  val top5All: Dataset[Row] = {
    val topMostProductsConcat: Dataset[Row] = topMostProducts
      .groupBy($"category")
      .pivot("order")
      .agg(first("product_id"))
      .withColumn(
        "top5_most",
        concat_ws(";", $"1", $"2", $"3", $"4", $"5"))
      .drop("1","2","3","4","5")

    val topLeastProductsConcat: Dataset[Row] = topLeastProducts
      .groupBy($"category")
      .pivot("order")
      .agg(first("product_id"))
      .withColumn(
        "top5_least",
        concat_ws(";", $"1", $"2", $"3", $"4", $"5"))
      .drop("1","2","3","4","5")

    val categoryTotals: Dataset[Row] = orders
      .groupBy(substring($"id",0,2).alias("category"))
      .agg(sum($"total").alias("total_spent"), sum($"quantity").alias("total_qty_cat")).cache

    val allTotals: Dataset[Row] = categoryTotals
      .join(topMostProductsConcat, "category")
      .join(topLeastProductsConcat, "category")

    val allTotalsOrdered: Dataset[Row] = allTotals
      .select($"category",$"top5_most", $"top5_least", $"total_qty_cat", $"total_spent")

    allTotalsOrdered
  }

  def main(args: Array[String]): Unit = {

    top5Most.coalesce(1).write
      .option("codec", "org.apache.hadoop.io.compress.GzipCodec")
      .csv(writePath + "top-5-most")

    top5Least.coalesce(1).write
      .option("codec", "org.apache.hadoop.io.compress.GzipCodec")
      .csv(writePath + "top-5-least")

    top5All.coalesce(1).write
      .option("codec", "org.apache.hadoop.io.compress.GzipCodec")
      .csv(writePath + "top-5-all")

    spark.stop()
  }
}
