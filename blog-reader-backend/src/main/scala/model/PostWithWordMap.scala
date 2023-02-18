package model

import io.circe.Json
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import spray.json.{DefaultJsonProtocol, JsValue, RootJsonFormat}

import scala.collection.mutable

object MyJsonProtocol extends DefaultJsonProtocol{
  implicit val postWithWordMapFormat: RootJsonFormat[PostWithWordMap] = jsonFormat6(PostWithWordMap.apply)
  implicit val renderedStringFormat: RootJsonFormat[RenderedString] = jsonFormat1(RenderedString.apply)
  implicit val postFormat: RootJsonFormat[Post] = jsonFormat5(Post.apply)
}

case class PostWithWordMap(id: Int, wordCounts: List[(String, Int)], content:String, excerpt:String, link:String, title:String)
case class Post(id:Int, content: RenderedString, excerpt: RenderedString, link: String, title:RenderedString)
case class RenderedString(rendered: String)
object PostWithWordMap{
  def fromJson(jsValue: JsValue): PostWithWordMap =
    import MyJsonProtocol._
    val post: Post = jsValue.convertTo[Post]
    val cleanedContent: Document = Jsoup.parse(post.content.rendered)
    val cleanedExcerpt: Document = Jsoup.parse(post.excerpt.rendered)
    val wordMap = generateWordmap(cleanedContent.text)
    PostWithWordMap(post.id, wordMap.toList, post.content.rendered, post.excerpt.rendered, post.link, post.title.rendered)

  private def generateWordmap(content: String): mutable.HashMap[String, Int] =
    val noPunctLowerCase: String = content.replaceAll("""\p{Punct}""", "").toLowerCase
    val words: Array[String] = noPunctLowerCase.split(" ")
    val wordMap: mutable.HashMap[String, Int] = mutable.HashMap()
    words.foreach((word: String) =>{
      if(!word.equals(""))
        if (wordMap.contains(word)) wordMap(word) += 1
        else wordMap.addOne(word, 1)
    })
    return wordMap
}
