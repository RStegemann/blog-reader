package model

import blogreader.Util.generateWordmap
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import spray.json.{DefaultJsonProtocol, JsValue, RootJsonFormat}

import scala.collection.mutable

/**
 * Provides Json (un-)marshalling information for [[model.PostWithWordMap]], [[model.RenderedString]] and [[model.Post]]
 */
object PostJsonProtocol extends DefaultJsonProtocol{
  implicit val postWithWordMapFormat: RootJsonFormat[PostWithWordMap] = jsonFormat6(PostWithWordMap.apply)
  implicit val renderedStringFormat: RootJsonFormat[RenderedString] = jsonFormat1(RenderedString.apply)
  implicit val postFormat: RootJsonFormat[Post] = jsonFormat5(Post.apply)
}

/**
 * Class encapsulating a String. Used in unmarshalling of Posts for 'content' and 'excerpt' fields.
 * @param rendered Any String
 */
case class RenderedString(rendered: String)

/**
 * Class representing a wordpress post. Used in unmarshalling of Posts
 * @param id ID of the post on the website
 * @param content Encapsulation of the content-String
 * @param excerpt Encapsulation of teh excerpt-String
 * @param link Link to the post
 * @param title Title of the post
 */
case class Post(id:Int, content: RenderedString, excerpt: RenderedString, link: String, title:RenderedString)

/**
 * Class used to unify Post-Information with the wordcount-map
 * @param id ID of the post on the website
 * @param wordCounts List of Tuples containing the amount of times each word appears in the post content.
 * @param content Content-String as contained in the content.rendered field
 * @param excerpt Excerpt-String as contained in the content.excerpt field
 * @param link Link to the post
 * @param title Title of the post
 */
case class PostWithWordMap(id: Int, wordCounts: List[(String, Int)], content:String, excerpt:String, link:String, title:String)

/**
 * Companion-object to the [[model.PostWithWordMap]]-class, used to generate an object from a json, as well as
 * internally generating a wordcount-map.
 */
object PostWithWordMap{

  /**
   * Takes a parsed JSON-String, generates a wordmap for the content Field of the Post and creates an
   * [[model.PostWithWordMap]]-object.
   * @throws IllegalArgumentException If JSON-Format doesn't correspond to [[model.Post]]-Attributes
   * @param jsValue JSON-String parsed with [[spray.json.JsonParser]]
   * @return [[model.PostWithWordMap]] for the represented Post
   */
  def fromJson(jsValue: JsValue): PostWithWordMap =
    import PostJsonProtocol._
    val post: Post = jsValue.convertTo[Post]
    val cleanedContent: Document = Jsoup.parse(post.content.rendered)
    val wordMap = generateWordmap(cleanedContent.text)
    PostWithWordMap(post.id, wordMap.toList, post.content.rendered, post.excerpt.rendered, post.link, post.title.rendered)

}
