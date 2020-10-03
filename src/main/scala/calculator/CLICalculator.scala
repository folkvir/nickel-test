package calculator

import java.io.File

import org.backuity.clist.{CliMain, arg}

object CLICalculator extends CliMain[Unit](
  name = "calculator",
  description = "Compute the best price for a list of books and a list of reductions provided in the JSON File"
) {
  var file = arg[File](name = "file", description = "File Path of the JSON Cart+Reductions to load (no verification is done, see example.json for more info)")

  def run: Unit = {
    println(s"Loading file: $file ")
    val (cart, reductions) = Calculator.buildJSON(file)
    val (price, time) = Calculator.computePriceWithTime(cart, reductions)
    println(s"Price: $price")
    println(s"Time(ms): $time")
    println(s"$price,$time")
  }
}
