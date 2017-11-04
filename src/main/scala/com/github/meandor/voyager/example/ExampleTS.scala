package com.github.meandor.voyager.example

import com.github.meandor.transporter.Location
import com.github.meandor.transporter.platform.{Energy, Target, TargetingScanner}
import com.typesafe.scalalogging.LazyLogging

object ExampleTarget extends Target with LazyLogging {
  override def location: Location = ExampleLocation("uranus")

  override def materialize(energy: Energy): Boolean = {
    logger.info(s"materializing: ${energy.toJson}")
    energy == ExampleEnergy(ExampleMatter("foo", "bar"))
  }
}

object ExampleTS extends TargetingScanner with LazyLogging {
  override def lockOn(location: Location): Option[Target] = {
    logger.info(s"targeting location: ${location.id}")
    if (location == ExampleLocation("uranus")) {
      Option(ExampleTarget)
    } else {
      None
    }
  }
}
