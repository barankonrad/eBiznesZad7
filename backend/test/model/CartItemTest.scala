package model

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test;
import play.api.libs.json._

class CartItemTest {

  @Test
  def testCartItemFields(): Unit = {
    val cartItem = CartItem(1, 5)

    assertThat(cartItem.id).isEqualTo(1)
    assertThat(cartItem.quantity).isEqualTo(5)
  }

  @Test
  def testSerialization(): Unit = {
    val cartItem = CartItem(1, 5)
    val json = Json.toJson(cartItem).toString()

    val deserializedCartItem = Json.parse(json).as[CartItem]

    assertThat(deserializedCartItem).isEqualTo(cartItem)
  }

  @Test
  def testParseValidJson(): Unit = {
    val json = Json.obj("id" -> 1, "quantity" -> 5)
    val result = json.validate[CartItem]

    assertThat(result.isSuccess).isTrue
    assertThat(result.asOpt.get).isEqualTo(CartItem(1, 5))
  }

  @Test
  def testParseInvalidJson(): Unit = {
    val invalidJson1 = Json.obj("id" -> 1)
    val result1 = invalidJson1.validate[CartItem]

    assertThat(result1.isError).isTrue

    val invalidJson2 = Json.obj("id" -> 1, "quantity" -> "five")
    val result2 = invalidJson2.validate[CartItem]

    assertThat(result2.isError).isTrue
  }

  @Test
  def testJsonWithExtraFields(): Unit = {
    val cartItem = CartItem(1, 10)

    val jsonWithExtra = Json.obj(
      "id" -> 1,
      "quantity" -> 10,
      "extraField" -> "should be ignored"
    )

    val result = jsonWithExtra.validate[CartItem]
    assertThat(result.isSuccess).isTrue
    assertThat(result.get).isEqualTo(cartItem)
  }
}