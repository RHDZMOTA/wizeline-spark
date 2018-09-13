package com.rhdzmota.model

import java.util.Base64

import com.rhdzmota.model.UserProfile.Image.WithMetadata
import io.circe.parser.decode
import io.circe.Error
import io.circe.syntax._

trait UserProfile

object UserProfile {
  import Implicits._
  import Circe._

  final case class UserProfileWithoutMetadata(
                        firstName: String, lastName: String, isAlive: Boolean, age: Int, address: UserProfile.Address,
                        image: UserProfile.Image.WithoutMetadata) extends UserProfile {
    def addImageMetadata(): UserProfileWithMetadata = UserProfileWithMetadata(
      firstName, lastName, isAlive, age, address, image.addMetadata())
  }

  final case class UserProfileWithMetadata(
                                            firstName: String,
                                            lastName: String,
                                            isAlive: Boolean,
                                            age: Int,
                                            address: UserProfile.Address,
                                            image: UserProfile.Image.WithMetadata
                                          ) extends UserProfile {
    def toJson: String = this.asJson.toString
  }

  def fromString(string: String): Either[Error, UserProfileWithoutMetadata] = decode[UserProfileWithoutMetadata](string)
  def unsafeFromString(string: String): UserProfileWithoutMetadata = fromString(string).extract

  final case class Address(streetAddress: String, city: String, state: String, postalCode: Int)

  trait Image {
    def addMetadata(): WithMetadata = this match {
      case Image.WithMetadata(d, m)    => Image.WithMetadata(d, m)
      case Image.WithoutMetadata(data) =>
        val dataString: String = Base64.getDecoder.decode(data).map(_.toChar).mkString
        val header = dataString.split("\n")
          .map(line => if (line contains "#") line.split("#").head else line)
          .flatMap(line => line.split(" "))
          .filter(line => 0 < line.length & line.length < 5)
        def formatMagic(m: String): String = if (m.trim.length != 2) "" else m
        val metadata = header.take(4).toList match {
          case List(m, w, h)        => WithMetadata.Metadata(formatMagic(m), w.toInt, h.toInt, None)
          case List(m, w, h, v)     => WithMetadata.Metadata(formatMagic(m), w.toInt, h.toInt, v.asInt)
        }
        Image.WithMetadata(data, metadata)
    }
  }
  object Image {
    final case class WithoutMetadata(data: String) extends Image
    final case class WithMetadata(data: String, metadata: WithMetadata.Metadata) extends Image
    object WithMetadata {
      case class Metadata(magic: String, width: Int, height: Int, maxValue: Option[Int])
    }
  }

}