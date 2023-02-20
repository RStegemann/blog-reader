package blogreader

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.http.scaladsl.unmarshalling.Unmarshal
import scala.concurrent.{Await, Future}
import scala.concurrent.duration.FiniteDuration
import system.dispatcher

/**
 * Used to observe a given wordpress website and grab posts according to the requestLink
 * @param requestLink Link to the Wordpress API, containing all the relevant fields and filters
 * @param updateTimer Interval in which to request the data
 * @param requestTimeout Time until request timeout
 * @param responseHandler Handler to process the data
 */
class WordpressObserver(requestLink: String,
                        updateTimer: FiniteDuration,
                        requestTimeout: FiniteDuration,
                        responseHandler: (s: String) => Unit){

  // Start a regular task to send HTTP Requests
  system.scheduler.scheduleAtFixedRate(FiniteDuration(0, java.util.concurrent.TimeUnit.SECONDS), updateTimer)(() => {
    requestPosts()
  })

  /**
   * Sends HTTP-Requests, resolves the Future and sends it to the handler as a String
   */
  private def requestPosts(): Unit =
    val responseFuture: Future[HttpResponse] = Http().singleRequest(HttpRequest(uri = requestLink))
    val responseAsString = Await.result(
      responseFuture.flatMap(resp => Unmarshal(resp.entity).to[String]),
      requestTimeout
    )
    responseHandler(responseAsString)
}
