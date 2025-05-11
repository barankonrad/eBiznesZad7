package controllers

import cache.Cache
import model.Payment
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}

import java.time.Instant
import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

@Singleton
class PaymentController @Inject()(val controllerComponents: ControllerComponents, val paymentCache: Cache[Payment]) extends BaseController {

  // Funkcja do obsługi płatności
  def makePayment(): Action[JsValue] = Action(parse.json).async { request =>
    request.body.validate[Payment] match {
      case JsSuccess(payment: Payment, _) =>
        // Dodanie płatności do cache
        paymentCache.add(payment)

        // Zwracanie odpowiedzi po wykonaniu płatności
        Future.successful(Ok(Json.obj(
          "status" -> "success",
          "message" -> "Payment successful!",
        )))

      case JsError(errors) =>
        Future.successful(BadRequest(Json.obj("error" -> "Invalid JSON", "details" -> errors.toString)))
    }
  }

  // Funkcja do sprawdzenia statusu płatności
  def checkPaymentStatus(id: Int): Action[AnyContent] = Action.async {
    paymentCache.get.find(_.id == id) match {
      case Some(payment: Payment) =>
        // Zwrócenie statusu płatności z cache
        Future.successful(Ok(Json.obj(
          "paymentId" -> id,
          "status" -> payment.status,
          "timestamp" -> payment.timestamp.toString
        )))
      case None =>
        Future.successful(NotFound(Json.obj("error" -> "Payment not found")))
    }
  }

  // Funkcja do pobrania wszystkich płatności
  def getAllPayments(): Action[AnyContent] = Action.async {
    val allPayments = paymentCache.get
    if (allPayments.isEmpty) {
      Future.successful(Ok(Json.obj("message" -> "No payments found")))
    } else {
      Future.successful(Ok(Json.toJson(allPayments)))
    }
  }
}