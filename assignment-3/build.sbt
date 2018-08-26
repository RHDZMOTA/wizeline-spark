import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.rhdzmota",
      scalaVersion := "2.11.8",
      version      := "0.0.0"
    )),
    name := "wizeline-assigment-3",
    libraryDependencies ++= {
      val configVersion = "1.3.1"
      val sparkVersion = "2.2.0"
      val circeVersion = "0.9.3"
      Seq(
        "com.typesafe" % "config" % configVersion,
        // Circe
        "io.circe" %% "circe-core"    % circeVersion,
        "io.circe" %% "circe-generic" % circeVersion,
        "io.circe" %% "circe-parser"  % circeVersion,
        "io.circe" %% "circe-generic-extras" % circeVersion,
        // Spark
        "org.apache.spark" %% "spark-core"  % sparkVersion,
        "org.apache.spark" %% "spark-sql" 	  % sparkVersion,
        scalaTest % Test
      )
    },
    assemblyMergeStrategy in assembly := {
      case PathList("META-INF", xs @ _*) => MergeStrategy.discard
      case x => MergeStrategy.first
    }
  )
