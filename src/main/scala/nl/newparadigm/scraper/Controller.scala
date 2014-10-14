package nl.newparadigm.scraper

import com.typesafe.scalalogging.Logger

import nl.newparadigm.cli.KvKWebScraper
import nl.newparadigm.kvk.model.{ ProcessingStats, Organisatie, SearchResults, Stats, SearchStats }
import nl.newparadigm.kvk.{ Filter, Search, Pager, PageProcessor, SearchUrl }
import nl.newparadigm.util.Timer

import org.slf4j.LoggerFactory

class Controller(filter: Filter) {

  private val logger = Logger(LoggerFactory.getLogger(classOf[Controller]))

  private lazy val searchUrl = new SearchUrl(filter, 1)
  private lazy val search = new Search(searchUrl)

  def getNumResults() = search.getNumResults()
  def getNumPages() = search.getNumPages()

  def run(): Option[SearchResults] = {
    val timer = new Timer()
    
    timer.start()
    
    search.execute()

    logger.debug(s"KvK web scrapper initialized [results=$getNumResults(), pages=$getNumPages()]")

    if (search.hasResults()) {
      logger.debug("KvK web scrapper loading results [startpage=%s, maxpages=%s]".format(filter.startpage, filter.maxpages))

      val pager = new Pager(filter, search.getNumPages())
      var resultList: List[Organisatie] = List()
      
      // TODO Make multi threaded
      while (pager.hasMorePages()) {
        val pageProcessor = new PageProcessor(pager.nextPage().get)

        //TODO Gracefully handle None
        //TODO When processing multiple pages and one fails does everything fail?
        pageProcessor.process() match {
          case Some(x) => {
            resultList = resultList ::: x
          }
          case None => println("Loading page failed or did not return results [%s]".format(searchUrl.getUrl()))
        }
      }

      timer.stop()
      
      //TODO What happens if processing fails? Handle error correctly
      val searchStats = new SearchStats(search.getNumResults(), search.getNumResults())
      val processingStats = new ProcessingStats(filter.startpage, filter.maxpages, resultList.size)
      val stats = new Stats(timer.execTime(false), searchStats, processingStats)
      
      Some(new SearchResults(KvKWebScraper.api, KvKWebScraper.release, stats, Some(resultList)))
    } else {
      None
    }
  }
}