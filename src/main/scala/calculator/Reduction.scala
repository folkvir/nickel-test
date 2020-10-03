package calculator

import play.api.libs.json.Json

/**
 * A reduction entry: formulae is price * (1 - reduction)
 *
 * @param reduction [0:1] 0 means this is the full price, 1 it is free, 0.5, half of the price, 0.75 is 1 - 0.75 of the price
 * @param different
 */
case class Reduction(val reduction: Double, val different: Int) {
  /**
   * The reduction can be applied, IFF the number of different books
   *
   * @param cart
   * @return
   */
  def canBeApplied(cart: Cart): Boolean = {
    cart.uniqSize() >= different
  }

  /**
   * Take a cart and return a new price with the cart minus the books took for the reduction
   * It returns the price based on the book price and the reduction
   * sum(for each book = (price * (1 - reduction)))
   *
   * @param cart
   * @return
   */
  def applyReduction(cart: Cart): (Double, Cart) = {
    var price = 0.0
    val newCart: Cart = cart.clone()
    var took = 0
    // should have enough element to loop while
    // we need to sort by number of elements to take first books with the highest count
    val sorted = cart.books.toArray.sortBy(_._2._1).reverse
    while (took < different) {
      val elem = sorted(took)
      newCart.remove(elem._2._2)
      price += elem._2._2.price
      took += 1
    }
    (price * (1 - reduction), newCart)
  }

  override def toString: String = s"calculator.Reduction($reduction, $different)"
}
