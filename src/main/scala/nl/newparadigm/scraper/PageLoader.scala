package nl.newparadigm.scraper

import org.apache.http.HttpStatus
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.DefaultHttpClient
import org.slf4j.LoggerFactory

import com.typesafe.scalalogging.Logger

class PageLoader(val searchUrl: String) {

  private val logger = Logger(LoggerFactory.getLogger(classOf[PageLoader]))

  private var error = false

  def getContent(): Option[String] = {
    logger.debug(s"Retrieving results [$searchUrl]")

    val httpClient = new DefaultHttpClient()
    var inputStream: java.io.InputStream = null
    var content = Option("")

    // This is a hack. Is their a way to initialize the variable directly to None?
    content = None

    try {
      val httpResponse = httpClient.execute(new HttpGet(searchUrl))
      val entity = httpResponse.getEntity()

      if (entity != null) {
        val code = httpResponse.getStatusLine().getStatusCode();

        if (code == HttpStatus.SC_OK) {
          inputStream = entity.getContent()
          content = Some(io.Source.fromInputStream(inputStream).getLines.mkString)
        } else {
          logger.error(s"Error loading url [HTTP status code $code]")
        }
      } else {
        logger.error(s"Error initializing the HTTP client [$searchUrl]")
      }
    } catch {
      case e @ (_: IllegalArgumentException | _: IllegalStateException) => {
        val msg = e.getMessage()
        logger.error(s"Error loading url [$searchUrl]:\n\tException: $msg")
      }
    } finally {
      if (inputStream != null) inputStream.close

      httpClient.getConnectionManager().shutdown()
    }

    if (content == None) error = true

    content
  }

  def isError: Boolean = error

}