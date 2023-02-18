package websocketservice

import akka.NotUsed
import akka.actor.{ActorSystem, PoisonPill, Props}
import akka.http.scaladsl.{Http, ServerBuilder}
import akka.http.scaladsl.model.ws.*
import akka.http.scaladsl.server.Directives
import akka.stream.{FlowShape, OverflowStrategy}
import akka.stream.scaladsl.*
import websocketservice.actors.{BroadcastGroup, Client}
import concurrent.duration.DurationInt

implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global

/**
 * Sets up a Websocket server for message passing
 * @param hostAddress Address to host this server on, i.e. localhost
 * @param port Port to listen on, i.e. 8080
 */
class WebSocketServer(val hostAddress : String, val port : Int) extends Directives {
  private implicit val system: ActorSystem = ActorSystem()

  private val serverBuilder: ServerBuilder = Http().newServerAt(hostAddress, port)
  // Messages on base directory get handled by newClient() function, registering new clients for broadcasts
  private val loginRoute = path("") {
    handleWebSocketMessages(newClient())
  }
  // Messages on "/broadcast" get handled by broadcast() function
  private val broadcastRoute = path("broadcast") {
      handleWebSocketMessages(broadcast())
  }

  serverBuilder.bind(concat(loginRoute, broadcastRoute)).map(_.addToCoordinatedShutdown(hardTerminationDeadline = 10.seconds))

  private val broadcastGroup = system.actorOf(Props(BroadcastGroup()), "chat")

  /**
   * Takes incoming messages from clients and registers them with the broadcast system
   * @return Flow running to Sink.ignore, ignoring incoming messages from clients
   */
  private def newClient(): Flow[Message, Message, NotUsed] = {
    val userActor = system.actorOf(Props(new Client(broadcastGroup)))

    val outgoingMessages: Source[Message, NotUsed] =
      Source.actorRef[Client.OutgoingMessage](PartialFunction.empty, PartialFunction.empty, 10, OverflowStrategy.fail)
        .mapMaterializedValue{ outActor =>
          userActor ! Client.Connected(outActor)
          NotUsed
        }.map(
          (outMsg: Client.OutgoingMessage) => outMsg.message
        )

    Flow.fromSinkAndSource(Sink.ignore, outgoingMessages)
  }

  /**
   * Takes incoming messages on the broadcast directory and sends them out to all registered clients.
   * This would definitely require authentification in a web deployment-situation as clients could send correctly formatted
   * .jsons with malicious code inside the content attribute to distribute malware towards clients.
   * @return Flow representing message distribution
   */
  private def broadcast(): Flow[Message, Message, NotUsed] = {
    val userActor = system.actorOf(Props(new Client(broadcastGroup)))

    val incomingMessages: Sink[Message, NotUsed] =
      Flow[Message].map {
        case tm: TextMessage => Client.IncomingMessage(tm)
        case bm: BinaryMessage => Client.IncomingMessage(bm.asTextMessage.asScala)
      }.to(Sink.actorRef[Client.IncomingMessage](userActor, PoisonPill, (x) => throw x))

    val outgoingMessages: Source[Message, NotUsed] =
      Source.actorRef[Client.OutgoingMessage](PartialFunction.empty, PartialFunction.empty, 10, OverflowStrategy.fail)
        .mapMaterializedValue {outActor =>
          userActor ! Client.SendBroadcast()
          NotUsed
        }.map(
        (outMsg: Client.OutgoingMessage) => outMsg.message
      )
    Flow.fromSinkAndSource(incomingMessages, outgoingMessages)
  }

  /**
   * Sends a broadcast request to the server
   * @param message Message to broadcast
   */
  def sendBroadcastRequest(message:String): Unit =
    SocketClient.SendMessage(message, "ws://"++hostAddress++":"++port.toString++"/broadcast")
}
