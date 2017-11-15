package com.github.meandor.voyager.httpbin

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
import com.github.meandor.voyager.httpbin.model.PostRequest
import com.lonelyplanet.prometheus.PrometheusResponseTimeRecorder
import com.lonelyplanet.prometheus.directives.ResponseTimeRecordingDirectives

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}

trait HttpBinRoutes extends JsonSupport {

  implicit def system: ActorSystem

  def actors: Map[String, ActorRef]

  private val responseTimeDirectives = ResponseTimeRecordingDirectives(PrometheusResponseTimeRecorder.Default)

  private val actorName = "httpbin"

  import responseTimeDirectives._

  implicit lazy val timeout: Timeout = Timeout(5.seconds)

  lazy val httpBinRoutes: Route =
    path("httpbin") {
      post {
        recordResponseTime("/httpbin") {
          entity(as[PostRequest]) { postRequest =>
            Metrics.beamLag.labels(postRequest.location.id).inc()
            val beam: Future[ActionPerformed] = {
              val examplePadActor = actors.get(actorName)
              if (examplePadActor.isDefined) {
                (examplePadActor.get ? Beam(postRequest.matter, postRequest.location)).mapTo[ActionPerformed]
              } else {
                Future(ActionPerformed("Actor not found!"))(ExecutionContext.global)
              }
            }

            onSuccess(beam) { performed =>
              Metrics.beamLag.labels(postRequest.location.id).dec()
              complete((StatusCodes.Created, performed))
            }
          }
        }
      }
    }
}
