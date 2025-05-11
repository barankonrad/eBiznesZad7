package model

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test;
import play.api.libs.json._

class CategoryTest {

  @Test
  def testCategoryFields(): Unit = {
    val category = Category(1, "Electronics")

    assertThat(category.id).isEqualTo(1)
    assertThat(category.name).isEqualTo("Electronics")
  }

  @Test
  def testSerialization(): Unit = {
    val category = Category(1, "Electronics")
    val json = Json.toJson(category).toString()

    val deserializedCategory = Json.parse(json).as[Category]

    assertThat(deserializedCategory).isEqualTo(category)
  }

  @Test
  def testParseValidJson(): Unit = {
    val json = Json.obj("id" -> 1, "name" -> "Electronics")
    val result = json.validate[Category]

    assertThat(result.isSuccess).isTrue
    assertThat(result.asOpt.get).isEqualTo(Category(1, "Electronics"))
  }

  @Test
  def testParseInvalidJson(): Unit = {
    val invalidJson1 = Json.obj("id" -> 1)
    val result1 = invalidJson1.validate[Category]

    assertThat(result1.isError).isTrue

    val invalidJson2 = Json.obj("id" -> 1, "name" -> 123)
    val result2 = invalidJson2.validate[Category]

    assertThat(result2.isError).isTrue
  }

  @Test
  def testJsonWithExtraFields(): Unit = {
    val category = Category(1, "Electronics")

    val jsonWithExtra = Json.obj(
      "id" -> 1,
      "name" -> "Electronics",
      "extraField" -> "should be ignored"
    )

    val result = jsonWithExtra.validate[Category]
    assertThat(result.isSuccess).isTrue
    assertThat(result.get).isEqualTo(category)
  }
}