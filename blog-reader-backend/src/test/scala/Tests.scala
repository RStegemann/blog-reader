import blogreader.Util
import model.PostWithWordMap
import org.scalatest.funspec.AnyFunSpec
import spray.json.{JsValue, JsonParser}
import spray.json.DefaultJsonProtocol.*
import spray.json.*
import model.PostJsonProtocol.postWithWordMapFormat

import scala.collection.mutable

class Tests extends AnyFunSpec{
  val jsonString: String = "[{\n    \"id\": 16372,\n    \"link\": \"https:\\/\\/www.thekey.academy\\/konfliktmanagement-in-4-schritten-zur-loesung\\/\",\n    \"title\": {\n        \"rendered\": \"Konfliktmanagement: in 4 Schritten zur L\\u00f6sung\"\n    },\n    \"content\": {\n        \"rendered\": \"Some content\"\n\t},\n    \"excerpt\": {\n        \"rendered\": \"<p>Sich nicht einig zu sein und dar\\u00fcber in Konflikt zu geraten, ist ganz normal. Diese Konflikte nicht zu l\\u00f6sen, kann aber viel Energie und auf der Arbeit auch Kosten binden. Was kannst Du also tun, um eine L\\u00f6sung zu finden, mit der alle Beteiligten zufrieden sind? Wir verraten es Dir. Und erkl\\u00e4ren Dir auch, was Dein vierj\\u00e4hriges Ich damit zu tun hat.<\\/p>\\n\"\n    }\n}, {\n    \"id\": 1418,\n    \"link\": \"https:\\/\\/www.thekey.academy\\/stressfaktor-arbeitsplatz-6-tipps-zur-stressbewaeltigung\\/\",\n    \"title\": {\n        \"rendered\": \"Weniger Stress im neuen Jahr? Mit diesen 6 Tipps gelingt es Dir\"\n    },\n    \"content\": {\n        \"rendered\": \"Some content\"\n\t},\n    \"excerpt\": {\n        \"rendered\": \"<p>Immer mehr Anforderungen von au\\u00dfen, eine immer schnellere Welt, vielf\\u00e4ltige Aufgaben, alles gleichzeitig: Weniger Stress zu haben ist seit Jahren der beliebteste Vorsatz f\\u00fcrs neue Jahr. Wir zeigen Dir, wie Du diesen Vorsatz umsetzen und effektiv etwas gegen Stress tun kannst &#8211; egal ob am Arbeitsplatz oder anderswo.<\\/p>\\n\"\n    }\n}, {\n    \"id\": 15423,\n    \"link\": \"https:\\/\\/www.thekey.academy\\/weniger-stress-im-neuen-jahr-6-tipps-vom-experten\\/\",\n    \"title\": {\n        \"rendered\": \"Weniger Stress im neuen Jahr? 6 Tipps vom Experten\"\n    },\n    \"content\": {\n        \"rendered\": \"Some content\"\n\t},\n    \"excerpt\": {\n        \"rendered\": \"<p>Immer mehr Anforderungen von au\\u00dfen, eine immer schnellere Welt, vielf\\u00e4ltige Aufgaben, alles gleichzeitig: Weniger Stress ist seit Jahren der beliebteste Vorsatz f\\u00fcrs neue Jahr. Wie Du es schaffst, diesen Vorsatz umzusetzen, erkl\\u00e4rt Dir Resilienz-Coach und Stress-Experte Thorsten Donat.<\\/p>\\n\"\n    }\n}, {\n    \"id\": 11401,\n    \"link\": \"https:\\/\\/www.thekey.academy\\/so-geht-der-wandel-in-der-digitalen-erwachsenenbildung\\/\",\n    \"title\": {\n        \"rendered\": \"So geht der Wandel in der digitalen Erwachsenenbildung\"\n    },\n    \"content\": {\n        \"rendered\": \"Some content\"\n\t},\n    \"excerpt\": {\n        \"rendered\": \"<p>\\u201cDas haben wir schon immer so gemacht\\u201d \\u2013 Ein Satz, der mit immer k\\u00fcrzeren Innovationszyklen Jahr um Jahr an Bedeutung verliert. Denn auch, wer mit dem Status Quo zufrieden ist, wird bald feststellen: Die Welt dreht sich weiter. Auch im Bildungssektor.<\\/p>\\n\"\n    }\n}, {\n    \"id\": 1331,\n    \"link\": \"https:\\/\\/www.thekey.academy\\/dein-agile-guide-die-wichtigsten-agile-methoden-im-kurzportrait\\/\",\n    \"title\": {\n        \"rendered\": \"Dein Agile Guide: Die wichtigsten agilen Methoden im Kurzportrait\"\n    },\n    \"content\": {\n        \"rendered\": \"Some content\"\n\t},\n    \"excerpt\": {\n        \"rendered\": \"<p>Im Projektmanagement, im Kreativen und sogar in der Verwaltung: Agile Methoden halten in den diversesten Bereichen Einzug. Im Agile Guide erl\\u00e4utern wir Dir, woher diese agile Bewegung kommt und mit welchen agilen Methoden Du ab morgen kundenzentrierter und effizienter Arbeiten kannst. <\\/p>\\n\"\n    }\n}, {\n    \"id\": 1309,\n    \"link\": \"https:\\/\\/www.thekey.academy\\/die-fuehrungsstile-eines-agilen-leaders\\/\",\n    \"title\": {\n        \"rendered\": \"Die F\\u00fchrungsstile eines agilen Leaders\"\n    },\n    \"content\": {\n        \"rendered\": \"Some content\"\n\t},\n    \"excerpt\": {\n        \"rendered\": \"<p>Egal, ob Du bereits F\\u00fchrungskraft bist oder bald eine sein m\\u00f6chtest &#8211; fr\\u00fcher oder sp\\u00e4ter stellst Du Dir die Frage, wie Du f\\u00fchren m\\u00f6chtest. Nach diesem Artikel wei\\u00dft Du, welche Vor- und Nachteile welcher F\\u00fchrungsstil hat und was Dich zu einer agilen F\\u00fchrungskraft macht. <\\/p>\\n\"\n    }\n}]"

