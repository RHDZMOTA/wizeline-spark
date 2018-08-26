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
    private val data: Config = app.getConfig("data")
    val read: String  = data.getString("read")
    object Write {
      private val write: Config = data.getConfig("write")
      object Product {
        private val product: Config = write.getConfig("product")
        val byGrossSales: String = product.getString("byGrossSales")
        val byOrders: String = product.getString("byOrders")
      }
      object Customer {
        private val customer: Config = write.getConfig("customer")
        val byGrossSpending: String = customer.getString("byGrossSpending")
        val byOrders: String = customer.getString("byOrders")
      }
    }
  }
}
