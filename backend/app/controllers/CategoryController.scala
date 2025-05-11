package controllers

import cache.Cache
import model.Category
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

@Singleton
class CategoryController @Inject()(val controllerComponents: ControllerComponents, val cache: Cache[Category]) extends BaseController {

  def showAll(): Action[AnyContent] = Action.async {
    Future.successful(Ok(Json.toJson(cache.get)))
  }

  def showById(id: Int): Action[AnyContent] = Action.async {
    val optionCategory: Option[Category] = cache.get.find(_.id == id)
    optionCategory match {
      case Some(category: Category) =>
        Future.successful(Ok(Json.toJson(category)))
      case None =>
        Future.successful(NotFound(Json.obj()))
    }
  }

  def update(): Action[JsValue] = Action(parse.json).async { request =>
    request.body.validate[Category] match {
      case JsSuccess(category, _) =>
        cache.update(category.id, category)
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
    request.body.validate[Category] match {
      case JsSuccess(category: Category, _) =>
        cache.add(category)
        Future.successful(Ok)
      case JsError(errors) =>
        Future.successful(BadRequest(Json.obj("error" -> "Invalid JSON", "details" -> errors.toString)))
    }
  }

}
