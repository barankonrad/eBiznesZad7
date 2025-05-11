package controllers

import cache.Cache
import model.Product
import org.junit.Test;
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers._
import org.mockito.Mockito._
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test._

class ProductControllerTest {

  private val PRODUCTS_PATH = "/products"
  private val CONTENT_TYPE_HEADER = ("Content-Type", "application/json")

  private val controllerComponents = Helpers.stubControllerComponents()
  private val cache = mock(classOf[Cache[Product]])
  private val controller = new ProductController(controllerComponents, cache)

  private val laptop = Product(1, "Laptop", 999.99, 1)
  private val book = Product(2, "Book", 19.99, 2)
  private val products = List(laptop, book)

  @Test
  def testShowAll(): Unit = {
    when(cache.get).thenReturn(products)

    val result = controller.showAll().apply(FakeRequest(GET, PRODUCTS_PATH))

    assert(status(result) == OK)
    assert(contentAsJson(result) == Json.toJson(products))
    verify(cache).get
  }

  @Test
  def testShowById(): Unit = {
    when(cache.get).thenReturn(products)

    val result = controller.showById(1).apply(FakeRequest(GET, s"$PRODUCTS_PATH/1"))

    assert(status(result) == OK)
    assert(contentAsJson(result) == Json.toJson(laptop))
  }

  @Test
  def testShowByIdNotFound(): Unit = {
    when(cache.get).thenReturn(products)

    val result = controller.showById(99).apply(FakeRequest(GET, s"$PRODUCTS_PATH/99"))

    assert(status(result) == NOT_FOUND)
  }

  @Test
  def testUpdate(): Unit = {
    val updatedProduct = Product(1, "Updated Laptop", 899.99, 1)
    val jsonBody = Json.toJson(updatedProduct)
    val request = FakeRequest(PUT, PRODUCTS_PATH)
      .withHeaders(CONTENT_TYPE_HEADER)
      .withBody(jsonBody)

    val result = controller.update()(request)

    assert(status(result) == OK)
    verify(cache).update(ArgumentMatchers.eq(1), any[Product])
  }

  @Test
  def testUpdateInvalidJson(): Unit = {
    val invalidJson = Json.obj("invalid" -> "data")
    val request = FakeRequest(PUT, PRODUCTS_PATH)
      .withHeaders(CONTENT_TYPE_HEADER)
      .withBody(invalidJson)

    val result = controller.update()(request)

    assert(status(result) == BAD_REQUEST)
  }

  @Test
  def testDelete(): Unit = {
    val result = controller.delete(1).apply(FakeRequest(DELETE, s"$PRODUCTS_PATH/1"))

    assert(status(result) == OK)
    verify(cache).delete(1)
  }

  @Test
  def testAdd(): Unit = {
    val newProduct = Product(3, "Headphones", 49.99, 1)
    val jsonBody = Json.toJson(newProduct)
    val request = FakeRequest(POST, PRODUCTS_PATH)
      .withHeaders(CONTENT_TYPE_HEADER)
      .withBody(jsonBody)

    val result = controller.add()(request)

    assert(status(result) == OK)
    verify(cache).add(any[Product])
  }

  @Test
  def testAddInvalidJson(): Unit = {
    val invalidJson = Json.obj("invalid" -> "data")
    val request = FakeRequest(POST, PRODUCTS_PATH)
      .withHeaders(CONTENT_TYPE_HEADER)
      .withBody(invalidJson)

    val result = controller.add()(request)

    assert(status(result) == BAD_REQUEST)
  }
}