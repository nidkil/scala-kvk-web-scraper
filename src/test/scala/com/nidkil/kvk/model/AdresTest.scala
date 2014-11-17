package com.nidkil.kvk.model

import com.nidkil.util.Util

import org.scalatest._

import com.nidkil.kvk.model.Adres;

import play.api.libs.json._

class AdresTest extends FlatSpec with Matchers {

  "An Adres" should "show all adres fields " in {
    val adres = new Adres(straat = "2e Jerichostraat", huisnummer = Some("16"), huisletter = Some("B1"), huisnummertoevoeging = Some("boven"), postcode = "3061GL", plaats = "Rotterdam")
    adres.toString() should be === ("2e Jerichostraat 16 B1 boven 3061 GL Rotterdam")
  }

  "An Adres" should "convert a compressed zipcode to a zipcode formatted with a space (1234AB > 1234 AB)" in {
    val adres = new Adres(straat = "2e Jerichostraat", postcode = "3061GL", plaats = "Rotterdam")
    adres.toString() should be === ("2e Jerichostraat 3061 GL Rotterdam")
  }

  "An Adres" should "leave a zipcode formatted with a space as is (1234 AB > 1234 AB)" in {
    val adres = new Adres(straat = "2e Jerichostraat", postcode = Util.formatPostcode("3061 GL"), plaats = "Rotterdam")
    adres.toString() should be === ("2e Jerichostraat 3061 GL Rotterdam")
  }

  "An Adres" should "leave an invalid zipcode as is (ABCD12 > ABCD12)" in {
    val adres = new Adres(straat = "2e Jerichostraat", postcode = "ABCD12", plaats = "Rotterdam")
    adres.toString() should be === ("2e Jerichostraat ABCD12 Rotterdam")
  }

  "An Adres" should "generate valid JSON when all properties have a value" in {
    val adres = new Adres(straat = "2e Jerichostraat", huisnummer = Some("16"), huisletter = Some("B1"), huisnummertoevoeging = Some("boven"), postcode = "3061GL", plaats = "Rotterdam")
    val adresJson = Json.toJson(adres)
    Json.stringify(adresJson) should be === ("{\"straat\":\"2e Jerichostraat\",\"huisnummer\":\"16\",\"huisnummerletter\":\"B1\",\"huisnummertoevoeging\":\"boven\",\"postcode\":\"3061 GL\",\"plaats\":\"Rotterdam\"}")
  }

  "An Adres" should "generate valid JSON when some of the properties have a value" in {
    val adres = new Adres(straat = "2e Jerichostraat 16", postcode = "3061GL", plaats = "Rotterdam")
    val adresJson = Json.toJson(adres)
    Json.stringify(adresJson) should be === ("{\"straat\":\"2e Jerichostraat 16\",\"postcode\":\"3061 GL\",\"plaats\":\"Rotterdam\"}")
  }

}