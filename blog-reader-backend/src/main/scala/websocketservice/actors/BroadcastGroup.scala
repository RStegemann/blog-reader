package websocketservice.actors

import akka.actor.{Actor, ActorRef, Terminated}
import akka.http.scaladsl.model.ws.TextMessage

object BroadcastGroup {
  var clients: Set[ActorRef] = Set.empty
  case object Join
  case object JoinAsBroadcaster
  case class SendMessage(message: TextMessage)
}

class BroadcastGroup extends Actor{
  import BroadcastGroup._

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
