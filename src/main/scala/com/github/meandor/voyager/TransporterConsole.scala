package com.github.meandor.voyager

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.github.meandor.voyager.httpbin.HttpBinRoutes
import com.lonelyplanet.prometheus.PrometheusResponseTimeRecorder
import com.lonelyplanet.prometheus.api.MetricsEndpoint
import com.typesafe.scalalogging.LazyLogging
import io.prometheus.client.CollectorRegistry

/**
  * Console for the outside world to communicate with the Transporter with.
  *
  * REST-API with all routes
  *
  * @param system ActorSystem from Transporter
  * @param actors Map of all available actors routes should be attached to
  */
case class TransporterConsole(system: ActorSystem, actors: Map[String, ActorRef]) extends HttpBinRoutes with LazyLogging {
  private val prometheusRegistry: CollectorRegistry = PrometheusResponseTimeRecorder.DefaultRegistry

  private val metricsEndpoint = new MetricsEndpoint(prometheusRegistry)

  /**
    * All available routes to be exposed.
    */
  val routes: Route = metricsEndpoint.routes ~ httpBinRoutes
}
