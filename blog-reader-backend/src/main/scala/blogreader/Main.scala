package blogreader

import akka.util.ByteString
import model.PostWithWordMap
import model.MyJsonProtocol.postWithWordMapFormat
import websocketservice.{SocketClient, WebSocketServer}
import spray.json.*
import spray.json.DefaultJsonProtocol.*

import concurrent.duration.DurationInt

object Main {
  private val socketServer = WebSocketServer("localhost", 8080)
  @main def run(): Unit = {
    val wordpressObserver = WordpressObserver(
      "https://www.thekey.academy/wp-json/wp/v2/posts?_fields=id,title,link,excerpt.rendered,content.rendered",
      5.seconds,
      5.seconds,
      handleObserverResponse)
  }

  private def handleObserverResponse(s: String): Unit =
    val something = JsonParser(s)
    val postArray: Array[JsValue] = something.convertTo[Array[JsValue]]
    val postWithMapArray: Array[PostWithWordMap] = postArray.map(postJson => PostWithWordMap.fromJson(postJson))
    val json = postWithMapArray.toJson
    socketServer.broadcast(json.toString)
    //println("content: " ++ postWithMap.content)
    /*val parsedJson: Either[ParsingFailure, Json] = io.circe.parser.parse(s)
    parsedJson match
      case Right(json) =>
        json.asArray match
          case Some(value: Vector[Json]) =>
            println(value(0).toString)
          case _ =>
            println("Failed conversion.")
        val contents: List[Json] = json.findAllByKey("content")
        for (rendered <- contents(1).findAllByKey("rendered"))
          rendered.asString match
            case Some(rendered) =>
              val jsoup: Document = Jsoup.parse(rendered)
              val words: Array[String] = jsoup.text().split(" ")
              val wordMap: mutable.HashMap[String, Int] = mutable.HashMap()
              for (word: String <- words)
                if (wordMap.contains(word)) wordMap(word) += 1
                else wordMap.addOne(word, 1)
            case None => print("Error")
      case Left(failure) => print(failure)*/
}
