package websocketservice.actors

import akka.actor.{Actor, ActorRef, Terminated}
import akka.http.scaladsl.model.ws.TextMessage

/**
 * Companion-object for [[BroadcastGroup]]. Defines message-types and keeps track of a list of all clients registered at the
 * [[websocketservice.WebSocketServer]]
 */
object BroadcastGroup {
  private var clients: Set[ActorRef] = Set.empty
  case object Join
  case object JoinAsBroadcaster
  case class SendMessage(message: TextMessage)
}

/**
 * Implements functionality for message events on BroadcastGroup actors.
 */
class BroadcastGroup extends Actor{
  import BroadcastGroup._

  /**
   * Messages to receive
   */
  def receive: Receive = {
    case Join =>
      clients += sender()
      context.watch(sender())
    case JoinAsBroadcaster =>
      context.watch(sender())
    case Terminated(user) =>
      clients -= user
    case msg: SendMessage =>
      clients.foreach(_ ! msg)
  }
}
