package com.nidkil.util

import com.typesafe.scalalogging.Logger

import org.slf4j.LoggerFactory

import scala.collection.mutable.Map
import scala.util.matching.Regex
import scala.util.matching.Regex.Match

class RegexHelper {

  private val logger = Logger(LoggerFactory.getLogger(classOf[RegexHelper]))

  object Optional {
    def unapply[T](a: T) = if (null == a) Some(None) else Some(Some(a))
  }

  private def getGroupValue(m: Match, groupIdx: Int, result: Map[String, String], key: String) {
    m.group(groupIdx) match {
      case Optional(Some(value)) => result += (key -> value.trim())
      case Optional(None) => // ignore 
    }
  }

  def splitAdres(str: String): Map[String, String] = {
    val matchesFound = RegexHelper.ADRES_REGEX_PATTERN.r.findAllIn(str)
    val result: Map[String, String] = Map()

    matchesFound.matchData.foreach { m =>
      getGroupValue(m, RegexHelper.GROEP_STRAAT, result, "straat")
      getGroupValue(m, RegexHelper.GROEP_HUISNR, result, "huisnr")
      getGroupValue(m, RegexHelper.GROEP_HUISLETTER, result, "huisletter")
      getGroupValue(m, RegexHelper.GROEP_HUISNR_TOEV, result, "huisnrtoev")

      logger.trace(s"Extracted adres [str=$str, straat=${result.getOrElse("straat", "")}, huisnr=${result.getOrElse("huisnr", "")}, huisletter=${result.getOrElse("huisletter", "")}, huisnrtoev=${result.getOrElse("huisnrtoev", "")}")
    }
    
    result
  }

}

object RegexHelper {

  /**
   * Need a visual regex builder? https://www.debuggex.com/
   * Test Java regex or convert regular regex to Java syntax? http://www.regexplanet.com/advanced/java/index.html
   */

  /**
   * 	^														# begin van de regel
   * 	(														# groep 1: start
   * 		([1-9][e][\s])*					  # groep 2: een getal tussen de 1 en 9 gevolgd door een 'e' en een spatie
   * 		([-/'a-zA-Z\.]+						# groep 3: een streep, enkele quote, kleine letter, hoofdletter of punt
   * 			(												# groep 4: start
   * 				[\.]?[\s]							# groep 5: een optionele punt gevolgd door een spatie
   * 			)?											# groep 4: einde - groep is optioneel
   * 		)+												# groep 4: einde - groep komt 1 of meerdere keren voor
   * 	)														# groep 1: einde
   * 	(														# groep 5: start
   * 		(?:\s*)										# negeer : 0, 1 of meerdere spaties - negeer
   * 		([1-9][0-9]*)							# groep 6: 0, 1 of meerdere getallen, warbij het eerste getal bij 1 begint
   * 		(													# groep 7: start
   * 			(?:[-\s\]*)		   				# negeer : 0, 1 of meerdere spaties, strepen ('-') of back slashes ('\')
   * 			([a-zA-Z])(?![a-zA-Z])	# groep 8: een hoofdletter of kleine letter die NIET gevolgd wordt door een hoofdletter of kleine letter
   * 		)?												# groep 7: einde - groep is optioneel
   * 		(													# start OR
   * 			(?:[-\s\]*)							#
   * 			|												#
   * 			(.*)?										# groep 9: de rest van de regel - groep is optioneel
   * 		)													# einde OR
   * 	)?													# groep 5: einde - groep is optioneel
   * 	$														# einde van de regel
   */
  val ADRES_REGEX_PATTERN = """^(([1-9][e][\s])*([-/'a-zA-Z\.]+([\.]?[\s])?)+)((?:\s*)([1-9][0-9]*)((?:[-\s\\]*)([a-zA-Z])(?![a-zA-Z]))?((?:[-\s\\]*)|(.*)))?$"""
  val GROEP_STRAAT = 1;
  val GROEP_HUISNR = 6;
  val GROEP_HUISLETTER = 8;
  val GROEP_HUISNR_TOEV = 10;

}