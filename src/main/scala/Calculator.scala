
object Calculator {
  def main(args: Array[String]): Unit = {
    // something something
    println("Hello my calculator of reduction: " + args.length)
  }

  def computePrice(cart: Cart): Double = {
    cart.getFlatPrice()
  }
}
