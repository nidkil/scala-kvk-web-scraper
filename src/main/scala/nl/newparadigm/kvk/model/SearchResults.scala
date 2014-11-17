package nl.newparadigm.kvk.model

import nl.newparadigm.util.Util

import play.api.libs.json._

case class SearchStats(
  val numResults: Int,
  val numPages: Int)

object SearchStats {
  implicit val searchStatsFmt = Json.format[SearchStats]
}

case class ProcessingStats(
  val startPage: Int,
  val endPage: Int,
  val results: Int)

object ProcessingStats {
  implicit val processingStatsFmt = Json.format[ProcessingStats]
}

case class Stats(
  val execTime: String,
  val searchStats: SearchStats,
  val processingStats: ProcessingStats)

object Stats {
  implicit val statsFmt = Json.format[Stats]
}

case class SearchResults(
  val api: String,
  val release: String,
  val stats: Stats,
  var results: Option[List[Organisatie]] = None) {
}

object SearchResults {
  implicit val searchResultsFmt = Json.format[SearchResults]
}