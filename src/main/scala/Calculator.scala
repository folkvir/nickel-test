import scala.collection.mutable.ArrayBuffer

object Calculator {
  def main(args: Array[String]): Unit = {
    // something something
  }

  /**
   * Compute the best price for provided cart with the provided reductions
   * @param cart
   * @param reductions
   * @return
   */
  def computePrice(cart: Cart, reductions: ArrayBuffer[Reduction]): Double = {
    reductions.length match {
      case 0 => cart.getFlatPrice()
      case _ => {
        // remove all reductions that cannot be applied to this cart
        computeBruteForce(cart, reductions.filter(red => red.canBeApplied(cart)))
      }
    }
  }

  /**
   * Produce the best price using a brut force approach,
   * aka we generate all possible solutions of reductions then we choose the smallest price
   * @param cart
   * @param reductions
   * @return
   */
  def computeBruteForce(cart: Cart, reductions: ArrayBuffer[Reduction]): Double = {
     // idea
    // (1) make a tree for each reduction + apply each reduction on the cart of each tree
    // (2) then for each r in Reductions, if we can apply the reduction of r on the cart, make a branch for each success
    // (3) continue on each branch from (2) until the cart is empty

    val trees = new ArrayBuffer[Node[Reduction]]()
    // Create the tree from each reduction, because we need all possible way
    // we use a single root tree here for each reduction, not multi root
    reductions.foreach(reduction => {
      trees.addOne(createTree(cart.clone(), reduction, reductions.clone()))
    })

    // init min to the flat price
    var minimum = cart.getFlatPrice()
    // then for each trees, go to each leaf and take the minimum price
    trees.foreach(tree => {
      val leafes: ArrayBuffer[Double] = getLeafes(tree, ArrayBuffer[Double]())
      minimum = minimum.min(leafes.min)
    })
    minimum
  }

  // Definition of the tree: a Node has a reduction property "node" and has 0 or more Nodes as "sons"
  case class Node[A](node: A, var price: Double, sons: ArrayBuffer[Node[A]]) {
    override def toString: String = s"Node($node, $price, $sons)"
  }

  /**
   * Create a Tree base on the constraint of each reduction for the specified cart and set the final price into each leaf
   * @param cart
   * @param first
   * @param reductions
   * @return
   */
  def createTree(cart: Cart, first: Reduction, reductions: ArrayBuffer[Reduction]): Node[Reduction] = {
    val (price, newCart) = first.apply(cart)
    val tree = new Node(first, price, new ArrayBuffer[Node[Reduction]]())
    // from this step we need to create recursively the tree
    createTreeBis(newCart, tree, reductions)
  }

  /**
   * Recursively create the tree based on Reduction constraint (red.canBeApplied)
   * @param cart
   * @param tree
   * @param reductions
   * @return
   */
  private def createTreeBis(cart: Cart, tree: Node[Reduction], reductions: ArrayBuffer[Reduction]): Node[Reduction] = {
    val filter = reductions.filter(p => p.canBeApplied(cart))
    if (filter.isEmpty) {
      tree.price += cart.getFlatPrice()
      tree
    } else {
      filter.foreach(reduction => {
        // apply the reduction on the cart and continue the creation
        val (price, newCart) = reduction.apply(cart.clone())
        val node = new Node(reduction, tree.price + price, new ArrayBuffer[Node[Reduction]]())
        tree.sons.addOne(createTreeBis(newCart.clone(), node, reductions.clone()))
      })
      tree
    }
  }

  /**
   * Walk through a tree and return an array of all leaf's price
   * @param tree the tree to traverse
   * @param res an empty array of double
   * @return
   */
  def getLeafes (tree: Node[Reduction], res: ArrayBuffer[Double]): ArrayBuffer[Double] = {
    if(tree.sons.length == 0) {
      res.addOne(tree.price)
    } else {
      tree.sons.foreach(s => {
        getLeafes(s, res)
      })
    }
    res
  }

}
