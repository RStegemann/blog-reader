package blogreader
import akka.actor.ActorSystem
import akka.http.scaladsl.model.ParsingException
import akka.util.ByteString
import model.PostWithWordMap
import model.PostJsonProtocol.postWithWordMapFormat
import websocketservice.{SocketClient, WebSocketServer}
import spray.json.*
import spray.json.DefaultJsonProtocol.*
import com.typesafe.scalalogging.LazyLogging

import java.net.BindException
import concurrent.duration.DurationInt
import scala.io.StdIn.readLine

implicit val system : ActorSystem = ActorSystem()
/**
 * Object used as entrypoint for the backend
 */
object Main {

  private var socketServer: Option[WebSocketServer] = None

  /**
   * Entrypoint
   */
  @main def run(): Unit = {

    ConnectToServer()
    WordpressObserver(
      "https://www.thekey.academy/wp-json/wp/v2/posts?_fields=id,title,link,excerpt.rendered,content.rendered",
      5.seconds,
      5.seconds,
      handleObserverResponse)
  }

  private def ConnectToServer(): Unit = {
    try {
      // Starts a websocket server on 'ws://localhost:8080'
      socketServer = Option(WebSocketServer("localhost", 8080))
    } catch {
      case bindException: BindException =>
        println("Server connection failed. Please check if TCP channel on endpoint" +
        "localhost:8080 is free and try again")
        system.terminate()
    }
  }

  /**
   * Callback to process the Respones as obtained from the [[blogreader.WordpressObserver]], transforms them into
   * JSON-Format,creates an Array [[model.PostWithWordMap]]-Instance and sends them to the
   * [[websocketservice.WebSocketServer]] for broadcasting.
   * @param s JSON-String containing wordpress posts
   */
  private def handleObserverResponse(s: String): Unit =
    try{
      val parsedJson = JsonParser(s)
      val postArray: Array[JsValue] = parsedJson.convertTo[Array[JsValue]]
      val postWithMapArray: Array[PostWithWordMap] = postArray.map(postJson => PostWithWordMap.fromJson(postJson))
      val json = postWithMapArray.toJson
      socketServer match
        case Some(server: WebSocketServer) => server.sendBroadcastRequest(json.toString)
        case None =>
          println("Serverconnection not found. Terminating.")
          system.terminate()
    }catch{
      case exception: ParsingException => println("Incorrect Json format found. Ignoring result.")
    }
}
