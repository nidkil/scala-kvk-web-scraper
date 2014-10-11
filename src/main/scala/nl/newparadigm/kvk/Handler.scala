package nl.newparadigm.kvk

import scala.util.matching.Regex

import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.HttpStatus
import org.slf4j.LoggerFactory
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import com.fasterxml.jackson.core.JsonParseException

import com.typesafe.scalalogging.Logger

import play.api.libs.json._

class Handler {

  private val logger = Logger(LoggerFactory.getLogger(classOf[Handler]))

}

class PageLoader(searchUrl: String) {

  private val logger = Logger(LoggerFactory.getLogger(classOf[PageLoader]))

  private var error = false

  //TODO Added error checking (HTTP result code and exception handling)
  //TODO Ensure objects are cleaned up correctly in the case of an exception
  def getHtml: Option[String] = {
    logger.debug(s"Retrieving results [$searchUrl]")

    val httpClient = new DefaultHttpClient()
    var inputStream: java.io.InputStream = null
    var html = Option("")

    // This is a hack. Is their a way to initialize the variable directly to None?
    html = None

    try {
      val httpResponse = httpClient.execute(new HttpGet(searchUrl))
      val entity = httpResponse.getEntity()

      if (entity != null) {
        val code = httpResponse.getStatusLine().getStatusCode();

        if (code == HttpStatus.SC_OK) {
          inputStream = entity.getContent()
          val content = io.Source.fromInputStream(inputStream).getLines.mkString

          html = retrieveHtmlFromContent(content)
        } else {
          logger.error(s"Error loading url [HTTP status code $code]")
        }
      } else {
          logger.error(s"Error initializing the HTTP client [$searchUrl]")
      }
    } catch {
      case e @ (_ : IllegalArgumentException | _ : IllegalStateException) => {
        val msg = e.getMessage()
        logger.error(s"Error loading url [$searchUrl]:\n\tException: $msg")
      }
    } finally {
      if (inputStream != null) inputStream.close

      httpClient.getConnectionManager().shutdown()
    }
    
    if(html == None) error = true
    
    html
  }

  def isError: Boolean = error

  private def retrieveHtmlFromContent(content: String): Option[String] = {
    var html = Option("")

    // This is a hack. Is their a way to initialize the variable directly to None?
    html = None
    
    logger.trace(s"Retrieved content:\n$content")

    // Match everything between " (" and ");", the matching groups are ignored
    val pattern = "(?<=\\s\\()(.*)(?=\\);)".r
    val jsonContent = (pattern findFirstIn content)
    // Fix bug that the json specification does not allow a tab character (\t) in a
    // token, it needs to be escaped
    // From the JSON standard [http://www.ecma-international.org/publications/files/ECMA-ST/ECMA-404.pdf]:
    // Insignificant whitespace is allowed before or after any token. The whitespace 
    // characters are: character tabulation (U+0009), line feed (U+000A), carriage 
    // return (U+000D), and space (U+0020). Whitespace is not allowed within any token, 
    // except that space is allowed in strings.
    val jsonFixed = jsonContent.get.replace("\t", "\\t")

    try {
      val jsonEncoded: JsValue = Json.parse(jsonFixed)

      html = Some((jsonEncoded \ "html").as[String])
    } catch {
      case e: JsonParseException => {
        logger.error("Error parsing JSON [" + e.getMessage() + "]:\n" + jsonFixed)
      }
      case e: JsResultException => {
        logger.warn(s"No results found:\n$content")
      }
    }
    
    if(html == None) error = true
    
    html
  }

}

class Init(searchUrl: SearchUrl) {

  private val logger = Logger(LoggerFactory.getLogger(classOf[Init]))

  private var results = -1

  def execute() = {
    logger.debug("Initializing KvK scrapper, determining number of results and pages")

    var pageLoader = new PageLoader(searchUrl.getUrl())
    pageLoader.getHtml match {
      case Some(html) => {
        val doc = Jsoup.parse(html)
        // Find element named strong that descends from a div with class feedback  
        val element = doc.select("div[class=feedback] > strong")

        results = element.first().ownText().toInt
      }
      case None => {
        if (!pageLoader.isError) logger.warn("No results found: " + searchUrl.getUrl())
      }
    }
  }

  def hasResults(): Boolean = if (results == -1) false else true
  def getNumResults(): Int = results
  def getNumPages(): Int = {
    var pages = -1
    if (results != -1) {
      pages = results / HandlerDefaults.RESULTS_PER_PAGE
      if (pages == 0) pages = 1
    }
    pages
  }

}

object HandlerDefaults {
  val RESULTS_PER_PAGE = 10
}
