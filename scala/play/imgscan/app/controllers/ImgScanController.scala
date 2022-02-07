package controllers

import javax.inject._
import play.api._
import play.api.libs.json._
import play.api.mvc._

import models.Images


@Singleton
class ImgScanController @Inject()
  (val controllerComponents: ControllerComponents) extends BaseController
{

  def listImages() = Action {
    implicit request: Request[AnyContent] => {
      val images = Images.all()
      val json = Json.toJson(images)
      Ok(json)
    }
  }
}
