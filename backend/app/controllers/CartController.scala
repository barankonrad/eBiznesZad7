package controllers

import cache.Cache
import model.CartItem
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future


@Singleton
class CartController @Inject()(val controllerComponents: ControllerComponents, val cart: Cache[CartItem]) extends BaseController {

  def showAll(): Action[AnyContent] = Action.async {
    Future.successful(Ok(Json.toJson(cart.get)))
  }

  def showById(id: Int): Action[AnyContent] = Action.async {
    val optionCartItem = cart.get.find(_.id == id)
    optionCartItem match {
      case Some(cartItem: CartItem) =>
        Future.successful(Ok(Json.toJson(cartItem)))
      case None =>
        Future.successful(NotFound(Json.obj()))
    }
  }

  def update(id: Int, quantity: Int): Action[AnyContent] = Action.async { request =>
    val optionCartItem = cart.get.find(_.id == id)
    optionCartItem match {
      case Some(cartItem: CartItem) =>
        val updatedCartItem = new CartItem(id, quantity)
        cart.update(id, updatedCartItem)
        Future.successful(Ok)
      case None =>
        Future.successful(NotFound(Json.obj()))
    }
  }

  def delete(id: Int): Action[AnyContent] = Action.async {
    cart.delete(id)
    Future.successful(Ok)
  }

  def clear(): Action[AnyContent] = Action.async {
    cart.get.foreach(item => cart.delete(item.id))
    Future.successful(Ok)
  }


  def add(): Action[JsValue] = Action(parse.json).async { request =>
    request.body.validate[CartItem] match {
      case JsSuccess(item: CartItem, _) =>
        cart.add(item)
        Future.successful(Ok)
      case JsError(errors) =>
        Future.successful(BadRequest(Json.obj("error" -> "Invalid JSON", "details" -> errors.toString)))
    }
  }
}
