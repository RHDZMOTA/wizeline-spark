package com.rhdzmota.model

import io.circe.parser.decode
import java.sql.Timestamp
import com.rhdzmota.extras._

case class AlimazonRow(id: String, timestamp: Timestamp, clientId: String, productId: String, quantity: Int, total: Double) {
  import Time._
  def withMonthAndDow: AlimazonRow.WithMonthAndDow =
    AlimazonRow.WithMonthAndDow(timestamp.dow, timestamp.month, id, timestamp, clientId, productId, quantity, total)
}

object AlimazonRow {
  import Implicits._

  final case class WithMonthAndDow(dow: Int, month: Int, id: String, timestamp: Timestamp, clientId: String, productId: String, quantity: Int, total: Double)

  def fromString(string: String): AlimazonRow =
    decode[AlimazonRow](string).extract
}
