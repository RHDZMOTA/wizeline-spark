package com.rhdzmota

import com.rhdzmota.config.{Context, Settings}
import org.apache.spark.sql.{Column, Dataset, Row}
import org.apache.spark.sql.expressions.{Window, WindowSpec}
import org.apache.spark.sql.functions._

object App extends Context {
  import spark.implicits._

  private val readClientsPath: String  = Settings.Data.readClients
  private val readClientOrdersPath: String  = Settings.Data.readClientOrders
  private val readStockOrdersPath: String   = Settings.Data.readStockOrders
  val writePath: String = Settings.Data.write

  // country gender id name registration_date
  val clients: Dataset[Row] = spark.read.json(readClientsPath)
    .repartition($"id")
    .cache()

  // client_id id product_id quantity timestamp total
  val clientOrders: Dataset[Row]  = spark.read.json(readClientOrdersPath)
    .repartition($"client_id")
    .cache()

  // id product_id quantity timestamp total
  val stockOrders: Dataset[Row]   = spark.read.json(readStockOrdersPath)
    .withColumnRenamed("product_id", "pid")
    .withColumnRenamed("quantity", "stock_quantity")
    .withColumnRenamed("timestamp", "stock_timestamp")
    .withColumnRenamed("total", "stock_total")
    .cache()

  val clientOrdersAggregation: Dataset[Row] = clients.join(clientOrders,
    clients.col("id") ===  clientOrders.col("client_id"))
    .groupBy($"client_id")
    .agg(
      count($"client_id").alias("total_transactions"),
      sum($"total").alias("total_amount"),
      sum($"quantity").alias("total_products"))

  val bestClientsInfo: Dataset[Row] = clients.join(clientOrdersAggregation,
    clients.col("id") === clientOrdersAggregation.col("client_id"))
    .select(
    $"client_id", $"name", $"gender", $"country", $"registration_date", $"total_transactions",
    $"total_amount", $"total_products")
    .sort($"total_transactions".desc, $"total_amount".desc)

  val heathCheckReport: Dataset[Row] = clientOrders.join(stockOrders,
    clientOrders.col("product_id") === stockOrders.col("pid"))
    .groupBy($"product_id")
    .agg(
      sum($"stock_quantity").alias("total_bought"),
      sum($"quantity").alias("total_sold"))
    .withColumn(
      "is_valid",
      ((b: Column, s: Column) => s < b)($"total_bought", $"total_sold"))


  def main(args: Array[String]): Unit = {

    bestClientsInfo.write
        .csv(writePath + "best-clients-info")

    heathCheckReport.write
        .csv(writePath + "health-check")

    spark.stop()
  }
}
