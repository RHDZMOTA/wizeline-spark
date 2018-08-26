package com.rhdzmota.model

import io.circe.Decoder
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveDecoder

object Implicits {

  implicit class EitherOps[L, R](either: Either[L, R]) {
    def toOption: Option[R] = either match {
      case Right(value) => Some(value)
      case _ => None
    }
    def extract: R = either.toOption.get
  }

  implicit val customConfig: Configuration = Configuration.default.withSnakeCaseMemberNames.withDefaults
  implicit val decodeTimestamp: Decoder[java.sql.Timestamp] = Decoder[String]
    .map(string => java.sql.Timestamp.valueOf(string.replace("T", " ")))
  implicit val decodeAlimzonRow: Decoder[AlimazonRow] = deriveDecoder[AlimazonRow]

}
