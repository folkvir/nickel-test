import org.scalatest.funsuite.AnyFunSuite

import scala.collection.mutable.ArrayBuffer

class TestCart extends AnyFunSuite {

  trait Builder {
    var cart: Cart = new Cart()
    val book: Book = new Book(0, "book1", 10)
    val book2: Book = new Book(1, "book2", 8.5)
    val exampleBooks = ArrayBuffer[Book]()
    exampleBooks.addOne(new Book(1, "tome-1", 8))
    exampleBooks.addOne(new Book(1, "tome-1", 8))
    exampleBooks.addOne(new Book(2, "tome-2", 8))
    exampleBooks.addOne(new Book(2, "tome-2", 8))
    exampleBooks.addOne(new Book(3, "tome-3", 8))
    exampleBooks.addOne(new Book(3, "tome-3", 8))
    exampleBooks.addOne(new Book(4, "tome-4", 8))
    exampleBooks.addOne(new Book(5, "tome-5", 8))
    val exampleReductions = new ArrayBuffer[Reduction]()
    exampleReductions.addOne(new Reduction(0.05, 2))
    exampleReductions.addOne(new Reduction(0.10, 3))
    exampleReductions.addOne(new Reduction(0.20, 4))
    exampleReductions.addOne(new Reduction(0.25, 5))
    // (2, 2, 2, 1, 1) => best price is 51.2 (20% + 20%) not 51.6 (25% + 10%)
  }

  test("Testing cart total articles") {
    new Builder() {
      cart.addBook(book)
      assertResult(1)(cart.size())
      cart.addBook(book2)
      assertResult(2)(cart.size())
      cart.addBook(book)
      assertResult(3)(cart.size())
      assertResult(2)(cart.books.size)
      cart.remove(book)
      assertResult(2)(cart.size())
      assertResult(2)(cart.books.size)
    }
  }
  test ("Testing cart total price") {
    new Builder() {
      cart.addBook(book)
      cart.addBook(book2)
      assertResult(18.5: Double)(cart.getFlatPrice())
    }
  }

  test ("Testing the example (2, 2, 2, 1, 1)") {
    new Builder() {
      // make example of the test
      exampleBooks.foreach(book => {
        cart.addBook(book)
      })

      cart.books.update(1, (2, cart.books.get(1).get._2))
      cart.books.update(2, (1, cart.books.get(2).get._2))
      cart.books.update(3, (2, cart.books.get(3).get._2))
      cart.books.update(4, (1, cart.books.get(4).get._2))
      cart.books.update(5, (2, cart.books.get(5).get._2))
      assertResult(2)(cart.books.get(1).get._1) // 2 tome-1
      assertResult(1)(cart.books.get(2).get._1) // 2 tome-2
      assertResult(2)( cart.books.get(3).get._1) // 2 tome-3
      assertResult(1)(cart.books.get(4).get._1) // 1 tome-4
      assertResult(2)(cart.books.get(5).get._1) // 1 tome-1

      // just test to apply a reduction
      assertResult(4)(exampleReductions(2).apply(cart)._2.size())
      assertResult(4)(exampleReductions(2).apply(cart)._2.uniqSize())
      // test flatPrice
      assertResult(64)(cart.getFlatPrice())
      val (price, time) = Calculator.computePriceWithTime(cart, exampleReductions)
      println(s"Time to process for a cart of size ${cart.size()} = $time (ms)")
      assertResult(51.2)(price)
    }
  }

  test ("Testing the example (5, 4, 5, 4, 5)") {
    new Builder() {
      // make example of the test
      exampleBooks.foreach(book => {
        cart.addBook(book)
      })
      // set the example
      cart.books.update(1, (5, cart.books.get(1).get._2))
      cart.books.update(2, (4, cart.books.get(2).get._2))
      cart.books.update(3, (5, cart.books.get(3).get._2))
      cart.books.update(4, (4, cart.books.get(4).get._2))
      cart.books.update(5, (5, cart.books.get(5).get._2))

      // test flatPrice
      assertResult(184)(cart.getFlatPrice())
      val (price, time) = Calculator.computePriceWithTime(cart, exampleReductions)
      println(s"Time to process for a cart of size ${cart.size()} = $time (ms)")
      assertResult(141.2)(price)

      cart.books.update(1, (5, cart.books.get(1).get._2))
      cart.books.update(2, (5, cart.books.get(2).get._2))
      cart.books.update(3, (5, cart.books.get(3).get._2))
      cart.books.update(4, (4, cart.books.get(4).get._2))
      cart.books.update(5, (4, cart.books.get(5).get._2))

      assertResult(184)(cart.getFlatPrice())
      val (price2, time2) = Calculator.computePriceWithTime(cart, exampleReductions)
      println(s"Time to process for a cart of size ${cart.size()} = $time2 (ms)")
      assertResult(141.2)(price2)
    }
  }
}