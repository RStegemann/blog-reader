package websocketservice

import akka.actor.ActorSystem
import akka.Done
import akka.http.scaladsl.Http
import akka.stream.scaladsl.*
import akka.http.scaladsl.model.*
import akka.http.scaladsl.model.ws.*
import akka.util.ByteString
import scala.concurrent.Future

object SocketClient {
  def SendMessage(message: String, socketAddress: String): Unit = {
    implicit val system: ActorSystem = ActorSystem()
    import system.dispatcher

    // Future[Done] is the materialized value of Sink.foreach,
    // emitted when the stream completes
    val incoming: Sink[Message, Future[Done]] =
    Sink.foreach[Message] {
      case message: TextMessage.Strict =>
        println(message.text)
      case _ =>
      // ignore other message types
    }

    // send this as a message over the WebSocket
    val textMessage = TextMessage(message)
    val outgoing = Source.single(textMessage)
    // flow to use (note: not re-usable!)
    val webSocketFlow = Http().webSocketClientFlow(WebSocketRequest(socketAddress))

    // the materialized value is a tuple with
    // upgradeResponse is a Future[WebSocketUpgradeResponse] that
    // completes or fails when the connection succeeds or fails
    // and closed is a Future[Done] with the stream completion from the incoming sink
    val (upgradeResponse, closed) =
    outgoing
      .viaMat(webSocketFlow)(Keep.right) // keep the materialized Future[WebSocketUpgradeResponse]
      .toMat(incoming)(Keep.both) // also keep the Future[Done]
      .run()

    // just like a regular http request we can access response status which is available via upgrade.response.status
    // status code 101 (Switching Protocols) indicates that server support WebSockets
    val connected = upgradeResponse.flatMap { upgrade =>
      if (upgrade.response.status == StatusCodes.SwitchingProtocols) {
        Future.successful(Done)
      } else {
        throw new RuntimeException(s"Connection failed: ${upgrade.response.status}")
      }
    }
  }
}