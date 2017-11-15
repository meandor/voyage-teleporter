package com.github.meandor.voyager.httpbin

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.github.meandor.transporter.platform.Platform.ActionPerformed
import com.github.meandor.voyager.httpbin.model.{HttpBinLocation, PostMatter, PostRequest, PostsMatter}
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

/**
  * How to transform internal data to json.
  */
trait JsonSupport extends SprayJsonSupport {

  import DefaultJsonProtocol._

  implicit val postsJsonFormat: RootJsonFormat[PostsMatter] = jsonFormat2(PostsMatter)

  implicit val postMatterJsonFormat: RootJsonFormat[PostMatter] = jsonFormat3(PostMatter)

  implicit val httpBinLocationJsonFormat: RootJsonFormat[HttpBinLocation] = jsonFormat1(HttpBinLocation)

  implicit val postRequestJsonFormat: RootJsonFormat[PostRequest] = jsonFormat2(PostRequest)

  implicit val actionPerformedJsonFormat: RootJsonFormat[ActionPerformed] = jsonFormat1(ActionPerformed)
}
