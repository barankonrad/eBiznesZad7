package controllers

import cache.Cache
import model.Category
import org.junit.Test;
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers._
import org.mockito.Mockito._
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test._

class CategoryControllerTest {

  private val controllerComponents = Helpers.stubControllerComponents()
  private val cache = mock(classOf[Cache[Category]])
  private val controller = new CategoryController(controllerComponents, cache)

  private val electronicsCategory = Category(1, "Electronics")
  private val booksCategory = Category(2, "Books")
  private val categories = List(electronicsCategory, booksCategory)

  @Test
  def testShowAll(): Unit = {
    when(cache.get).thenReturn(categories)

    val result = controller.showAll().apply(FakeRequest(GET, "/categories"))

    assert(status(result) == OK)
    assert(contentAsJson(result) == Json.toJson(categories))
    verify(cache).get
  }

  @Test
  def testShowById(): Unit = {
    when(cache.get).thenReturn(categories)

    val result = controller.showById(1).apply(FakeRequest(GET, "/categories/1"))

    assert(status(result) == OK)
    assert(contentAsJson(result) == Json.toJson(electronicsCategory))
  }

  @Test
  def testShowByIdNotFound(): Unit = {
    when(cache.get).thenReturn(categories)

    val result = controller.showById(99).apply(FakeRequest(GET, "/categories/99"))

    assert(status(result) == NOT_FOUND)
  }

  @Test
  def testUpdate(): Unit = {
    val updatedCategory = Category(1, "Updated Electronics")
    val jsonBody = Json.toJson(updatedCategory)
    val request = FakeRequest(PUT, "/categories")
      .withHeaders("Content-Type" -> "application/json")
      .withBody(jsonBody)

    val result = controller.update()(request)

    assert(status(result) == OK)
    verify(cache).update(ArgumentMatchers.eq(1), any[Category])
  }

  @Test
  def testUpdateInvalidJson(): Unit = {
    val invalidJson = Json.obj("invalid" -> "data")
    val request = FakeRequest(PUT, "/categories")
      .withHeaders("Content-Type" -> "application/json")
      .withBody(invalidJson)

    val result = controller.update()(request)

    assert(status(result) == BAD_REQUEST)
  }

  @Test
  def testDelete(): Unit = {
    val result = controller.delete(1).apply(FakeRequest(DELETE, "/categories/1"))

    assert(status(result) == OK)
    verify(cache).delete(1)
  }

  @Test
  def testAdd(): Unit = {
    val newCategory = Category(3, "Clothing")
    val jsonBody = Json.toJson(newCategory)
    val request = FakeRequest(POST, "/categories")
      .withHeaders("Content-Type" -> "application/json")
      .withBody(jsonBody)

    val result = controller.add()(request)

    assert(status(result) == OK)
    verify(cache).add(any[Category])
  }

  @Test
  def testAddInvalidJson(): Unit = {
    val invalidJson = Json.obj("invalid" -> "data")
    val request = FakeRequest(POST, "/categories")
      .withHeaders("Content-Type" -> "application/json")
      .withBody(invalidJson)

    val result = controller.add()(request)

    assert(status(result) == BAD_REQUEST)
  }
}