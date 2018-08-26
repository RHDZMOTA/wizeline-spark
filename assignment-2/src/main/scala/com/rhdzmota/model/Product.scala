package com.rhdzmota.model

object Product {
  sealed trait Product

  final case class ByGrossSales(productId: String, grossSales: Double) extends Product
  final case class ByOrders(productId: String, orderCount: Long) extends Product

}
