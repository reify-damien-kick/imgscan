package models

import play.api.libs.json._


case class Image(val id: Int, val imgfile: String, val detect: Boolean)

object Image {
  implicit val imageWrites = new Writes[Image] {
    def writes(x: Image) = Json.obj(
      "id" -> x.id, "imgfile" -> x.imgfile, "detect" -> x.detect)
  }
}

object Images {
  def all() = List(
    Image(id=1, imgfile="Serenity-Now-Featured-Image.jpg", detect=true),
    Image(id=2, imgfile="Rocket-Raccoon-2.jpg", detect=true))
}
