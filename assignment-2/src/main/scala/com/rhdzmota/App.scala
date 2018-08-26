package com.rhdzmota

import java.util.Calendar

import com.rhdzmota.config.{Context, Settings}
import org.apache.spark.sql._
import org.apache.spark.sql.functions._
import com.rhdzmota.model._

object App extends Context {
  import spark.implicits._

  val readPath: String  = Settings.Data.read

  val alimazon: Dataset[AlimazonRow.WithMonthAndDow] =
      spark.sparkContext.textFile(readPath).toDS.map(row => AlimazonRow.fromString(row).withMonthAndDow).cache()

  val dowList: List[Int] = List(
    Calendar.SUNDAY,
    Calendar.MONDAY,
    Calendar.TUESDAY,
    Calendar.WEDNESDAY,
    Calendar.THURSDAY,
    Calendar.FRIDAY,
    Calendar.SATURDAY)

  val monthList: List[Int] = List(
    Calendar.JANUARY,
    Calendar.FEBRUARY,
    Calendar.MARCH,
    Calendar.APRIL,
    Calendar.MAY,
    Calendar.JUNE,
    Calendar.JULY,
    Calendar.AUGUST,
    Calendar.SEPTEMBER,
    Calendar.OCTOBER,
    Calendar.NOVEMBER,
    Calendar.DECEMBER
  )

  def dowProcedure(dow: Int): Unit = {
    val alimazonDow = alimazon.filter(_.dow == dow).cache().groupByKey(_.productId)
    val dowTag = (dow + 1).toString
    alimazonDow.agg(sum("total").as[Double])
      .map({ case (id, sales) => Product.ByGrossSales(id, sales) })
      .orderBy(desc("grossSales"))
      .head(10).toSeq.toDS.coalesce(1)
      .write.csv(Settings.Data.Write.Product.byGrossSales.replace("<dow>", dowTag))
    alimazonDow.agg(count("id").as[Long])
      .map({ case (id, orders) => Product.ByOrders(id, orders)})
      .orderBy(desc("orderCount"))
      .head(10).toSeq.toDS.coalesce(1)
      .write.csv(Settings.Data.Write.Product.byOrders.replace("<dow>", dowTag))
  }

  def monthProcedure(month: Int): Unit = {
    val alimazonMonth = alimazon.filter(_.month == month).cache().groupByKey(_.clientId)
    val monthTag = (month + 1).toString
    alimazonMonth.agg(sum("total").as[Double])
      .map({ case (id, sales) => Customer.ByGrossSpending(id, sales)})
      .orderBy(desc("grossSpending"))
      .head(10).toSeq.toDS.coalesce(1)
      .write.csv(Settings.Data.Write.Customer.byGrossSpending.replace("<month>", monthTag))
    alimazonMonth.agg(count("id").as[Long])
      .map({ case (id, orders) => Customer.ByOrders(id, orders)})
      .orderBy(desc("ordersCount"))
      .head(10).toSeq.toDS.coalesce(1)
      .write.csv(Settings.Data.Write.Customer.byOrders.replace("<month>", monthTag))
  }

  def main(args: Array[String]): Unit = {
    dowList.foreach(dow => dowProcedure(dow))
    monthList.foreach(month => monthProcedure(month))
    spark.stop()
  }
}
