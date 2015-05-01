package actors

import akka.actor._
import akka.cluster.ClusterEvent._
import akka.cluster._
import com.typesafe.config.ConfigFactory
import models.Member._
import play.api.libs.json.Json._
import play.api.libs.json._

/**
 * Created by the playframework for a websocket connection.
 * Listens to MemberEvents and pushs them to the websocket.
 *
 * @param out - the websocket to which we can push messages
 */
class MonitorActor(out: ActorRef, system: ActorSystem) extends Actor with ActorLogging {

  val cluster = Cluster(system)


  // subscribe to cluster changes, re-subscribe when restart 
  override def preStart(): Unit = {
    cluster.subscribe(self, initialStateMode = InitialStateAsEvents,
      classOf[MemberEvent], classOf[UnreachableMember])
  }

  // clean up on shutdown
  override def postStop(): Unit = cluster unsubscribe self

  // handle the member events
  def receive = {
    case MemberUp(member) => handleMemberUp(member)
    case UnreachableMember(member) => handleUnreachable(member)
    case MemberRemoved(member, previousStatus) => handleRemoved(member, previousStatus)
    case MemberExited(member) => handleExit(member)
    case _: MemberEvent => // ignore
  }

  def handleMemberUp(member: Member) {
    out ! (Json.obj("state" -> "up") ++ toJson(member).as[JsObject])
  }

  def handleUnreachable(member: Member) {
    out ! (Json.obj("state" -> "unreachable") ++ toJson(member).as[JsObject])

  }

  def handleRemoved(member: Member, previousStatus: MemberStatus) {
    out ! (Json.obj("state" -> "removed") ++ toJson(member).as[JsObject])
  }

  def handleExit(member: Member) {
    out ! (Json.obj("state" -> "exit") ++ toJson(member).as[JsObject])
  }

}


object MonitorActor {

  /**
   * Definition for the controller to create the websocket
   */
  def props(out: ActorRef, system: ActorSystem) = {
    Props(new MonitorActor(out, system))
  }
}
