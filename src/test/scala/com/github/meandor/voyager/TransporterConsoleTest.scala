package com.github.meandor.voyager

import akka.http.scaladsl.model.{HttpRequest, StatusCodes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{FlatSpec, Matchers}

class TransporterConsoleTest extends FlatSpec with Matchers with ScalaFutures with ScalatestRouteTest {
  "Transporter Console" should "start and serve up metrics" in {
    val request = HttpRequest(uri = "/metrics")

    request ~> TransporterConsole(system, Map()).routes ~> check {
      status should ===(StatusCodes.OK)

      entityAs[String] should include(
        "# HELP request_processing_seconds Time spent processing request\n" +
          "# TYPE request_processing_seconds histogram\n")
    }
  }
}
