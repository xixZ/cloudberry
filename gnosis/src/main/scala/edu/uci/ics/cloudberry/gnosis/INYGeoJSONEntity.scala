package edu.uci.ics.cloudberry.gnosis

import com.vividsolutions.jts.geom.Geometry
import play.api.libs.json.{Json, JsObject}

sealed trait INYGeoJSONEntity extends IEntity{
  def geometry: Geometry

  def toJson: JsObject

  def toPropertyMap: Map[String, AnyRef]

}

case class NYBoroughEntity(boroCode: Int,
                           boroName: String,
                           geometry: Geometry
                          ) extends INYGeoJSONEntity {

  override def toJson: JsObject =
    Json.obj("boroCode" -> boroCode, "boroName" ->boroName);
  override val level: TypeLevel = BoroLevel

  override val parentLevel: TypeLevel = BoroLevel

  override val key: Long = boroCode.toLong
  override val parentKey: Long = boroCode.toLong

  override def toPropertyMap: Map[String, AnyRef] = Map[String, AnyRef](
    "boroCode" -> Int.box(boroCode),
    "boroName" -> boroName)
}

case class NYNeighborEntity(neighborID: Int,
                            boroCode: Int,
                            geometry: Geometry
                           ) extends INYGeoJSONEntity {
  override def toJson: JsObject = Json.obj(
    "neighborID" ->neighborID, "boroCode" -> boroCode);
  override val level: TypeLevel = NeighborLevel

  override val parentLevel: TypeLevel = BoroLevel
  override val key: Long = neighborID.toLong

  override val parentKey: Long = boroCode.toLong

  override def toPropertyMap: Map[String, AnyRef] = Map[String, AnyRef](
    "neighborID" -> Int.box(neighborID),
    "boroCode" -> Int.box(boroCode)
  )

}

object INYGeoJSONEntity {

  def apply(map: Map[String, AnyRef], geometry: Geometry): INYGeoJSONEntity = {
    NYNeighborEntity(
        neighborID = map.get("id").get.asInstanceOf[Int],
        boroCode = map.get("parentID").get.asInstanceOf[Int],
        geometry
    )
  }
}

