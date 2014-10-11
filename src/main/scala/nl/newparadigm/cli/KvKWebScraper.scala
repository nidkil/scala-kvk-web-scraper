package nl.newparadigm.cli

import nl.newparadigm.kvk.FilterConfig
import nl.newparadigm.kvk.Filter
import nl.newparadigm.scraper.Controller

import org.slf4j.LoggerFactory
import com.typesafe.scalalogging.Logger

class KvKWebScraper {

  private val logger = Logger(LoggerFactory.getLogger(classOf[KvKWebScraper]))

  val release = "0.1.0"

  val parser = new scopt.OptionParser[FilterConfig]("kvkzoek") {
    head("kvkzoek", release)
    opt[String]('a', "handelsnaam") action { (x, c) =>
      c.copy(handelsnaam = x)
    } text ("name or partial name of an organization to limit the search to")
    opt[String]('k', "kvknummer") action { (x, c) =>
      c.copy(kvknummer = x)
    } text ("valid KvK number to limit the search to")
    opt[String]('t', "straat") action { (x, c) =>
      c.copy(straat = x)
    } text ("street to limit the search to")
    opt[String]('i', "huisnummer") action { (x, c) =>
      c.copy(huisnummer = x)
    } text ("house number to limit the search to")
    opt[String]('z', "postcode") action { (x, c) =>
      c.copy(postcode = x)
    } text ("zipcode to limit the search to")
    opt[String]('p', "plaats") action { (x, c) =>
      c.copy(plaats = x)
    } text ("city to limit the search to")
    opt[Int]('s', "startpage") action { (x, c) =>
      c.copy(startpage = x)
    } text ("page to start processing from")
    opt[Int]('m', "maxpages") action { (x, c) =>
      c.copy(maxpages = x)
    } text ("maximum number of pages to return")
    opt[Boolean]('o', "hoofdvestiging") action { (x, c) =>
      c.copy(hoofdvestiging = x)
    } text ("select 'hoofdvestigingen'")
    opt[Boolean]('n', "nevenvestiging") action { (x, c) =>
      c.copy(nevenvestiging = x)
    } text ("select 'nevenvestiging'")
    opt[Boolean]('r', "rechtspersoon") action { (x, c) =>
      c.copy(rechtspersoon = x)
    } text ("select 'rechtspersoon'")
    opt[Boolean]('r', "vervallen") action { (x, c) =>
      c.copy(vervallen = x)
    } text ("select 'vervallen'")
    opt[Boolean]('r', "uitgeschreven") action { (x, c) =>
      c.copy(uitgeschreven = x)
    } text ("select 'uitgeschreven'")
    note("some notes.\n")
    help("help") text ("prints this usage text")
  }

  def run(args: Array[String]) {
    logger.info("Initializing CLI")
    
    parser.parse(args, FilterConfig()) map { config =>
      var filter = new Filter(config)
      
      logger.debug(s"CLI parser: $filter")
      
      if(!filter.isValid()) {
        parser.reportError("One of the following options must be specified: handelsnaam, kvknummer, straat, huisnummer, postcode, plaats\nTry --help for more information") 
        
        System.exit(2)
      }
      
      val controller = new Controller(filter)
    } getOrElse {
      //TODO Handle errors
    }
    
  }

}

object Main {
  
  def main(args: Array[String]) {
    val scrapper = new KvKWebScraper()

    scrapper.run(args)
  }
}