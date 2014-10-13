package nl.newparadigm.kvk.model

import nl.newparadigm.util.Util

import play.api.libs.json._
import play.api.libs.functional.syntax._

case class Adres(
  var straat: String = "",
  var huisnummer: Option[String] = None,
  var huisletter: Option[String] = None,
  var huisnummertoevoeging: Option[String] = None,
  var postcode: String = "",
  var plaats: String = "") {

  postcode = Util.formatPostcode(postcode)
  
  override def toString(): String =
    Util.addTrailingSpace(straat) +
      Util.addTrailingSpace(huisnummer) +
      Util.addTrailingSpace(huisletter) +
      Util.addTrailingSpace(huisnummertoevoeging.getOrElse("")) +
      Util.addTrailingSpace(postcode) +
      plaats

}

object Adres {

  val adresWrites: Writes[Adres] = (
    (JsPath \ "straat").write[String] and
    (JsPath \ "huisnummer").writeNullable[String] and
    (JsPath \ "huisnummerletter").writeNullable[String] and
    (JsPath \ "huisnummertoevoeging").writeNullable[String] and
    (JsPath \ "postcode").write[String] and
    (JsPath \ "plaats").write[String])(unlift(Adres.unapply))

  val adresReads: Reads[Adres] = (
    (JsPath \ "straat").read[String] and
    (JsPath \ "huisnummer").readNullable[String] and
    (JsPath \ "huisnummerletter").readNullable[String] and
    (JsPath \ "huisnummertoevoeging").readNullable[String] and
    (JsPath \ "postcode").read[String] and
    (JsPath \ "plaats").read[String])(Adres.apply _)

  implicit val adresFormat: Format[Adres] =
    Format(adresReads, adresWrites)

}