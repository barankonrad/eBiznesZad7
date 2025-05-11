package model

import play.api.libs.json.{Format, Json}

case class CartItem(id: Int,
                    quantity: Int)

object CartItem {
  implicit val format: Format[CartItem] = Json.format[CartItem]
}