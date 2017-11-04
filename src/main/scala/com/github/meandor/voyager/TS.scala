package com.github.meandor.voyager

import com.github.meandor.transporter.Location
import com.github.meandor.transporter.platform.{Target, TargetingScanner}
import com.github.meandor.voyager.httpbin.HttpBinTarget
import com.github.meandor.voyager.httpbin.model.HttpBinLocation
import com.typesafe.scalalogging.LazyLogging

object TS extends TargetingScanner with LazyLogging {
  override def lockOn(location: Location): Option[Target] = {
    logger.info(s"targeting location: ${location.id}")
    location match {
      case HttpBinLocation(_) => Option(HttpBinTarget)
      case _ => None
    }
  }
}
