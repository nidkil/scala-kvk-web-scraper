package nl.newparadigm.cli

import com.typesafe.scalalogging.Logger

import java.io.{File, PrintWriter}

import nl.newparadigm.kvk.FilterConfig
import nl.newparadigm.kvk.Filter
import nl.newparadigm.scraper.Controller

import org.slf4j.LoggerFactory

import play.api.libs.json._

class KvKWebScraper {

  private val logger = Logger(LoggerFactory.getLogger(classOf[KvKWebScraper]))

  private val parser = new scopt.OptionParser[FilterConfig]("kvkzoek") {
    head("\nkvkzoek", KvKWebScraper.release, "\n")
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
    opt[Boolean]('v', "vervallen") action { (x, c) =>
      c.copy(vervallen = x)
    } text ("select 'vervallen'")
    opt[Boolean]('u', "uitgeschreven") action { (x, c) =>
      c.copy(uitgeschreven = x)
    } text ("select 'uitgeschreven'")
    note("\nSome notes:\n")
    opt[String]('f', "file") action { (x, c) =>
      c.copy(file = x)
    } text ("write results to file")
    opt[Unit]("prettyPrint") action { (_, c) =>
      c.copy(prettyPrint = true)
    } text ("prettify JSON results")
    help("help") text ("prints this usage text")
  }

  private def writeToFile(path: String, data: String): Unit = {
    val pw = new java.io.PrintWriter(new File(path))
    try pw.write(data) finally pw.close()
  }
  
  def run(args: Array[String]) {
    logger.info("Initializing CLI")

    parser.parse(args, FilterConfig()) map { config =>
      var filter = new Filter(config)

      logger.debug(s"CLI parser: $filter")

      if (!filter.isValid()) {
        parser.reportError("One of the following options must be specified: handelsnaam, kvknummer, straat, huisnummer, postcode, plaats\nTry --help for more information")

        System.exit(2)
      }

      val controller = new Controller(filter)

      controller.run() match {
        case Some(x) => {
          if (config.prettyPrint) {
            if (config.file.length() == 0) {
              println(Json.prettyPrint(Json.toJson(x)))                            
            } else {
              writeToFile(config.file, Json.prettyPrint(Json.toJson(x)))              
            }
          } else {
            if (config.file.length() == 0) {
              println(Json.stringify(Json.toJson(x)))                            
            } else {
              writeToFile(config.file, Json.stringify(Json.toJson(x)))              
            }
          }

          System.exit(0)
        }
        case None => {
          println("No results")
          System.exit(1)
        }
      }
    } getOrElse {
      //TODO Handle errors
    }

  }

}

object KvKWebScraper {

  val api = "v1"
  val release = "0.1.0"

  def main(args: Array[String]) {
    val scrapper = new KvKWebScraper()

    scrapper.run(args)
  }
}