package calculator

import java.io.InputStream

import calculator.Readers.{bookReader, reductionReader}
import play.api.libs.json.{JsArray, Json}

import scala.collection.mutable.ArrayBuffer

object Calculator {
  /**
   * Build a tuple (Cart, ArrayBuffer[Reduction]) based on a stream of a JSON file
   * File needs to be formatted with two properties, { books: [..., even empty], reductions: [..., even empty] }
   *
   * @param stream
   * @return
   */
  def buildJSON(stream: InputStream): (Cart, ArrayBuffer[Reduction]) = {
    val json = try {
      Json.parse(stream)
    } finally {
      stream.close()
    }
    val books = (json \ "books").as[JsArray].value.map(book => book.as[Book])
    val reductions = (json \ "reductions").as[JsArray].value.map(red => red.as[Reduction]).to(ArrayBuffer)
    val cart = Cart()
    books.foreach(b => cart.addBook(b))
    (cart, reductions)
  }

  /**
   * Compute the best price for provided cart with the provided reductions
   *
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
   * Compute the price but also the time it took to compute
   *
   * @param cart
   * @param reductions
   * @return
   */
  def computePriceWithTime(cart: Cart, reductions: ArrayBuffer[Reduction]): (Double, Long) = {
    val start = System.currentTimeMillis()
    val price = this.computePrice(cart, reductions)
    (price, System.currentTimeMillis() - start)
  }

  /**
   * Produce the best price using a brut force approach,
   * aka we generate all possible solutions of reductions then we choose the smallest price
   *
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
    var minimum: (Double, Node[Reduction]) = (cart.getFlatPrice(), null)
    // then for each trees, go to each leaf and take the minimum price
    trees.foreach(tree => {
      val leafes: ArrayBuffer[(Double, Node[Reduction])] = getLeafes(tree, ArrayBuffer[(Double, Node[Reduction])]())
      val minLeaf = leafes.minBy(_._1)
      val min = minimum._1.min(minLeaf._1)
      if (min < minimum._1) {
        minimum = minLeaf
      }
    })
    minimum._1
  }

  /**
   * Create a Tree base on the constraint of each reduction for the specified cart and set the final price into each leaf
   *
   * @param cart
   * @param first
   * @param reductions
   * @return
   */
  def createTree(cart: Cart, first: Reduction, reductions: ArrayBuffer[Reduction]): Node[Reduction] = {
    val (price, newCart) = first.applyReduction(cart)
    val tree = Node(null, first, price, new ArrayBuffer[Node[Reduction]]())
    // from this step we need to create recursively the tree
    createTreeBis(newCart, tree, reductions)
  }

  /**
   * Walk through a tree and return an array of all leaf's price
   *
   * @param tree the tree to traverse
   * @param res  an empty array of double
   * @return
   */
  def getLeafes(tree: Node[Reduction], res: ArrayBuffer[(Double, Node[Reduction])]): ArrayBuffer[(Double, Node[Reduction])] = {
    if (tree.sons.length == 0) {
      res.addOne((tree.price, tree))
    } else {
      tree.sons.foreach(s => {
        getLeafes(s, res)
      })
    }
    res
  }

  /**
   * Walk through the path to the root and return every node met
   * The first element is the leaf, the last is the root
   *
   * @param leaf
   * @return an array of all reduction met with their associated price
   */
  def getWalkFromLeaf(leaf: AnyRef): ArrayBuffer[(Reduction, Double)] = {
    var tmp: AnyRef = leaf
    // now we have the min leaf, we can get all the way back to the root
    val walk = new ArrayBuffer[(Reduction, Double)]()
    while (tmp != null) {
      val node: Calculator.Node[Reduction] = tmp.asInstanceOf[Calculator.Node[Reduction]]
      walk.addOne((node.node, node.price))
      tmp = node.parent
    }
    walk
  }

  /**
   * Recursively create the tree based on Reduction constraint (red.canBeApplied)
   *
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
        val (price, newCart) = reduction.applyReduction(cart.clone())
        val node = Node(tree, reduction, tree.price + price, new ArrayBuffer[Node[Reduction]]())
        tree.sons.addOne(createTreeBis(newCart, node, reductions.clone()))
      })
      tree
    }
  }

  // Definition of the tree: a Node has a reduction property "node" and has 0 or more Nodes as "sons"
  case class Node[A](parent: AnyRef, node: A, var price: Double, sons: ArrayBuffer[Node[A]]) {
    override def toString: String = s"Node($node, $price, $sons)"
  }

}
