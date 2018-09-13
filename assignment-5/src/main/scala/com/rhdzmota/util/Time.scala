package com.rhdzmota.util

object Time {

  def fromNanosToSeconds(nanos: Long): Long =
    nanos / 1000000000L

  def printExecutionTime[T](block: => T, expressionName: String = "undefined"): T = {
    val start: Long = System.nanoTime()
    val result = block
    val end = System.nanoTime()
    println(s"[$expressionName] Elapsed time: ${fromNanosToSeconds(end - start)} seconds.")
    result
  }
}
