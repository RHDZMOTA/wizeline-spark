package com.rhdzmota.extras.ops

case object Math {
  implicit class DoubleOps(double: Double) {
    def round(decimals: Int): Double =
      BigDecimal(double).setScale(decimals, BigDecimal.RoundingMode.HALF_UP).toDouble
  }
}
