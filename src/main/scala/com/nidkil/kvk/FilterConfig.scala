package com.nidkil.kvk

import com.nidkil.util.HttpHelper

import FilterDefaults._

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