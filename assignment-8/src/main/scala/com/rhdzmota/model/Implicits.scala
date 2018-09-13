package com.rhdzmota.model

import com.rhdzmota.model.UserProfile.Image.WithMetadata.Metadata
import com.rhdzmota.model.UserProfile.{Address, Image, UserProfileWithMetadata, UserProfileWithoutMetadata}
import com.rhdzmota.model.UserProfile.Image.{WithMetadata, WithoutMetadata}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}
import io.circe.syntax._

import scala.util.Try

object Implicits {
  implicit class EitherOps[L, R](either: Either[L, R]) {
    def toOption: Option[R] = either match {
      case Right(value) => Some(value)
      case _ => None
    }
    def extract: R = either.toOption.get
  }

  implicit class StringOps(string: String) {
    def asInt: Option[Int] = Try(string.toInt).toOption
  }

  object Circe {
    // Metadata
    implicit val encodeMetadata: Encoder[Metadata] = deriveEncoder
    implicit val decodeMetadata: Decoder[Metadata] = deriveDecoder
    // WithMetadata
    implicit val encodeWithMetadata: Encoder[WithMetadata] = deriveEncoder
    implicit val decodeWithMetadata: Decoder[WithMetadata] = deriveDecoder
    // WithoutMetadata
    implicit val encodeWithoutMetadata: Encoder[WithoutMetadata] = deriveEncoder
    implicit val decodeWithoutMetadata: Decoder[WithoutMetadata] = deriveDecoder
    // Image
    implicit val encodeImage: Encoder[Image] = Encoder.instance {
      case withoutMetadata: WithoutMetadata => withoutMetadata.asJson
      case withMetadata: WithMetadata       => withMetadata.asJson
    }
    implicit val decodeImage: Decoder[Image] =
      Decoder[WithMetadata].map[Image](identity).or(Decoder[WithoutMetadata].map[Image](identity))
    // Address
    implicit val encodeAddress: Encoder[Address] = deriveEncoder
    implicit val decodeAddress: Decoder[Address] = deriveDecoder
    // UserProfile
    implicit val encodeUserProfileWithoutMetadata: Encoder[UserProfileWithoutMetadata] = deriveEncoder
    implicit val decodeUserProfileWithoutMetadata: Decoder[UserProfileWithoutMetadata] = deriveDecoder
    implicit val encodeUserProfileWithMetadata: Encoder[UserProfileWithMetadata] = deriveEncoder
    implicit val decodeUserProfileWithMetadata: Decoder[UserProfileWithMetadata] = deriveDecoder

  }
}
