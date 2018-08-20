import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.rhdzmota",
      scalaVersion := "2.11.8",
      version      := "0.0.0"
    )),
    name := "wizeline-spark-matrices",
    libraryDependencies ++= {
      val configVersion = "1.3.1"
      val sparkVersion = "2.2.0"
      Seq(
        "com.typesafe" % "config" % configVersion,
        // Spark
        "org.apache.spark" %% "spark-core"      % sparkVersion,
        "org.apache.spark" %% "spark-streaming" % sparkVersion,
        "org.apache.spark" %% "spark-sql"   	    % sparkVersion,
        "org.apache.spark" %% "spark-mllib" 	    % sparkVersion,
        scalaTest % Test
      )
    },
    assemblyMergeStrategy in assembly := {
      case PathList("META-INF", xs @ _*) => MergeStrategy.discard
      case x => MergeStrategy.first
    }
  )
