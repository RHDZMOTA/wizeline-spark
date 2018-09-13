package com.rhdzmota.config

import org.apache.spark.sql.SparkSession

trait Context {
  val spark: SparkSession = SparkSession.builder
    .appName(Settings.Spark.name)
    .config(Settings.Spark.Label.master, Settings.Spark.Value.master)
    .getOrCreate()
}
