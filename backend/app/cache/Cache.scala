package cache

import scala.language.reflectiveCalls


class Cache[T <: { def id: Int }] {
  private var items: List[T] = List.empty

  def get: List[T] = items

  def add(item: T): Unit = {
    items = item :: items
  }

  def update(id: Int, updatedItem: T): Unit = {
    items = items.map {
      case item if item.id == id => updatedItem
      case item => item
    }
  }

  def delete(id: Int): Unit = {
    items = items.filterNot(_.id == id)
  }
}



