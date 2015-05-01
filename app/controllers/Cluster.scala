package controllers

import actors._
import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import models.Metrics
import play.api.Play.current
import play.api.libs.json.JsValue
import play.api.mvc._

object Cluster extends Controller {
  val system = ActorSystem.create("HajpCluster", ConfigFactory.load().getConfig("HajpCluster"))

  def clusterNodesWebsocket = WebSocket.acceptWithActor[JsValue, JsValue] { implicit request =>
    MonitorActor.props(_, system)
  }

  def clusterMetricsWebsocket = WebSocket.acceptWithActor[JsValue, Metrics.NodeMetric] { implicit request =>
    MetricsActor.props(_, system)
  }
}
