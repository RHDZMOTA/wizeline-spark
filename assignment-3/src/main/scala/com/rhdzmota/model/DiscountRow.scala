package com.rhdzmota.model

import com.rhdzmota.extras.ops._

case class DiscountRow(
                        productId: String,
                        totalProductSold: Long,
                        totalRegisteredSales: Double,
                        totalOrders: Long,
                        unitProductPrice: Double,
                        newUnitProductPrice: Double
                      )

object DiscountRow {
  import Math._

  def apply(discount: Double, productId: String, totalProductSold: Long, totalRegisteredSales: Double, totalOrders: Long): DiscountRow =
    DiscountRow(
      productId = productId,
      totalProductSold = totalProductSold,
      totalRegisteredSales = totalRegisteredSales,
      totalOrders = totalOrders,
      unitProductPrice = (totalRegisteredSales / totalProductSold).round(2),
      newUnitProductPrice = ((1 - discount) * totalRegisteredSales / totalProductSold).round(2)
    )
}
