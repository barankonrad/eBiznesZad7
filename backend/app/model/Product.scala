package model

import play.api.libs.json.{Format, Json}

case class Product(id: Int,
                   name: String,
                   price: Double,
                   categoryId: Int)

object Product {
  implicit val productFormat: Format[Product] = Json.format[Product]
}