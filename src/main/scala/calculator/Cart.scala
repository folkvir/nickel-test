package calculator

import scala.collection.mutable

case class Cart() {
  // HashMap of Books, indexed by id, with a tuple of (count, book)
  // better in memory than array 'cause of occurrences
  var books: mutable.HashMap[Int, (Int, Book)] = mutable.HashMap()

  /**
   * Add a new book to the cart
   *
   * @param book
   */
  def addBook(book: Book): Unit = {
    if (books contains book.id) {
      val (count, b): (Int, Book) = books.get(book.id).get
      books.update(book.id, (count + 1, b))
    } else {
      books.addOne(book.id, (1, book))
    }
  }

  /**
   * Get the total number of articles
   *
   * @return
   */
  def size(): Int = {
    books.foldLeft[Int](0) {
      case (acc, (_, (count, _))) => acc + count
    }
  }

  /**
   * Return the number of different articles in the cart
   *
   * @return
   */
  def uniqSize(): Int = this.books.size

  /**
   * Remove a book from the cart, true if removed, false otherwise
   *
   * @param book
   * @return
   */
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
   * Return the flat price of the price without reductions at all
   *
   * @return
   */
  def getFlatPrice(): Double = {
    val price: Double = books.foldLeft[Double](0.0) {
      case (acc, (_, (count, book))) => acc + count * book.price
    }
    price
  }

  /**
   * Clone a cart by returning a new identical instance
   *
   * @return
   */
  override def clone(): Cart = {
    val cart = new Cart()
    cart.books = this.books.clone()
    cart
  }

  override def toString: String = s"calculator.Cart($books)"
}
