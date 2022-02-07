package models

import play.api.libs.functional.syntax._
import play.api.libs.json._


case class Image(val id: Int, val imgfile: String, val detect: Boolean)

object ImageLike {
  implicit val imageWrites = new Writes[Image] {
    def writes(x: Image) = Json.obj(
      "id" -> x.id, "imgfile" -> x.imgfile, "detect" -> x.detect)
  }

  implicit val imageReads: Reads[Image] = (
    (JsPath \ "id").read[Int] and (JsPath \ "imgfile").read[String] and
      (JsPath \ "detect").read[Boolean]
  )(Image.apply _)
}

object Images {
  def all() = List(
    Image(id=1, imgfile="Serenity-Now-Featured-Image.jpg", detect=true),
    Image(id=2, imgfile="Rocket-Raccoon-2.jpg", detect=true))
}
