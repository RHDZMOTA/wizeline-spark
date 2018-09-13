package com.rhdzmota

import com.rhdzmota.config.{Context, Settings}
import com.rhdzmota.util.Time
import org.apache.spark.sql._
import org.apache.spark.sql.functions._

object App extends Context {
  import spark.implicits._

  val readPath: String  = Settings.Data.read
  val writePath: String = Settings.Data.write

  val loadData: Dataset[Row] = spark.read.json(readPath).cache()

  def partitionSizeExercise(): Unit = {
    /**
      * Write the size of each partition obtained by default after reading the dataset in the folder:
      * gs://de-training-output-<username>/assignment-5/default-partitions
      */
    val name1 = "default-partitions-2"
    Time.printExecutionTime({
      loadData.mapPartitions(iter => Seq(iter.size).iterator)
        .orderBy(desc("value"))
        .write.csv(writePath + name1)
    }, name1)



    /**
      * Partition the dataset by client_id and write the size of the partitions in the folder:
      * gs://de-training-output-<username>/assignment-5/client_id-partitions
      */
    val name2 = "client_id-partitions-1"
    Time.printExecutionTime({
      loadData.repartition($"client_id").mapPartitions(iter => Seq(iter.size).iterator)
        .orderBy(desc("value"))
        .write.csv(writePath + name2)
    }, name2)


    /**
      * Partition the dataset by 15 partitions and write the size of the partitions in the folder:
      * gs://de-training-output-<username>/assignment-5/fifteen-partitions
      */
    val name3 = "fifteen-partitions"
    Time.printExecutionTime({
      loadData.repartition(15).mapPartitions(iter => Seq(iter.size).iterator)
        .orderBy(desc("value"))
        .write.csv(writePath + name3)
    }, name3)


    /**
      * Partition the dataset by 15 partition and client_id and write the size of the partitions in the
      * folder: gs://de-training-output-<username>/assignment-5/fifteen-client_id-partitions
      */
    val name4 = "fifteen-client_id-partitions"
    Time.printExecutionTime({
      loadData.repartition(15, $"client_id").mapPartitions(iter => Seq(iter.size).iterator)
        .orderBy(desc("value"))
        .write.csv(writePath + name4)
    }, name4)

    /**
      * Partition the dataset by 15 partition and product_id and month and write the size of the
      * partitions in the folder:
      * gs://de-training-output-<username>/assignment-5/fifteen-product_id-month-partitions
      */
    val name5 = "fifteen-product_id-month-partitions"
    Time.printExecutionTime({
      loadData.repartition(15, $"client_id", month($"timestamp"))
        .mapPartitions(iter => Seq(iter.size).iterator)
        .orderBy(desc("value"))
        .write.csv(writePath + name5)
    }, name5)


  }

  def partitionFilesExercise(): Unit = {
    /**
      * Repartition the output to generate 10 files and write them to the folder:
      * gs://de-training-output-<username>/assignment-5/10-files
      */
    val name1 = "10-files"
    Time.printExecutionTime({
      loadData.repartition(10).write.csv(writePath + name1)
    }, name1)

    /**
      * Repartition the output to generate 150 files and write them to the folder:
      * gs://de-training-output-<username>/assignment-5/150-files
      */
    val name2 = "150-files"
    Time.printExecutionTime({
      loadData.repartition(150).write.csv(writePath + name2)
    }, name2)

    /**
      * Repartition the output to generate 800 files and write them to the folder:
      * gs://de-training-output-<username>/assignment-5/800-files
      */
    val name3 = "180-files"
    Time.printExecutionTime({
      loadData.repartition(800).write.csv(writePath + name3)
    }, name3)
  }

  def main(args: Array[String]): Unit = {
    partitionSizeExercise()
    partitionFilesExercise()
    spark.stop()
  }
}
