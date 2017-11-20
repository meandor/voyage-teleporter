package com.github.meandor.voyager.httpbin

import com.github.meandor.transporter.Location
import com.github.meandor.transporter.platform.{Energy, Target}
import com.github.meandor.voyager.httpbin.PTC.PostEnergy
import com.github.meandor.voyager.httpbin.model.{HttpBinLocation, PostsMatter}
import com.softwaremill.sttp._
import com.typesafe.scalalogging.LazyLogging

object HttpBinTarget extends Target with LazyLogging {

  override def location: Location = HttpBinLocation("http://httpbin.org")

  override def materialize(energy: Energy): Boolean = {
    logger.info(s"materializing: ${energy.toJson}")
    val postEnergy = energy.asInstanceOf[PostEnergy]
    val posts = postEnergy.subject.posts
    val materializeResults = posts.par.map(sendRest)
    materializeResults.foldLeft(true)({
      (acc, response) =>
        val validResponse = validateResponse(response)
        acc & validResponse
    })
  }

  private def sendRest(posts: PostsMatter) = {
    implicit val backend: SttpBackend[Id, Nothing] = HttpURLConnectionBackend()

    val request = sttp.post(uri"http://httpbin.org/post").body(PTC.energize(posts).toJson)
    val response = request.send()
    response.body
  }

  private def validateResponse(response: Either[String, String]) = {
    if (response.isRight) {
      response.right.get contains "\"data\": \"{"
    } else {
      false
    }
  }
}