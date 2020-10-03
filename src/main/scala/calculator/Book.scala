package calculator

/**
 * Represent a calculator.Book with an id, a name, and a price
 *
 * @param id
 * @param name
 * @param price
 */
case class Book(val id: Int, val name: String, val price: Double) {
  override def toString: String = s"calculator.Book($id, $name, $price)"
}


