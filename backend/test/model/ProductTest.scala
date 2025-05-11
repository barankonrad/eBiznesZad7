package model

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test;
import play.api.libs.json._

class ProductTest {

  @Test
  def testProductFields(): Unit = {
    val product = Product(1, "Laptop", 999.99, 2)

    assertThat(product.id).isEqualTo(1)
    assertThat(product.name).isEqualTo("Laptop")
    assertThat(product.price).isEqualTo(999.99)
    assertThat(product.categoryId).isEqualTo(2)
  }

  @Test
  def testSerialization(): Unit = {
    val product = Product(1, "Laptop", 999.99, 2)
    val json = Json.toJson(product).toString()

    val deserializedProduct = Json.parse(json).as[Product]

    assertThat(deserializedProduct).isEqualTo(product)
  }

  @Test
  def testParseValidJson(): Unit = {
    val json = Json.obj(
      "id" -> 1,
      "name" -> "Laptop",
      "price" -> 999.99,
      "categoryId" -> 2
    )
    val result = json.validate[Product]

    assertThat(result.isSuccess).isTrue
    assertThat(result.asOpt.get).isEqualTo(Product(1, "Laptop", 999.99, 2))
  }

  @Test
  def testParseInvalidJson(): Unit = {
    val invalidJson1 = Json.obj("id" -> 1, "name" -> "Laptop")
    val result1 = invalidJson1.validate[Product]

    assertThat(result1.isError).isTrue

    val invalidJson2 = Json.obj(
      "id" -> 1,
      "name" -> "Laptop",
      "price" -> "expensive",
      "categoryId" -> 2
    )
    val result2 = invalidJson2.validate[Product]

    assertThat(result2.isError).isTrue
  }

  @Test
  def testJsonWithExtraFields(): Unit = {
    val product = Product(1, "Laptop", 999.99, 2)

    val jsonWithExtra = Json.obj(
      "id" -> 1,
      "name" -> "Laptop",
      "price" -> 999.99,
      "categoryId" -> 2,
      "extraField" -> "should be ignored"
    )

    val result = jsonWithExtra.validate[Product]
    assertThat(result.isSuccess).isTrue
    assertThat(result.get).isEqualTo(product)
  }
}