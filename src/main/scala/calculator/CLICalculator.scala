package calculator

import java.io.InputStream

import org.backuity.clist.{CliMain, opt}

import scala.io.Source

/**
 * CLI Object for the Calculator
 */
object CLICalculator extends CliMain[Unit](
  name = "calculator",
  description = "Compute the best price for a list of books and a list of reductions provided in the JSON File"
) {
  var calculator = Calculator
  var defaultPathExample = "/example.json"
  var file = opt[String](default = defaultPathExample, name = "file", description = "File Path of the JSON Cart+Reductions to load (no verification is done, see example.json for more info)")
  var example = opt[Boolean](default = false, description = "Print the example.json and execute the program for this example")

  def run: Unit = {
    def runWithFile(file: InputStream) = {
      val (cart, reductions) = calculator.buildJSON(file)
      val (price, time) = calculator.computePriceWithTime(cart, reductions)
      println(s"Price: $price")
      println(s"Time(ms): $time")
      println(s"$price,$time")
    }

    if (example) {
      // load the example file
      println(Source.fromInputStream(getClass.getResourceAsStream(file)).mkString)
      runWithFile(getClass.getResourceAsStream(file))
    } else {
      if (file == defaultPathExample) {
        System.err.println("Please specify the JSON file path (see --example for more info on the format)")
        System.exit(1)
      } else {
        runWithFile(getClass.getResourceAsStream(file))
      }
    }
  }


}
