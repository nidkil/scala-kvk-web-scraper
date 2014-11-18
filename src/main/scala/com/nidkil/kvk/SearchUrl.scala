package com.nidkil.kvk

import com.nidkil.util.HttpHelper

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