class Book(val id: Int, val name: String, val price: Double) {
  override def toString: String = s"Book($id, $name, $price)"
}
