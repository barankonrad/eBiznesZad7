package cache

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test;
import org.specs2.control.Properties.aProperty

class CacheTest {

  case class MockItem(id: Int, name: String)

  @Test
  def testUpdateNonExistentItem(): Unit = {
    val cache = new Cache[MockItem]
    val updatedItem = MockItem(99, "Doesn't Exist")

    assertThat(cache.get).isEmpty
    cache.update(99, updatedItem)
    assertThat(cache.get).isEmpty
  }

  @Test
  def testDeleteNonExistentItem(): Unit = {
    val cache = new Cache[MockItem]
    val item = MockItem(1, "Test Item")

    cache.add(item)
    assertThat(cache.get.size).isEqualTo(1)

    cache.delete(99)
    assertThat(cache.get.size).isEqualTo(1)
    assertThat(cache.get.head).isEqualTo(item)
  }

  @Test
  def testCacheOrder(): Unit = {
    val cache = new Cache[MockItem]
    val item1 = MockItem(1, "First")
    val item2 = MockItem(2, "Second")
    val item3 = MockItem(3, "Third")

    cache.add(item1)
    cache.add(item2)
    cache.add(item3)

    val result = cache.get
    assertThat(result.size).isEqualTo(3)
    assertThat(result.head).isEqualTo(item3)
    assertThat(result(1)).isEqualTo(item2)
    assertThat(result(2)).isEqualTo(item1)
  }
}