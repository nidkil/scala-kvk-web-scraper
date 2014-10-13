package nl.newparadigm.kvk.model

import nl.newparadigm.util.Util
import org.scalatest._
import play.api.libs.json._

class OragnisationTest extends FlatSpec with Matchers {

  "An Organisation" should "complete organisation should show all fields " in {
    val adres = new Adres(straat = "2e Jerichostraat", huisnummer = Some("16"), huisletter = Some("B1"), huisnummertoevoeging = Some("boven"), postcode = "3061GL", plaats = "Rotterdam")
    val bestaandeHandelsnamen = Array("orange10", "blue10")
    val vervallenHandelsnamen = Array("orange20", "blue20")
    val organisatie = new Organisatie(
    	handelsnaam = "New Paradigm",
    	statutaireNaam = "New Paradigm BV",
    	Some(bestaandeHandelsnamen),
    	Some(vervallenHandelsnamen),
    	kvkNummer = "12345678",
    	vestigingsnummer = Some("87654321"),
    	samenwerkingsverband = Some("Happy Days"),
    	adres = Some(adres),
    	status = Some("Actief"))
    info(organisatie.toString())
    organisatie.toString() should be === ("handelsnaam=New Paradigm, statutaireNaam=New Paradigm BV, bestaandeHandelsnamen=orange10; blue10, vervallenHandelsnamen=orange20; blue20, kvkNummer=12345678, vestigingsnummer=87654321, hoofdvestiging=false, rechtspersoon=false, samenwerkingsverband=Happy Days, adres=2e Jerichostraat 16 B1 boven 3061 GL Rotterdam, status=Actief")
  }

  "An Organisation" should "generate valid JSON when all properties have a value" in {
    val adres = new Adres(straat = "2e Jerichostraat", huisnummer = Some("16"), huisletter = Some("B1"), huisnummertoevoeging = Some("boven"), postcode = "3061GL", plaats = "Rotterdam")
    val bestaandeHandelsnamen = Array("orange10", "blue10")
    val vervallenHandelsnamen = Array("orange20", "blue20")
    val organisatie = new Organisatie(
    	handelsnaam = "New Paradigm",
    	statutaireNaam = "New Paradigm BV",
    	Some(bestaandeHandelsnamen),
    	Some(vervallenHandelsnamen),
    	kvkNummer = "12345678",
    	vestigingsnummer = Some("87654321"),
    	hoofdvestiging = true,
    	rechtspersoon = true,
    	samenwerkingsverband = Some("Happy Days"),
    	adres = Some(adres),
    	status = Some("Actief"))
    val organisatieJson = Json.toJson(organisatie)
    info(Json.stringify(organisatieJson))
    Json.stringify(organisatieJson) should be === ("{\"handelsnaam\":\"New Paradigm\",\"statutaireNaam\":\"New Paradigm BV\",\"bestaandeHandelsnamen\":[\"orange10\",\"blue10\"],\"vervallenHandelsnamen\":[\"orange20\",\"blue20\"],\"kvkNummer\":\"12345678\",\"vestigingsnummer\":\"87654321\",\"hoofdvestiging\":true,\"rechtspersoon\":true,\"samenwerkingsverband\":\"Happy Days\",\"adres\":{\"straat\":\"2e Jerichostraat\",\"huisnummer\":\"16\",\"huisnummerletter\":\"B1\",\"huisnummertoevoeging\":\"boven\",\"postcode\":\"3061 GL\",\"plaats\":\"Rotterdam\"},\"status\":\"Actief\"}")
  }

  "An Organisation" should "generate valid JSON when only the mandatory properties have a value" in {
    val organisatie = new Organisatie(
    	handelsnaam = "New Paradigm",
    	statutaireNaam = "New Paradigm BV",
    	kvkNummer = "12345678")
    val organisatieJson = Json.toJson(organisatie)
    info(Json.stringify(organisatieJson))
    Json.stringify(organisatieJson) should be === ("{\"handelsnaam\":\"New Paradigm\",\"statutaireNaam\":\"New Paradigm BV\",\"kvkNummer\":\"12345678\",\"hoofdvestiging\":false,\"rechtspersoon\":false}")
  }

  "An Organisation" should "beable to be initialized with only the mandatory properties and then update alle the properties" in {
    val organisatie = new Organisatie(
    	handelsnaam = "New Paradigm",
    	statutaireNaam = "New Paradigm BV",
    	kvkNummer = "12345678")
    val organisatieJson1 = Json.toJson(organisatie)
    info(Json.stringify(organisatieJson1))
    Json.stringify(organisatieJson1) should be === ("{\"handelsnaam\":\"New Paradigm\",\"statutaireNaam\":\"New Paradigm BV\",\"kvkNummer\":\"12345678\",\"hoofdvestiging\":false,\"rechtspersoon\":false}")
    
    organisatie.handelsnaam = "Tjoa Coaching"
    organisatie.statutaireNaam = "Tjoa Coaching"
    organisatie.bestaandeHandelsnamen = Some(Array("Tjoa Coaching"))
    organisatie.vervallenHandelsnamen = Some(Array("Tjoa Consulting"))
    organisatie.kvkNummer = "87654321"
    organisatie.vestigingsnummer = Some("12345678")
    organisatie.hoofdvestiging = true
    organisatie.rechtspersoon = true
    organisatie.samenwerkingsverband = Some("Rainy Nights")
    organisatie.adres = Some(new Adres(straat = "Rodezand 34", postcode = "3011 AN", plaats="Den Haag"))
    organisatie.status = Some("Inactief")
    val organisatieJson2 = Json.toJson(organisatie)
    info(Json.stringify(organisatieJson2))
    Json.stringify(organisatieJson2) should be === ("{\"handelsnaam\":\"Tjoa Coaching\",\"statutaireNaam\":\"Tjoa Coaching\",\"bestaandeHandelsnamen\":[\"Tjoa Coaching\"],\"vervallenHandelsnamen\":[\"Tjoa Consulting\"],\"kvkNummer\":\"87654321\",\"vestigingsnummer\":\"12345678\",\"hoofdvestiging\":true,\"rechtspersoon\":true,\"samenwerkingsverband\":\"Rainy Nights\",\"adres\":{\"straat\":\"Rodezand 34\",\"postcode\":\"3011 AN\",\"plaats\":\"Den Haag\"},\"status\":\"Inactief\"}")
  }
}