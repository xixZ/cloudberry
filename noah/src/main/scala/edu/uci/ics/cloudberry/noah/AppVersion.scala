package edu.uci.ics.cloudberry.noah

import java.text.SimpleDateFormat

import better.files.File
import edu.uci.ics.cloudberry.noah.adm.ADM

object AppVersion extends App {

  case class KeyIndex(name: String, idx: Int)

  val AppID = KeyIndex("app_id", 0)
  val Package = KeyIndex("package_name", 1)
  val AppName = KeyIndex("app_name", 2)
  val Version = KeyIndex("version", 3)
  val Developer = KeyIndex("developer", 4)
  val AppType = KeyIndex("app_type", 5)
  val Category = KeyIndex("category", 6)
  val ICON = KeyIndex("icon", 14)
  val CreateDeviceID = KeyIndex("create_device_id", 16)
  val CreateDate = KeyIndex("create_date", 17)
  val UpdateDate = KeyIndex("update_date", 18)

  var count = 0
  var totalLine = 0

  val formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm")
  val src = "/Users/jiajianfeng/Downloads/application_versions.csv"

  val file = File(src + ".adm")
  file.clear()
  for (ln <- scala.io.Source.fromFile(src).getLines()) {
    totalLine += 1
    try {
      val parts = ln.split(",")
      val builder = new java.lang.StringBuilder()
      builder.append('{')

      ADM.keyValueToSbWithComma(builder, AppID.name, ADM.mkInt64Constructor(parts(AppID.idx).toInt))
      Seq(Package, AppName, Version, Developer, AppType, Category, ICON).foreach(
        pair => ADM.keyValueToSbWithComma(builder, pair.name, ADM.mkQuote(parts(pair.idx)))
      )
      ADM.keyValueToSbWithComma(builder, CreateDeviceID.name, ADM.mkInt64Constructor(parts(CreateDeviceID.idx).toInt))

      ADM.keyValueToSbWithComma(builder, CreateDate.name, ADM.mkDateTimeConstructor(formatter.parse(parts(CreateDate.idx).trim)))
      ADM.keyValueToSb(builder, UpdateDate.name, ADM.mkDateTimeConstructor(formatter.parse(parts(UpdateDate.idx).trim)))

      builder.append('}')
      file.appendLine(builder.toString())
      count += 1
    } catch {
      case e: Throwable => System.err.println(e);// System.err.println(ln)
    }
  }
  System.err.println(s"produced $count line from $totalLine")
}
