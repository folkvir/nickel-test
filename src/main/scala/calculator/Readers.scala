package calculator

import play.api.libs.functional.syntax.toFunctionalBuilderOps
import play.api.libs.json.{Reads, __}

object Readers {
  implicit val bookReader: Reads[Book] = (
    (__ \ "id").read[Int] and (__ \ "name").read[String] and (__ \ "price").read[Double]
  )(Book.apply _)

  implicit val reductionReader: Reads[Reduction] = (
    (__ \ "reduction").read[Double] and (__ \ "different").read[Int]
  )(Reduction.apply _)
}
