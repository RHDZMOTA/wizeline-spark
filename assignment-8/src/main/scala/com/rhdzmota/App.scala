package com.rhdzmota

import com.rhdzmota.config.{Context, Settings}
import com.rhdzmota.model.UserProfile.UserProfileWithMetadata
import com.rhdzmota.model.UserProfile
import org.apache.spark.sql.Dataset

object App extends Context {
  import spark.implicits._

  val readPath: String  = Settings.Data.read
  val writePath: String = Settings.Data.write

  val rawData: Dataset[String] = spark.read.textFile(readPath)

  val userProfile: Dataset[UserProfileWithMetadata]=
    rawData.map(string => UserProfile.unsafeFromString(string).addImageMetadata())

  val output: Dataset[String] = userProfile.map(row => row.toJson)


  def main(args: Array[String]): Unit = {
    output.write.json(writePath)
    spark.stop()
  }
}
