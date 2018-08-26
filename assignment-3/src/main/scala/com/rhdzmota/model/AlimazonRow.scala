package com.rhdzmota.model

import java.sql.Timestamp

import io.circe.parser.decode
import com.rhdzmota.extras.ops._

case class AlimazonRow(id: String, timestamp: Timestamp, clientId: String, productId: String, quantity: Int, total: Float) {
  import Time._
  val withHour: AlimazonRow.WithHour =
    AlimazonRow.WithHour(id, timestamp, clientId, productId, quantity, total, timestamp.hour)
}

object AlimazonRow {
  import Implicits._

  case class WithHour(id: String, timestamp: Timestamp, clientId: String, productId: String, quantity: Int, total: Float, hour: Int)

  def fromString(string: String): AlimazonRow =
    decode[AlimazonRow](string).extract
}
