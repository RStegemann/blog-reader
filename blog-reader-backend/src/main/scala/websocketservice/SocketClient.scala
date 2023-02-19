package websocketservice

import akka.actor.ActorSystem
import akka.Done
import akka.http.scaladsl.Http
import akka.stream.scaladsl.*
import akka.http.scaladsl.model.*
import akka.http.scaladsl.model.ws.*
import akka.util.ByteString
import scala.concurrent.Future

/**
 * Object capable of connecting to a Websocket and sending messages.
 * Used by [[websocketservice.WebSocketServer.sendBroadcastRequest()]] to send broadcasts to connected clients
 */
object SocketClient {

  /**
   * Sends a message to a given websocket address
   * @param message Message to send
   * @param socketAddress Socketaddress
   */
  def SendMessage(message: String, socketAddress: String): Unit = {
    implicit val system: ActorSystem = ActorSystem()
    import system.dispatcher

    // send this as a message over the WebSocket
    val textMessage = TextMessage(message)
    val outgoing = Source.single(textMessage)
    // flow to use (note: not re-usable!)
    val webSocketFlow = Http().webSocketClientFlow(WebSocketRequest(socketAddress))
    outgoing.viaMat(webSocketFlow)(Keep.right).run()
  }
}