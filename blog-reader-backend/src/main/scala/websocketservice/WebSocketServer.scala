package websocketservice

import akka.NotUsed
import akka.actor.Status.{Failure, Success}
import akka.actor.{Actor, ActorSystem, PoisonPill, Props}
import akka.http.Version.check
import akka.http.scaladsl.{Http, ServerBuilder}
import akka.http.scaladsl.client.RequestBuilding.WithTransformation
import akka.http.scaladsl.model.ws.*
import akka.http.scaladsl.server.Directives
import akka.http.scaladsl.settings.ClientConnectionSettings
import akka.http.scaladsl.testkit.WSTestRequestBuilding.WS
import akka.http.scaladsl.testkit.{ScalatestRouteTest, WSProbe}
import akka.stream.{FlowShape, Inlet, Outlet, OverflowStrategy}
import akka.stream.scaladsl.*
import akka.util.ByteString
import websocketservice.actors.{BroadcastGroup, Client}

import concurrent.duration.DurationInt
import scala.collection.mutable.ListBuffer
import scala.concurrent.{Await, ExecutionContextExecutor, Future}

class WebSocketServer(val hostAddress : String, val port : Int) extends Directives {
  private implicit val system: ActorSystem = ActorSystem()

  private val serverBuilder: ServerBuilder = Http().newServerAt(hostAddress, port)
  private val websocketRoute = path("") {
    handleWebSocketMessages(newClient())
  }
  private val broadcastRoute = path("broadcast"){
    handleWebSocketMessages(broadcast())
  }
  serverBuilder.bind(concat(websocketRoute, broadcastRoute))

  private val broadcastGroup = system.actorOf(Props(BroadcastGroup()), "chat")

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

  def broadcast(message:String): Unit =
    SocketClient.SendMessage(message, "ws://"++hostAddress++":"++port.toString++"/broadcast")
}
