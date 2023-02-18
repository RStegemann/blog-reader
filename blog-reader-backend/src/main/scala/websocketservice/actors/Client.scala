package websocketservice.actors

import akka.actor.{Actor, ActorRef}
import akka.http.scaladsl.model.ws.TextMessage

object Client {
  case class Connected(outgoing: ActorRef)
  case class IncomingMessage(message: TextMessage)
  case class OutgoingMessage(message: TextMessage)
  case class SendBroadcast()
}

class Client(broadcastGroup: ActorRef) extends Actor{
  import Client._

  def receive ={
    case Connected(outgoing: ActorRef) => context.become(connected(outgoing))
    case SendBroadcast() => context.become(connectBroadcaster())
  }

  private def connected(outgoing: ActorRef): Receive = {
    broadcastGroup ! BroadcastGroup.Join
    {
      case BroadcastGroup.SendMessage(text) =>
        outgoing ! OutgoingMessage(text)
    }
  }

  private def connectBroadcaster(): Receive = {
    broadcastGroup ! BroadcastGroup.JoinAsBroadcaster
    {
      case IncomingMessage(text) =>
        broadcastGroup ! BroadcastGroup.SendMessage(text)
    }
  }
}
