package com.github.meandor.voyagetransporter.example

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.github.meandor.transporter.platform.Platform.ActionPerformed
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

/**
  * How to transform internal data to json.
  */
trait JsonSupport extends SprayJsonSupport {

  import DefaultJsonProtocol._

  implicit val exampleMatterJsonFormat: RootJsonFormat[ExampleMatter] = jsonFormat2(ExampleMatter)

  implicit val exampleLocationJsonFormat: RootJsonFormat[ExampleLocation] = jsonFormat1(ExampleLocation)

  implicit val actionPerformedJsonFormat: RootJsonFormat[ActionPerformed] = jsonFormat1(ActionPerformed)

  implicit val exampleRequestJsonFormat: RootJsonFormat[ExampleRequest] = jsonFormat2(ExampleRequest)
}
