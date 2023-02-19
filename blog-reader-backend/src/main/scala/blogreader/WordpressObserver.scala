package blogreader

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.http.scaladsl.unmarshalling.Unmarshal
import scala.concurrent.{Await, Future}
import scala.concurrent.duration.FiniteDuration
import system.dispatcher

class WordpressObserver(requestLink: String, updateTimer: FiniteDuration, requestTimeout: FiniteDuration, responseHandler: (s: String) => Unit){

  system.scheduler.scheduleAtFixedRate(FiniteDuration(0, java.util.concurrent.TimeUnit.SECONDS), updateTimer)(() => {
    requestPosts
  })

  private def requestPosts =
    val responseFuture: Future[HttpResponse] = Http().singleRequest(HttpRequest(uri = requestLink))
    val responseAsString = Await.result(
      responseFuture.flatMap(resp => Unmarshal(resp.entity).to[String]),
      requestTimeout
    )
    responseHandler(responseAsString)
}
