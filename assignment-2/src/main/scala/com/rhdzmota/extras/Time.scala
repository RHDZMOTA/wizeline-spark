package com.rhdzmota.extras

import java.util.Calendar
import java.sql.Timestamp

object Time {

  implicit class CalendarOps(c: Calendar) {
    def withTimestamp(ts: Timestamp): Calendar = {
      c.setTime(ts)
      c
    }

    def increaseByMonths(n: Int): Calendar = {
      c.add(Calendar.MONTH, n)
      c
    }

  }

  implicit class TimestampOps(ts: java.sql.Timestamp) {
    def hour: Int =
      Calendar.getInstance().withTimestamp(ts).get(Calendar.HOUR_OF_DAY)

    def month: Int =
      Calendar.getInstance().withTimestamp(ts).get(Calendar.MONTH)

    def year: Int =
      Calendar.getInstance().withTimestamp(ts).get(Calendar.YEAR)

    def dow: Int =
      Calendar.getInstance().withTimestamp(ts).get(Calendar.DAY_OF_WEEK)

    def addMonths(n: Int): Timestamp =
      new Timestamp(Calendar.getInstance().withTimestamp(ts).increaseByMonths(n).getTime.getTime)

    def minusMonths(n: Int): Timestamp =
      new Timestamp(Calendar.getInstance().withTimestamp(ts).increaseByMonths(-1 * n).getTime.getTime)
  }
}
