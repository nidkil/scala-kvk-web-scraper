package nl.newparadigm.cli

import nl.newparadigm.kvk.FilterConfig
import nl.newparadigm.kvk.Version

object Parameters {

  val parser = new scopt.OptionParser[FilterConfig]("kvkzoek") {
    head("\nkvkzoek", Version.release, "\n")
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

}