package nl.newparadigm.kvk.model

import nl.newparadigm.util.Util

import play.api.libs.json._
import play.api.libs.functional.syntax._

case class Organisatie(
  var handelsnaam: String,
  var statutaireNaam: String,
  var bestaandeHandelsnamen: Option[Array[String]] = None,
  var vervallenHandelsnamen: Option[Array[String]] = None,
  var kvkNummer: String,
  var vestigingsnummer: Option[String] = None,
  var hoofdvestiging: Boolean = false,
  var rechtspersoon: Boolean = false,
  var samenwerkingsverband: Option[String] = None,
  var adres: Option[Adres] = None,
  var status: Option[String] = None) {

	private def getHandelsnamenAsString() = { bestaandeHandelsnamen.getOrElse(Array()).mkString("; ") }

	private def getVervallenHandelsnamenAsString() = { vervallenHandelsnamen.getOrElse(Array()).mkString("; ") }
	
  override def toString(): String =
    Util.template("handelsnaam=", Some(handelsnaam), ", ") +
    Util.template("statutaireNaam=", Some(statutaireNaam), ", ") +
    Util.template("bestaandeHandelsnamen=", Some(getHandelsnamenAsString()), ", ") +
    Util.template("vervallenHandelsnamen=", Some(getVervallenHandelsnamenAsString()), ", ") +
    Util.template("kvkNummer=", Some(kvkNummer), ", ") +
    Util.template("vestigingsnummer=", vestigingsnummer, ", ") +
    Util.template("hoofdvestiging=", Some(hoofdvestiging), ", ") +
    Util.template("rechtspersoon=", Some(rechtspersoon), ", ") +
    Util.template("samenwerkingsverband=", samenwerkingsverband, ", ") +
    Util.template("adres=", adres, ", ") +
    Util.template("status=", status, "")

}

object Organisatie {

  val organisatieWrites: Writes[Organisatie] = (
    (JsPath \ "handelsnaam").write[String] and
    (JsPath \ "statutaireNaam").write[String] and
    (JsPath \ "bestaandeHandelsnamen").writeNullable[Array[String]] and
    (JsPath \ "vervallenHandelsnamen").writeNullable[Array[String]] and
    (JsPath \ "kvkNummer").write[String] and
    (JsPath \ "vestigingsnummer").writeNullable[String] and
    (JsPath \ "hoofdvestiging").write[Boolean] and
    (JsPath \ "rechtspersoon").write[Boolean] and
    (JsPath \ "samenwerkingsverband").writeNullable[String] and
    (JsPath \ "adres").writeNullable[Adres] and
    (JsPath \ "status").writeNullable[String])(unlift(Organisatie.unapply))

  val organisatieReads: Reads[Organisatie] = (
    (JsPath \ "handelsnaam").read[String] and
    (JsPath \ "statutaireNaam").read[String] and
    (JsPath \ "bestaandeHandelsnamen").readNullable[Array[String]] and
    (JsPath \ "vervallenHandelsnamen").readNullable[Array[String]] and
    (JsPath \ "kvkNummer").read[String] and
    (JsPath \ "vestigingsnummer").readNullable[String] and
    (JsPath \ "hoofdvestiging").read[Boolean] and
    (JsPath \ "rechtspersoon").read[Boolean] and
    (JsPath \ "samenwerkingsverband").readNullable[String] and
    (JsPath \ "adres").readNullable[Adres] and
    (JsPath \ "status").readNullable[String])(Organisatie.apply _)

  implicit val organisatiesFormat: Format[Organisatie] =
    Format(organisatieReads, organisatieWrites)

}