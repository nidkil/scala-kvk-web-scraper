package com.nidkil.kvk

class Filter(filterConfig : FilterConfig) extends FilterBase {
  
  import FilterDefaults._
  
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