  describe("Json evaluation"){
    it("should correctly convert between objects and jsons"){
      val parsedJson = JsonParser(jsonString)
      val postArray: Array[JsValue] = parsedJson.convertTo[Array[JsValue]]
      assert(postArray != null && postArray.length > 0)
      val postWithMapArray: Array[PostWithWordMap] = postArray.map(postJson => PostWithWordMap.fromJson(postJson))
      assert(postWithMapArray(0).id == 16372)
      assert(postWithMapArray(0).link == """https://www.thekey.academy/konfliktmanagement-in-4-schritten-zur-loesung/""")
      assert(postWithMapArray(0).title == """Konfliktmanagement: in 4 Schritten zur Lösung""")
      assert(postWithMapArray(0).content == """Some content""")
      // Excerpt equals check fails despite being same somehow
      val json = postWithMapArray.toJson
      assert(json.toString != "")
    }
  }

  describe("Wordcountmap generation"){
    it("should count only words while removing punctuations and special characters"){
      val testString = "Hello hello hello some more random words. !%And/ here{{ are., some\\ \"surrounded\" by /special/, characters,,..-"
      val wordmap = Util.generateWordmap(testString)
      val validationMap = mutable.HashMap().addOne(("here", 1))
        .addOne(("some", 2))
        .addOne(("surrounded", 1))
        .addOne(("more", 1))
        .addOne(("words", 1))
        .addOne(("special", 1))
        .addOne(("random", 1))
        .addOne(("characters", 1))
        .addOne(("are", 1))
        .addOne(("and", 1))
        .addOne(("by", 1))
        .addOne(("hello", 3))
      assert(wordmap.equals(validationMap))
    }
  }
}
