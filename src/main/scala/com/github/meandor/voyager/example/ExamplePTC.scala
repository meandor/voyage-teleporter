package com.github.meandor.voyager.example

import com.github.meandor.transporter.Matter
import com.github.meandor.transporter.platform.{Energy, PhaseTransitionCoil}
import com.typesafe.scalalogging.LazyLogging

case class ExampleEnergy(subject: Matter) extends Energy {
  override def toJson: String = s"{'id':'${subject.id}','owner':'${subject.owner}'}"
}

object ExamplePTC extends PhaseTransitionCoil with LazyLogging {
  override def energize(subject: Matter): Energy = {
    logger.info(s"Energizing PhaseTransitionCoil with: ${subject.id}, ${subject.owner}")
    ExampleEnergy(subject)
  }
}
