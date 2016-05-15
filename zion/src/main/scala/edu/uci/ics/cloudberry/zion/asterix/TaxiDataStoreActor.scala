package edu.uci.ics.cloudberry.zion.asterix

import edu.uci.ics.cloudberry.zion.actor.DataStoreActor
import edu.uci.ics.cloudberry.zion.model._
import play.api.Logger
import play.api.libs.json.JsArray
import play.api.libs.ws.WSResponse

import scala.concurrent.{ExecutionContext, Future}

class TaxiDataStoreActor(conn: AsterixConnection)(implicit ec: ExecutionContext) extends DataStoreActor(TaxiDataStoreActor.Name) {

  import TaxiDataStoreActor._

  // TODO use the Visitor pattern to generate the AQL instead of this hacking code
  override def query(query: DBQuery): Future[Response] = {
    conn.post(generateAQL(name, query)).map(handleWSResponse)
  }

  override def update(query: DBUpdateQuery): Future[Response] = ???
}

object TaxiDataStoreActor {
  val DataVerse = "tripdata"
  val DataSet = "tripdata"
  val DataType = "tripDataType"
  val Name = s"$DataVerse.$DataSet"

  val FieldPrimaryKey = "id"
  val FieldStateID = "geo_tag.stateID"
  val FieldCountyID = "geo_tag.countyID"
  val FieldCityID = "geo_tag.cityID"

  val FieldCreateAt = "create_at"
  val FieldKeyword = "text"
  val FieldHashTag = "hash_tag"

  import SpatialLevels._
  import TimeLevels._

  val SpatialLevelMap = Map[SpatialLevels.Value, String](State -> FieldStateID, County -> FieldCountyID, City -> FieldCityID)

  val TimeFormatMap = Map[TimeLevels.Value, String](Year -> "YYYY", Month -> "YYYY-MM", Day -> "YYYY-MM-DD",
    Hour -> "YYYY-MM-DD hh", Minute -> "YYYY-MM-DD hh:mm", Second -> "YYYY-MM-DD hh:mm:ss")

  def mergeResult(viewCount: SpatialTimeCount, sourceCount: SpatialTimeCount): Response = {
    SpatialTimeCount(
      KeyCountPair.keyCountMerge(viewCount.map, sourceCount.map),
      KeyCountPair.keyCountMerge(viewCount.time, sourceCount.time),
      KeyCountPair.keyCountMerge(viewCount.hashtag, sourceCount.hashtag)
    )
  }

  def handleWSResponse(wsResponse: WSResponse): Response = {
    wsResponse.json.asInstanceOf[JsArray].apply(0).as[SpatialTimeCount]
  }

  def generateAQL(name: String, query: DBQuery): String = {
    val aqlVisitor = AQLVisitor(name)
    val predicate = query.predicates.map(p => aqlVisitor.visitPredicate("t", p)).mkString("\n")
    s"""
       |use dataverse $DataVerse
       |let $$common := (
       |for $$t in dataset $name
       |$predicate
       |return $$t
       |)
       |
       |let $$map := (
       |for $$t in $$common
       |${byMap(query.summaryLevel.spatialLevel)}
       |)
       |
       |let $$time := (
       |for $$t in $$common
       |${byTime(query.summaryLevel.timeLevel)}
       |)
       |
       |return {"map": $$map, "time": $$time}
       |""".stripMargin
  }

  //TODO move this hacking code to visitor
  private def byMap(level: SpatialLevels.Value): String = {
    s"""
       |group by $$c := $$t.${SpatialLevelMap.getOrElse(level, FieldStateID)} with $$t
       |return { "key": string($$c) , "count": count($$t) }
       |""".stripMargin
  }

  private def byTime(level: TimeLevels.Value): String = {
    s"""
       |group by $$c := print-datetime($$t.create_at, "${TimeFormatMap.getOrElse(level, "YYYY-MM-DD")}") with $$t
       |let $$count := count($$t)
       |return { "key" : $$c , "count": $$count }
       |""".stripMargin
  }

}