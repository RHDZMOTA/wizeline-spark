package com.rhdzmota.model

import io.circe.parser.decode

case class AlimazonRow(id: String, timestamp: String, clientId: String, productId: String, quantity: Int, total: Float)

object AlimazonRow {
  import Implicits._

  def fromString(string: String): AlimazonRow =
    decode[AlimazonRow](string).extract
}
