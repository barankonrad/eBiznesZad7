package controllers

import cache.Cache
import model.Product
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

@Singleton
class ProductController @Inject()(val controllerComponents: ControllerComponents, val cache: Cache[Product])
  extends BaseController {

  def showAll(): Action[AnyContent] = Action.async {
    Future.successful(Ok(Json.toJson(cache.get)))
  }

  def showById(id: Int): Action[AnyContent] = Action.async {
    val optionProduct: Option[Product] = cache.get.find(_.id == id)
    optionProduct match {
      case Some(product: Product) =>
        Future.successful(Ok(Json.toJson(product)))
      case None =>
        Future.successful(NotFound(Json.obj()))
    }
  }

  def update(): Action[JsValue] = Action(parse.json).async { request =>
    request.body.validate[Product] match {
      case JsSuccess(existingItem: Product, _) =>
        cache.update(existingItem.id, existingItem)
        Future.successful(Ok)
      case JsError(errors) =>
        Future.successful(BadRequest(Json.obj("error" -> "Invalid JSON", "details" -> errors.toString)))
    }
  }

  def delete(id: Int): Action[AnyContent] = Action.async {
    cache.delete(id)
    Future.successful(Ok)
  }

  def add(): Action[JsValue] = Action(parse.json).async { request =>
    request.body.validate[Product] match {
      case JsSuccess(item: Product, _) =>
        cache.add(item)
        Future.successful(Ok)
      case JsError(errors) =>
        Future.successful(BadRequest(Json.obj("error" -> "Invalid JSON", "details" -> errors.toString)))
    }
  }
}
