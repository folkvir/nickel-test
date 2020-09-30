import scala.collection.mutable

class Cart {
  // HashMap of Books, indexed by id, with a tuple of (count, book)
  var books: mutable.HashMap[Int, (Int, Book)] = mutable.HashMap()

  def addBook(book: Book): Unit = {
    if (books contains book.id) {
      val (count, b): (Int, Book) = books.get(book.id).get
      books.update(book.id, (count + 1, b))
    } else {
      books.addOne(book.id, (1, book))
    }
  }

  def size(): Int = {
    books.size
  }

  def remove(book: Book): Boolean = {
    if (books contains book.id) {
      val (count, b): (Int, Book) = books.get(book.id).get
      count match {
        case 1 => books.remove(book.id)
        case _ => books.update(book.id, (count - 1, b))
      }
      true
    } else {
      false
    }
  }

  /**
   *
   * @return
   */
  def getFlatPrice (): Double = {
    val price: Double = books.foldLeft[Double](0.0){
      case (acc, (id, (count, book))) => acc + count * book.price
    }
    price
  }
}
