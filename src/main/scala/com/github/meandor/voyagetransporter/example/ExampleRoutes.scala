package com.github.meandor.voyagetransporter.example

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MethodDirectives.post
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.pattern.ask
import akka.util.Timeout
import com.github.meandor.transporter.Metrics
import com.github.meandor.transporter.platform.Platform.{ActionPerformed, Beam}
import com.lonelyplanet.prometheus.PrometheusResponseTimeRecorder
import com.lonelyplanet.prometheus.directives.ResponseTimeRecordingDirectives

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}

trait ExampleRoutes extends JsonSupport {

  implicit def system: ActorSystem

  def actors: Map[String, ActorRef]

  private val responseTimeDirectives = ResponseTimeRecordingDirectives(PrometheusResponseTimeRecorder.Default)

  import responseTimeDirectives._

  implicit lazy val timeout: Timeout = Timeout(5.seconds)

  lazy val exampleRoutes: Route =
    path("example") {
      post {
        recordResponseTime("/example") {
          entity(as[ExampleRequest]) { testRequest =>
            Metrics.beamLag.labels(testRequest.location.id).inc()
            val beam: Future[ActionPerformed] = {
              val examplePadActor = actors.get("examplePad")
              if (examplePadActor.isDefined) {
                (examplePadActor.get ? Beam(testRequest.matter, testRequest.location)).mapTo[ActionPerformed]
              } else {
                Future(ActionPerformed("Actor not found!"))(ExecutionContext.global)
              }
            }

            onSuccess(beam) { performed =>
              Metrics.beamLag.labels(testRequest.location.id).dec()
              complete((StatusCodes.Created, performed))
            }
          }
        }
      }
    }
}
