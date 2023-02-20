package websocketservice.actors

import akka.actor.{Actor, ActorRef}
import akka.http.scaladsl.model.ws.TextMessage

/**
 * Defines Client Actor messages
 */
object Client {
  /**
   * Send when the Client connects to the Websocket
   * @param outgoing Reference to the connecting client
   */
  case class Connected(outgoing: ActorRef)

  /**
   * Send when a client sends an incoming message to the websocket
   * @param message Contained message
   */
  case class IncomingMessage(message: TextMessage)

  /**
   * Send when the websocket sends a message to the client
   * @param message Contained message
   */
  case class OutgoingMessage(message: TextMessage)

  /**
   * Send when a client attempts to broadcast a message
   */
  case class SendBroadcast()
}

/**
 * Creates client actors and implements their messaging behaviour
 * @param broadcastGroup Reference to the [[websocketservice.actors.BroadcastGroup]] to use for broadcast messages
 */
class Client(broadcastGroup: ActorRef) extends Actor{
  import Client._

  /**
   * Messages to receive
   */
  def receive: Receive ={
    case Connected(outgoing: ActorRef) => context.become(connected(outgoing))
    case SendBroadcast() => context.become(connectBroadcaster())
  }

  /**
   * Notifies broadcastgroup of a joining Client and links outgoing message event of Client with BroadCastGroup.SendMessage
   * @param outgoing Reference to joining client
   */
  private def connected(outgoing: ActorRef): Receive = {
    broadcastGroup ! BroadcastGroup.Join
    {
      case BroadcastGroup.SendMessage(text) =>
        outgoing ! OutgoingMessage(text)
    }
  }

  /**
   * Connects a Client as a Broadcaster, forwarding send messages to the broadcastgroup to send them to all registered
   * clients.
   */
  private def connectBroadcaster(): Receive = {
    broadcastGroup ! BroadcastGroup.JoinAsBroadcaster
    {
      case IncomingMessage(text) =>
        broadcastGroup ! BroadcastGroup.SendMessage(text)
    }
  }
}
