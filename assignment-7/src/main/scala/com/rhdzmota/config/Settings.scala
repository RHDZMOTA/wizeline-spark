package com.rhdzmota.config

import com.typesafe.config.{Config, ConfigFactory}

object Settings {
  private val app: Config = ConfigFactory.load().getConfig("application")
  object Spark {
    private val spark: Config = app.getConfig("spark")
    val name: String = spark.getString("name")
    object Label {
      private val label: Config = spark.getConfig("label")
      val master: String = label.getString("master")
    }
    object Value {
      private val value: Config = spark.getConfig("value")
      val master: String = value.getString("master")
    }
  }
  object Data {
    private val data: Config      = app.getConfig("data")
    val readStockOrders: String   = data.getString("readStockOrders")
    val readClientOrders: String  = data.getString("readClientOrders")
    val readClients: String       = data.getString("readClients")
    val write: String = data.getString("write")
  }
}
