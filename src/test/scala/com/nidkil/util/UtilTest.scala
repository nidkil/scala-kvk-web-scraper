package com.nidkil.util

import org.scalatest._

import com.nidkil.util.Util;

class UtilTest extends FlatSpec with Matchers {

  "A addTrailingSpace" should "add a trailing space if a string has a value" in {
    val test = Util.addTrailingSpace("Test")
    test.concat("String").toString() should be ("Test String")
  }

  "A addTrailingSpace" should "not add a trailing space if a string does not have a value" in {
    val test = Util.addTrailingSpace(Some(""))
    test.concat("String").toString() should be ("String")
  }

  "A addTrailingSpace" should "not add a trailing space if a string is None" in {
    val test = Util.addTrailingSpace(None)
    test.concat("String").toString() should be ("String")
  }

  "A addTrailing" should "add a trailing comma and space if a string has a value" in {
    val test = Util.addTrailing("Test", ", ")
    test.concat("String").toString() should be ("Test, String")
  }

  "A addTrailingSpace" should "not add a trailing comma and space if a string does not have a value" in {
    val test = Util.addTrailing(Some(""), ", ")
    test.concat("String").toString() should be ("String")
  }

  "A addTrailingSpace" should "not add a trailing comma and space if a string is None" in {
    val test = Util.addTrailing(None, ", ")
    test.concat("String").toString() should be ("String")
  }

  "A formatPostcode" should "convert a compressed zipcode to a zipcode formatted with a space (1234AB > 1234 AB)" in {
    val adres = Util.formatPostcode("3061GL")
    adres.toString() should be ("3061 GL")
  }

  "A formatPostcode" should "return a zipcode formatted with a space as is (1234 AB > 1234 AB)" in {
    val adres = Util.formatPostcode("3061GL")
    adres.toString() should be ("3061 GL")
  }

  "A formatPostcode" should "return a value that is not a valid zipcode as is" in {
    val adres1 = Util.formatPostcode("")
    adres1.toString() should be ("")
    val adres2 = Util.formatPostcode("aaaa778192")
    adres2.toString() should be ("aaaa778192")
  }

}