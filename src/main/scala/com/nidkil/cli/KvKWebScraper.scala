package com.nidkil.cli

import java.io.File

import org.slf4j.LoggerFactory

import com.nidkil.kvk.Filter
import com.nidkil.kvk.FilterConfig
import com.nidkil.kvk.Parameters
import com.nidkil.scraper.Controller
import com.typesafe.scalalogging.Logger

import play.api.libs.json.Json

class KvKWebScraper {

  import Parameters._
  
  private val logger = Logger(LoggerFactory.getLogger(classOf[KvKWebScraper]))

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

      controller.run match {
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

object KvKWebScraper extends App {
  val scrapper = new KvKWebScraper()
  scrapper.run(args)
}