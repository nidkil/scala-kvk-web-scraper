package com.nidkil.kvk

import com.nidkil.util.HttpHelper

abstract class FilterBase

case class FilterConfig (
  var handelsnaam: String = FilterDefaults.DEFAULT_EMPTY,
  var kvknummer: String = FilterDefaults.DEFAULT_EMPTY,
  var straat: String = FilterDefaults.DEFAULT_EMPTY,
  var huisnummer: String = FilterDefaults.DEFAULT_EMPTY,
  var postcode: String = FilterDefaults.DEFAULT_EMPTY,
  var plaats: String = FilterDefaults.DEFAULT_EMPTY,
  var hoofdvestiging: Boolean = FilterDefaults.DEFAULT_HOOFDVESTIGING,
  var nevenvestiging: Boolean = FilterDefaults.DEFAULT_NEVENVESTIGING,
  var rechtspersoon: Boolean = FilterDefaults.DEFAULT_RECHTSPERSOON,
  var vervallen: Boolean = FilterDefaults.DEFAULT_VERVALLEN,
  var uitgeschreven: Boolean = FilterDefaults.DEFAULT_UITGESCHREVEN,
  var startpage: Int = FilterDefaults DEFAULT_STARTPAGE,
  var maxpages: Int = FilterDefaults DEFAULT_MAXPAGE,
  var prettyPrint: Boolean = false,
  var file: String = FilterDefaults.DEFAULT_EMPTY) extends FilterBase

class Filter(filterConfig : FilterConfig) extends FilterBase {
  
  def isValid(): Boolean = {
    if (handelsnaam != FilterDefaults.DEFAULT_EMPTY ||
      kvknummer != FilterDefaults.DEFAULT_EMPTY ||
      straat != FilterDefaults.DEFAULT_EMPTY ||
      huisnummer != FilterDefaults.DEFAULT_EMPTY ||
      postcode != FilterDefaults.DEFAULT_EMPTY ||
      plaats != FilterDefaults.DEFAULT_EMPTY) true else false
  }

  def handelsnaam = filterConfig.handelsnaam
  def kvknummer = filterConfig.kvknummer
  def straat = filterConfig.straat
  def huisnummer = filterConfig.huisnummer
  def postcode = filterConfig.postcode
  def plaats = filterConfig.plaats
  def hoofdvestiging = filterConfig.hoofdvestiging
  def nevenvestiging = filterConfig.nevenvestiging
  def rechtspersoon = filterConfig.rechtspersoon
  def vervallen = filterConfig.vervallen
  def uitgeschreven = filterConfig.uitgeschreven
  def startpage = filterConfig.startpage
  def maxpages = filterConfig.maxpages
  
  override def toString = s"handelsnaam=$handelsnaam, " +
    s"kvknummer=$kvknummer, " +
    s"straat=$straat, " +
    s"huisnummer=$huisnummer, " +
    s"postcode=$postcode, " +
    s"plaats=$plaats, " +
    s"hoofdvestiging=$hoofdvestiging, " +
    s"nevenvestiging=$nevenvestiging, " +
    s"rechtspersoon=$rechtspersoon, " +
    s"vervallen=$vervallen, " +
    s"uitgeschreven=$uitgeschreven"

}

object FilterDefaults {
  val DEFAULT_EMPTY = ""
  val DEFAULT_HOOFDVESTIGING = true
  val DEFAULT_NEVENVESTIGING = true
  val DEFAULT_RECHTSPERSOON = true
  val DEFAULT_VERVALLEN = false
  val DEFAULT_UITGESCHREVEN = false
  val DEFAULT_STARTPAGE = 1
  val DEFAULT_MAXPAGE = 1
}

class SearchUrl(val filter: Filter, val startPage: Int) extends HttpHelper {

  private val searchUrlTemplate: String = "http://zoeken.kvk.nl/search.ashx?" +
    "callback=jQuery110207092458721687029_1388446807034" +
    "&handelsnaam=%s" +
    "&kvknummer=%s" +
    "&straat=%s" +
    "&postcode=%s" +
    "&huisnummer=%s" +
    "&plaats=%s" +
    "&hoofdvestiging=%s" +
    "&rechtspersoon=%s" +
    "&nevenvestiging=%s" +
    "&vervallen=%s" +
    "&uitgeschreven=%s" +
    "&start=%s" +
    "&initial=0" +
    "&searchfield=uitgebreidzoeken" +
    "&_=1388446807035"

  private def getTrueFalse(value : Boolean) : String = 
    if(value) "true" else "false"
      
  private def getOneZero(value : Boolean) : String = 
    if(value) "1" else "0"

  def getUrl() : String = searchUrlTemplate.format(
      urlEncode(filter.handelsnaam),
      urlEncode(filter.kvknummer),
      urlEncode(filter.straat),
      urlEncode(filter.huisnummer),
      urlEncode(filter.postcode),
      urlEncode(filter.plaats),
      getTrueFalse(filter.hoofdvestiging),
      getTrueFalse(filter.nevenvestiging),
      getTrueFalse(filter.rechtspersoon),
      getOneZero(filter.vervallen),
      getOneZero(filter.uitgeschreven),
      ((startPage - 1) * 10))

}