import org.scalatest.funsuite.AnyFunSuite

class TestCart extends AnyFunSuite {

  trait Builder {
    var cart: Cart = new Cart()
    val book: Book = new Book(0, "book1", 10)
    val book2: Book = new Book(1, "book2", 8.5)
  }

  test("Testing cart total articles") {
    new Builder() {
      cart.addBook(book)
      assertResult(1)(cart.size())
      cart.addBook(book2)
      assertResult(2)(cart.size())
    }
  }
  test ("Testing cart total price") {
    new Builder() {
      cart.addBook(book)
      cart.addBook(book2)
      assertResult(18.5: Double)(cart.getFlatPrice())
    }
  }
}