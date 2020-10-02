/**
 * A reduction entry: formulae is price * (1 - reduction)
 * @param reduction [0:1] 0 means this is the full price, 1 it is free, 0.5, half of the price, 0.75 is 1 - 0.75 of the price
 * @param different
 */
class Reduction(val reduction: Double, val different: Int) {
  /**
   * The reduction can be applied, IFF the number of different books
   * @param cart
   * @return
   */
  def canBeApplied(cart: Cart) : Boolean = {
    cart.uniqSize() >= different
  }

  /**
   * Take a cart and return a new price with the cart minus the books took for the reduction
   * It returns the price based on the book price and the reduction
   * sum(for each book = (price * (1 - reduction)))
   * @param cart
   * @return
   */
  def apply(cart: Cart): (Double, Cart) = {
    var price = 0.0
    val newCart: Cart = cart.clone()
    var took = 0
    cart.books.foreach { case (_, (_, book: Book)) => {
      if (took < different) {
        newCart.remove(book)
        price += book.price
        took += 1
      }
    } }
    (price * (1 - reduction), newCart)
  }

  override def toString: String = s"Reduction($reduction, $different)"
}
