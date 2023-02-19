package blogreader

import scala.collection.mutable

/**
 * Contains methods that are not bound to one specific context
 */
object Util {

  /**
   * Generates a wordcount map for the given String by removing all punctuation characters, converting words
   * to lowercase and adding them a Hashmap.
   *
   * @param content String to count words in
   * @return [[mutable.HashMap]] containing all words and their count
   */
  def generateWordmap(content: String): mutable.HashMap[String, Int] =
    val noPunctLowerCase: String = content.replaceAll("""[\p{Punct}”“–]""", "").toLowerCase
    val words: Array[String] = noPunctLowerCase.split(" ")
    val wordMap: mutable.HashMap[String, Int] = mutable.HashMap()
    words.foreach((word: String) => {
      if (!word.equals(""))
        if (wordMap.contains(word)) wordMap(word) += 1
        else wordMap.addOne(word, 1)
    })
    return wordMap
}
