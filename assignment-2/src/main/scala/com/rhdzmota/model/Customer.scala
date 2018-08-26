package com.rhdzmota.model

object Customer {
  sealed trait Customer
  final case class ByGrossSpending(clientId: String, grossSpending: Double) extends Customer
  final case class ByOrders(clientId: String, ordersCount: Long) extends Customer
}
