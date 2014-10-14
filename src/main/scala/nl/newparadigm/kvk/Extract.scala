package nl.newparadigm.kvk

import com.fasterxml.jackson.core.JsonParseException

import com.typesafe.scalalogging.Logger

import nl.newparadigm.kvk.model.{ Adres, Organisatie }
import nl.newparadigm.scraper.PageLoader
import nl.newparadigm.util.{JSoup, RegexHelper, Util}

import org.slf4j.LoggerFactory
import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, Element}
import play.api.libs.json._

import scala.collection.mutable.ListBuffer
import scala.util.matching.Regex

class ExtractResult(element: Element) {

  private val logger = Logger(LoggerFactory.getLogger(classOf[ExtractResult]))

  private var kvkNummer = ""
  private var vestigingsnummer = ""
  private var nevenvestiging = false
  private var rechtspersoon = false
  private var adres : Option[Adres] = None

  def getResult() : Organisatie = {
    extractKvKMeta()
    validateMoreSearchInfo()

    val organisatie = new Organisatie(
      handelsnaam = getHandelsnaam(),
      statutaireNaam = getStatutaireNaam(),
      bestaandeHandelsnamen = getBestaandeHandelsnamen(),
      vervallenHandelsnamen = getVervallenHandelsnamen(),
      kvkNummer = getKvkNummer(),
      vestigingsnummer = getVestigingsnummer(),
      hoofdvestiging = getHoofdvestiging(),
      rechtspersoon = getRechtspersoon(),
      samenwerkingsverband = getSamenwerkingsverband(),
      adres = getAdres(),
      status = getStatus())

    organisatie
  }

  private def getHandelsnaam() = element.select("h3.handelsnaamHeader").text()

  private def getKvkNummer() = kvkNummer

  private def getVestigingsnummer() = Some(vestigingsnummer)

  private def getNevenvestiging() = nevenvestiging

  private def getRechtspersoon() = rechtspersoon

  private def getHoofdvestiging() = element.select("a.hoofdvestigingTag").size() > 0

  private def getStatus() = Some(element.select("p.status").text())

  private def getBestaandeHandelsnamen() = Some(extractHandelsnamen(element.select("div.more-search-info h4:contains(Bestaande handelsnamen) + p").text()))

  private def getVervallenHandelsnamen() = Some(extractHandelsnamen(element.select("div.more-search-info h4:contains(Vervallen handelsnamen) + p").text()))

  private def getSamenwerkingsverband() = Some(element.select("div.more-search-info h4:contains(Naam samenwerkingsverband) + p").text())

  private def getStatutaireNaam() = element.select("div.more-search-info h4:contains(Statutaire naam) + p").text()

  private def getAdres() = Some(adres).getOrElse(None) 

  private def extractHandelsnamen(handelsnamen: String): Array[String] = handelsnamen.split("""\s\|\s""")

  private def getKvkMeta() = element.select("ul.kvk-meta li")

  private def getMoreSearchInfo() = element.select("div.more-search-info")

  private def validateMoreSearchInfo() = {
    val elements = getMoreSearchInfo()
    val h4 = elements.select("h4")
    val p = elements.select("p")
    // If number of h4 and p elements do not match log a warning
    if (h4.size() != p.size()) logger.warn("Number of h4 elements and p elements do not match [h4=%s, p=%s]:\n\t%s".format(h4.size(), p.size(), elements))
    // If there are more h4 elements than expected log a warning
    if (h4.size > 4) logger.warn("number of h4 elements exceeds expected number [h4=%s]:\n\t%s".format(h4.size(), elements))
  }

  private def extractKvKMeta() = {
    var adresRegelCnt = 0
    val elements = JSoup._IterableElements(getKvkMeta())
    for (element <- elements) {
      element.text() match {
        case txt if txt.startsWith("KVK") => kvkNummer = txt.substring(4)
        case txt if txt.startsWith("Vestigingsnr.") => vestigingsnummer = txt.substring(14)
        case txt if txt.startsWith("Nevenvestiging") => nevenvestiging = true
        case txt if txt.startsWith("Rechtspersoon") => rechtspersoon = true
        case _ => {
          if (adresRegelCnt > 2) {
            // Ignore empty lines, otherwise log a warning message that an unkown value was found
            if (element.text().length() > 0) logger.warn("Unknown value found [%s]:\n%s".format(element.text(), elements))
          } else {
            if(adres == None) adres = Some(new Adres())
            adresRegelCnt match {
              case 0 => {
                val regexHelper = new RegexHelper()
                val result = regexHelper.splitAdres(element.text())
                
                result.foreach { 
                  case (key, value) if key.equals("straat") => getAdres().get.straat = value                  
                  case (key, value) if key.equals("huisnr") => getAdres().get.huisnummer = Some(value)                  
                  case (key, value) if key.equals("huisletter") => getAdres().get.huisletter = Some(value)                  
                  case (key, value) if key.equals("huisnrtoev") => getAdres().get.huisnummertoevoeging = Some(value)                  
                }
                
              }
              case 1 => getAdres().get.postcode = Util.formatPostcode(element.text())
              case 2 => getAdres().get.plaats = element.text()
            }
            adresRegelCnt += 1
          }
        }
      }
    }
  }

}

