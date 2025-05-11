package model

import play.api.libs.json.{Format, Json}

case class Category(id: Int,
                    name: String)

object Category {
  implicit val categoryProduct: Format[Category] = Json.format[Category]
}
