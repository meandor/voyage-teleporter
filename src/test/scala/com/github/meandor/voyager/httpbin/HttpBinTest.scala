package com.github.meandor.voyager.httpbin

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model.{ContentTypes, MessageEntity, StatusCodes}
import akka.http.scaladsl.testkit.{RouteTestTimeout, ScalatestRouteTest}
import com.github.meandor.transporter.platform.Platform
import com.github.meandor.voyager.TS
import com.github.meandor.voyager.httpbin.model.{HttpBinLocation, PostMatter, PostRequest, PostsMatter}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{FeatureSpec, Matchers}

import scala.concurrent.duration._

class HttpBinTest extends FeatureSpec with Matchers with ScalatestRouteTest with ScalaFutures with HttpBinRoutes {

  val httpBinPad: ActorRef = system.actorOf(Platform.props(PTC, TS), "httpbin")

  override val actors = Map("httpbin" -> httpBinPad)

  feature("Export data to https://httpbin.org/") {
    scenario("Post a collection of data to https://httpbin.org/") {

      implicit def default(implicit system: ActorSystem) = RouteTestTimeout(10.second)

      val postRequest = PostRequest(HttpBinLocation("some"), PostMatter("42", "bar", Seq(PostsMatter("foobar", "foobaz"), PostsMatter("foobar42", "foobaz42"))))
      val postEntity = Marshal(postRequest).to[MessageEntity].futureValue

      Post("/httpbin").withEntity(postEntity) ~> httpBinRoutes ~> check {
        status shouldBe StatusCodes.Created

        // we expect the response to be json:
        contentType shouldBe ContentTypes.`application/json`

        // and we know what message we're expecting back:
        entityAs[String] shouldBe "{\"description\":\"Successful Beam\"}"
      }
    }
  }
}
