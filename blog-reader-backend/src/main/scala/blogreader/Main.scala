package blogreader
import akka.actor.ActorSystem
import akka.util.ByteString
import model.PostWithWordMap
import model.PostJsonProtocol.postWithWordMapFormat
import websocketservice.{SocketClient, WebSocketServer}
import spray.json.*
import spray.json.DefaultJsonProtocol.*
import com.typesafe.scalalogging.LazyLogging
import concurrent.duration.DurationInt
import scala.io.StdIn.readLine

implicit val system : ActorSystem = ActorSystem()
/**
 * Object used as entrypoint for the backend
 */
object Main {
  // Starts a websocket server on 'ws://localhost:8080'
  private val socketServer = WebSocketServer("localhost", 8080)

  /**
   * Entrypoint
   */
  @main def run(): Unit = {
    val wordpressObserver = WordpressObserver(
      "https://www.thekey.academy/wp-json/wp/v2/posts?_fields=id,title,link,excerpt.rendered,content.rendered",
      5.seconds,
      5.seconds,
      handleObserverResponse)
  }

  /**
   * Callback to process the Respones as obtained from the [[blogreader.WordpressObserver]], transforms them into
   * JSON-Format,creates an Array [[model.PostWithWordMap]]-Instance and sends them to the
   * [[websocketservice.WebSocketServer]] for broadcasting.
   * @param s JSON-String containing wordpress posts
   */
  private def handleObserverResponse(s: String): Unit =
    val something = JsonParser(s)
    val postArray: Array[JsValue] = something.convertTo[Array[JsValue]]
    val postWithMapArray: Array[PostWithWordMap] = postArray.map(postJson => PostWithWordMap.fromJson(postJson))
    val json = postWithMapArray.toJson
    socketServer.sendBroadcastRequest(json.toString)
}
