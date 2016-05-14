package edu.uci.ics.cloudberry.noah

import java.text.SimpleDateFormat
import java.util.concurrent.Executors

import better.files.File
import edu.uci.ics.cloudberry.noah.adm.ADM
import org.joda.time.{DateTime, Period}

import scala.concurrent.{ExecutionContext, Future}

object AppUsage extends App {

  case class KeyIndex(name: String, idx: Int)

  val DeviceID = KeyIndex("device_id", 0)
  val AppID = KeyIndex("app_id", 1)
  val AppType = KeyIndex("app_type", 2)
  val StartDate = KeyIndex("start_date", 3)
  val RunTime = KeyIndex("run_time", 4)
  val Continuation = KeyIndex("continuation", 5)
  val Year = KeyIndex("year", 6)
  val Month = KeyIndex("month", 7)
  val Day = KeyIndex("day", 8)
  val PackageName = KeyIndex("package_name", 9)

  var count = 0
  var totalLine = 0

  val formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
  val src = "/Users/jiajianfeng/Downloads/app_usage_events_september_1st.csv"

  val file = File(src + ".adm")
  file.clear()
  for (ln <- scala.io.Source.fromFile(src).getLines().drop(1)) {
    totalLine += 1
    try {
      val parts = ln.split(",")
      val builder = new java.lang.StringBuilder()
      builder.append('{')

      Seq(DeviceID, AppID).foreach(
        p => ADM.keyValueToSbWithComma(builder, p.name, ADM.mkInt64Constructor(parts(p.idx).toInt))
      )
      ADM.keyValueToSbWithComma(builder, AppType.name, ADM.mkInt8Constructor(parts(AppType.idx).toInt.toString))

      ADM.keyValueToSbWithComma(builder, StartDate.name, ADM.mkDateTimeConstructor(formatter.parse(parts(StartDate.idx))))

      val runTime = parts(RunTime.idx).toLong + 1
      ADM.keyValueToSbWithComma(builder, RunTime.name, ADM.mkInt64Constructor(runTime))

      val dateStart = formatter.parse(parts(StartDate.idx))
      val dateEnd = new DateTime(dateStart).plus(new Period(runTime))
      ADM.keyValueToSbWithComma(builder, "use_interval", ADM.mkIntervalConstructor(dateStart, dateEnd.toDate))

      ADM.keyValueToSbWithComma(builder, Continuation.name, (parts(Continuation.idx) != "False").toString)

      val entryDate = new DateTime(parts(Year.idx).toInt, parts(Month.idx).toInt, parts(Day.idx).toInt, 0, 0)
      ADM.keyValueToSbWithComma(builder, "entry_date", ADM.mkDateConstructor(entryDate.toDate))

      ADM.keyValueToSb(builder, PackageName.name, ADM.mkQuote(parts(PackageName.idx)))

      builder.append('}')
      file.appendLine(builder.toString())
      count += 1
    } catch {
      case e: Throwable => System.err.println(e); // System.err.println(ln)
    }
  }
  System.err.println(s"produced $count line from $totalLine")
}
