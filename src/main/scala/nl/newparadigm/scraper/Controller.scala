package nl.newparadigm.scraper

import nl.newparadigm.kvk.Filter
import nl.newparadigm.kvk.Init
import nl.newparadigm.kvk.SearchUrl

import org.slf4j.LoggerFactory
import com.typesafe.scalalogging.Logger

class Controller(filter: Filter) {
  
  private val logger = Logger(LoggerFactory.getLogger(classOf[Controller]))

  var searchUrl = new SearchUrl(filter)
  var init = new Init(searchUrl)

  init.execute()

  val numResults = init.getNumResults()
  val numPages = init.getNumPages()
  logger.debug(s"KvK scrapper initialized [results=$numResults, pages=$numPages]")
}