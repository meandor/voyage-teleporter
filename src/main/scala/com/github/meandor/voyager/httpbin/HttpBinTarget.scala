package com.github.meandor.voyager.httpbin

import com.github.meandor.transporter.Location
import com.github.meandor.transporter.platform.{Energy, Target}
import com.github.meandor.voyager.httpbin.model.HttpBinLocation
import com.typesafe.scalalogging.LazyLogging

object HttpBinTarget extends Target with LazyLogging {
  override def location: Location = HttpBinLocation("HttpBin")

  override def materialize(energy: Energy): Boolean = {
    logger.info(s"materializing: ${energy.toJson}")

    false
  }
}