//TODO Make extendable class that can be extended by Init and PageProcessor
class PageDecoder() {

  private val logger = Logger(LoggerFactory.getLogger(classOf[PageProcessor]))

  private var noResultsMsg = ""
  private var noResults = false
  private var error = false

  def decode(content: String): Option[String] = {
    logger.trace(s"Retrieved content:\n\t$content")

    var html: Option[String] = None

    // Match everything between " (" and ");", the matching groups are ignored (group 1 & 3)
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

      html = (jsonEncoded \ "html").asOpt[String]

      if (html == None) {
        html = (jsonEncoded \ "error").asOpt[String]

        if (html != None) {
          getNoResultMsg(html.get)

          noResults = true
        }
      }
    } catch {
      case e: JsonParseException => {
        logger.error("Error parsing JSON [%s]:\n\t%s".format(e.getMessage(), jsonFixed), e)
      }
      case e: JsResultException => {
        logger.error("No results found [%s]:\n%s".format(e.getMessage(), content), e)
      }
    }

    if (html == None) error = true

    html
  }

  def hasError() = error

  def getNoResultMsg() = noResultsMsg

  def hasResults() = (noResults == false)

  private def getNoResultMsg(html: String) = {
    val doc = Jsoup.parse(html)
    val msg = doc.select("div.headline").first().ownText()
    val searchValues = doc.select("div.headline b").first().ownText()

    noResultsMsg = "%s: %s".format(msg.substring(0, msg.length() - 1), searchValues)
  }

}

class PageProcessor(searchUrl: SearchUrl) {

  private val logger = Logger(LoggerFactory.getLogger(classOf[PageProcessor]))

  def process() : Option[List[Organisatie]] = {
    logger.debug("Processing page [%s]".format(searchUrl.getUrl()))

    val pageLoader = new PageLoader(searchUrl.getUrl())
    val pageDecoder = new PageDecoder()

    //TODO Handle no results, i.e. move past last results page
    pageDecoder.decode(pageLoader.getContent().get) match {
      case Some(html) => {
        val doc = Jsoup.parse(html)
        // Find element named strong that descends from a div with class feedback  
        val elements = JSoup._IterableElements(doc.select("li.type1"))
        var list = new ListBuffer[Organisatie]()
        
        for (e <- elements) {
          val extractResult = new ExtractResult(e)

          list += extractResult.getResult()
        }
        
        Some(list.toList)
      }
      case None => {
        //TODO Throw custom exception
        if (pageLoader.isError) logger.error("Error occured loading page [" + pageLoader.searchUrl + "]")
        else logger.warn("No results found [" + pageLoader.searchUrl + "]")
        None
      }
    }
  }

}

//TODO Combine with PageProcessor, Init is called by constructor
class Search(searchUrl: SearchUrl) {

  private val logger = Logger(LoggerFactory.getLogger(classOf[Search]))

  private var results = -1
  private var error = false

  def execute() = {
    logger.debug("Initializing KvK scrapper, determining number of results and pages")

    val pageLoader = new PageLoader(searchUrl.getUrl())
    var pageDecoder = new PageDecoder()

    pageDecoder.decode(pageLoader.getContent().get) match {
      case Some(html) => {
        val doc = Jsoup.parse(html)

        if (pageDecoder.hasResults()) {
          // Find element named strong that descends from a div with class feedback  
          val elements = doc.select("div.feedback > strong")

          results = elements.first().ownText().toInt
        } else {
          logger.warn("No results found [%s]: %s".format(pageDecoder.getNoResultMsg(), searchUrl.getUrl()))
        }
      }
      case None => {
        logger.error("Unknown content returned: " + searchUrl.getUrl())
      }
    }
  }

  def hasError() = error

  def hasResults(): Boolean = if (results == -1) false else true

  def getNumResults(): Int = results

  def getNumPages(): Int = {
    var pages = -1
    if (results != -1) {
      pages = results / ExtractDefaults.RESULTS_PER_PAGE
      if (pages == 0) pages = 1
    }
    pages
  }

}

object ExtractDefaults {
  val RESULTS_PER_PAGE = 10
}
