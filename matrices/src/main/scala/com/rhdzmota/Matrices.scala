package com.rhdzmota

import com.rhdzmota.config.{Context, Settings}
import org.apache.spark.mllib.linalg.distributed.{BlockMatrix, CoordinateMatrix, MatrixEntry}

object Matrices extends Context {
  import spark.implicits._

  val readPath: String  = Settings.Data.read
  val writePath: String = Settings.Data.write

  def loadMatrix(n: Int): BlockMatrix =
    new CoordinateMatrix(
      spark.sparkContext.textFile(readPath.replace("*", n.toString)).toDS()
        .map(_.split(","))
        .flatMap(row => row.tail.zipWithIndex.map( {
          case (v, c) => MatrixEntry(row.head.toLong, c.toLong, v.toDouble)}))
        .rdd
    ).toBlockMatrix()

  def loadData: (BlockMatrix, BlockMatrix) = (loadMatrix(1), loadMatrix(2))

  def main(args: Array[String]): Unit = {
    val data = loadData
    val result: BlockMatrix = data._1.multiply(data._2)
    result.toCoordinateMatrix().entries.saveAsTextFile(Settings.Data.write)
    spark.stop()
  }
}
