package controllers

import cache.Cache
import model.CartItem
import org.junit.Test;
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers._
import org.mockito.Mockito._
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test._

class CartControllerTest {

  private val controllerComponents = Helpers.stubControllerComponents()
  private val cache = mock(classOf[Cache[CartItem]])
  private val controller = new CartController(controllerComponents, cache)

  private val firstItem = CartItem(1, 3)
  private val secondItem = CartItem(2, 5)
  private val cartItems = List(firstItem, secondItem)

  @Test
  def testShowAll(): Unit = {
    when(cache.get).thenReturn(cartItems)

    val result = controller.showAll().apply(FakeRequest(GET, "/cart"))

    assert(status(result) == OK)
    assert(contentAsJson(result) == Json.toJson(cartItems))
    verify(cache).get
  }

  @Test
  def testShowById(): Unit = {
    when(cache.get).thenReturn(cartItems)

    val result = controller.showById(1).apply(FakeRequest(GET, "/cart/1"))

    assert(status(result) == OK)
    assert(contentAsJson(result) == Json.toJson(firstItem))
  }

  @Test
  def testShowByIdNotFound(): Unit = {
    when(cache.get).thenReturn(cartItems)

    val result = controller.showById(99).apply(FakeRequest(GET, "/cart/99"))

    assert(status(result) == NOT_FOUND)
  }

  @Test
  def testUpdate(): Unit = {
    when(cache.get).thenReturn(cartItems)

    val result = controller.update(1, 10).apply(FakeRequest(PUT, "/cart/1/10"))

    assert(status(result) == OK)
    verify(cache).update(ArgumentMatchers.eq(1), any[CartItem])
  }

  @Test
  def testUpdateNotFound(): Unit = {
    when(cache.get).thenReturn(cartItems)

    val result = controller.update(99, 10).apply(FakeRequest(PUT, "/cart/99/10"))

    assert(status(result) == NOT_FOUND)
  }

  @Test
  def testDelete(): Unit = {
    val result = controller.delete(1).apply(FakeRequest(DELETE, "/cart/1"))

    assert(status(result) == OK)
    verify(cache).delete(1)
  }

  @Test
  def testAdd(): Unit = {
    val newCartItem = CartItem(3, 2)
    val jsonBody = Json.toJson(newCartItem)
    val request = FakeRequest(POST, "/cart")
      .withHeaders("Content-Type" -> "application/json")
      .withBody(jsonBody)

    val result = controller.add()(request)

    assert(status(result) == OK)
    verify(cache).add(any[CartItem])
  }

  @Test
  def testAddInvalidJson(): Unit = {
    val invalidJson = Json.obj("invalid" -> "data")
    val request = FakeRequest(POST, "/cart")
      .withHeaders("Content-Type" -> "application/json")
      .withBody(invalidJson)

    val result = controller.add()(request)

    assert(status(result) == BAD_REQUEST)
  }
}