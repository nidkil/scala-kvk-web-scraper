package com.nidkil.util

import org.scalatest._

import com.nidkil.util.RegexHelper;

class RegexHelperTest extends FlatSpec with Matchers {

  "A adresMatcher" should "extract the different parts of an address based on a regex pattern" in {
    val regexHelper = new RegexHelper()
    testData.foreach { data =>
      {
        val result = regexHelper.splitAdres(data.test)
        val diffSet = (result.toSet diff data.expected.toSet)
        
        if(diffSet.size > 0) {
          println(s"Match failed:\n\t- test=${data.test}\n\t- expected=${data.expected}\n\t- result=${result}")
        }
        
        diffSet should be(Set.empty)
      }
    }
  }

  private def testData(): List[AdresMatchTestData] = {
    val data = List(
      AdresMatchTestData("2e Jerichostraat", Map("straat" -> "2e Jerichostraat")),
      AdresMatchTestData("2e Jerichostraat 1", Map("straat" -> "2e Jerichostraat", "huisnr" -> "1")),
      AdresMatchTestData("2e Jerichostraat 16", Map("straat" -> "2e Jerichostraat", "huisnr" -> "16")),
      AdresMatchTestData("2e Jerichostraat 161", Map("straat" -> "2e Jerichostraat", "huisnr" -> "161")),
      AdresMatchTestData("2e Jerichostraat    1", Map("straat" -> "2e Jerichostraat", "huisnr" -> "1")),
      AdresMatchTestData("2e Jerichostraat       16", Map("straat" -> "2e Jerichostraat", "huisnr" -> "16")),
      AdresMatchTestData("2e Jerichostraat          161", Map("straat" -> "2e Jerichostraat", "huisnr" -> "161")),
      AdresMatchTestData("2e Jerichostraat 1-", Map("straat" -> "2e Jerichostraat", "huisnr" -> "1")),
      AdresMatchTestData("2e Jerichostraat 16-", Map("straat" -> "2e Jerichostraat", "huisnr" -> "16")),
      AdresMatchTestData("2e Jerichostraat 161-", Map("straat" -> "2e Jerichostraat", "huisnr" -> "161")),
      AdresMatchTestData("2e Jerichostraat    1 -", Map("straat" -> "2e Jerichostraat", "huisnr" -> "1")),
      AdresMatchTestData("2e Jerichostraat       16 -", Map("straat" -> "2e Jerichostraat", "huisnr" -> "16")),
      AdresMatchTestData("2e Jerichostraat          161 -", Map("straat" -> "2e Jerichostraat", "huisnr" -> "161")),
      AdresMatchTestData("2e Jerichostraat 100", Map("straat" -> "2e Jerichostraat", "huisnr" -> "100")),
      AdresMatchTestData("2e Jerichostraat 100-200", Map("straat" -> "2e Jerichostraat", "huisnr" -> "100", "huisnrtoev" -> "-200")),
      AdresMatchTestData("2e Jerichostraat 100 -200", Map("straat" -> "2e Jerichostraat", "huisnr" -> "100", "huisnrtoev" -> "-200")),
      AdresMatchTestData("2e Jerichostraat 100- 200", Map("straat" -> "2e Jerichostraat", "huisnr" -> "100", "huisnrtoev" -> "- 200")),
      AdresMatchTestData("2e Jerichostraat 100 - 200", Map("straat" -> "2e Jerichostraat", "huisnr" -> "100", "huisnrtoev" -> "- 200")),
      AdresMatchTestData("2e Jerichostraat 100-200-300", Map("straat" -> "2e Jerichostraat", "huisnr" -> "100", "huisnrtoev" -> "-200-300")),
      AdresMatchTestData("2e Jerichostraat 100 - 200-300", Map("straat" -> "2e Jerichostraat", "huisnr" -> "100", "huisnrtoev" -> "- 200-300")),
      AdresMatchTestData("2e Jerichostraat 100-200  -  300", Map("straat" -> "2e Jerichostraat", "huisnr" -> "100", "huisnrtoev" -> "-200  -  300")),
      AdresMatchTestData("2e Jerichostraat 100   -   200   -   300", Map("straat" -> "2e Jerichostraat", "huisnr" -> "100", "huisnrtoev" -> "-   200   -   300")),
      AdresMatchTestData("2e Jerichostraat 100\\200", Map("straat" -> "2e Jerichostraat", "huisnr" -> "100", "huisnrtoev" -> "\\200")),
      AdresMatchTestData("2e Jerichostraat 100 \\200", Map("straat" -> "2e Jerichostraat", "huisnr" -> "100", "huisnrtoev" -> "\\200")),
      AdresMatchTestData("2e Jerichostraat 100\\ 200", Map("straat" -> "2e Jerichostraat", "huisnr" -> "100", "huisnrtoev" -> "\\ 200")),
      AdresMatchTestData("2e Jerichostraat 100 \\ 200", Map("straat" -> "2e Jerichostraat", "huisnr" -> "100", "huisnrtoev" -> "\\ 200")),
      AdresMatchTestData("2e Jerichostraat 100   \\   200", Map("straat" -> "2e Jerichostraat", "huisnr" -> "100", "huisnrtoev" -> "\\   200")),
      AdresMatchTestData("2e Jerichostraat 1-rest", Map("straat" -> "2e Jerichostraat", "huisnr" -> "1", "huisnrtoev" -> "-rest")),
      AdresMatchTestData("2e Jerichostraat 1 -rest", Map("straat" -> "2e Jerichostraat", "huisnr" -> "1", "huisnrtoev" -> "-rest")),
      AdresMatchTestData("2e Jerichostraat 1- rest", Map("straat" -> "2e Jerichostraat", "huisnr" -> "1", "huisnrtoev" -> "- rest")),
      AdresMatchTestData("2e Jerichostraat 1 - rest", Map("straat" -> "2e Jerichostraat", "huisnr" -> "1", "huisnrtoev" -> "- rest")),
      AdresMatchTestData("2e Jerichostraat 16 20", Map("straat" -> "2e Jerichostraat", "huisnr" -> "16", "huisnrtoev" -> "20")),
      AdresMatchTestData("2e Jerichostraat 16 20 30", Map("straat" -> "2e Jerichostraat", "huisnr" -> "16", "huisnrtoev" -> "20 30")),
      AdresMatchTestData("2e Jerichostraat 16 20 30 A", Map("straat" -> "2e Jerichostraat", "huisnr" -> "16", "huisnrtoev" -> "20 30 A")),
      AdresMatchTestData("2e Jerichostraat 16 20 30A", Map("straat" -> "2e Jerichostraat", "huisnr" -> "16", "huisnrtoev" -> "20 30A")),
      AdresMatchTestData("2e Jerichostraat 16 20 30 a", Map("straat" -> "2e Jerichostraat", "huisnr" -> "16", "huisnrtoev" -> "20 30 a")),
      AdresMatchTestData("2e Jerichostraat 16 20 30a", Map("straat" -> "2e Jerichostraat", "huisnr" -> "16", "huisnrtoev" -> "20 30a")),
      AdresMatchTestData("2e Jerichostraat 16 20-30", Map("straat" -> "2e Jerichostraat", "huisnr" -> "16", "huisnrtoev" -> "20-30")),
      AdresMatchTestData("2e Jerichostraat 16 20- 30", Map("straat" -> "2e Jerichostraat", "huisnr" -> "16", "huisnrtoev" -> "20- 30")),
      AdresMatchTestData("2e Jerichostraat 16 20 -30", Map("straat" -> "2e Jerichostraat", "huisnr" -> "16", "huisnrtoev" -> "20 -30")),
      AdresMatchTestData("2e Jerichostraat 16 A", Map("straat" -> "2e Jerichostraat", "huisnr" -> "16", "huisletter" -> "A")),
      AdresMatchTestData("2e Jerichostraat 16A", Map("straat" -> "2e Jerichostraat", "huisnr" -> "16", "huisletter" -> "A")),
      AdresMatchTestData("2e Jerichostraat 16 Ab", Map("straat" -> "2e Jerichostraat", "huisnr" -> "16", "huisnrtoev" -> "Ab")),
      AdresMatchTestData("2e Jerichostraat 16Ab", Map("straat" -> "2e Jerichostraat", "huisnr" -> "16", "huisnrtoev" -> "Ab")),
      AdresMatchTestData("2e Jerichostraat 16 A b", Map("straat" -> "2e Jerichostraat", "huisnr" -> "16", "huisletter" -> "A", "huisnrtoev" -> "b")),
      AdresMatchTestData("2e Jerichostraat 16AB", Map("straat" -> "2e Jerichostraat", "huisnr" -> "16", "huisnrtoev" -> "AB")),
      AdresMatchTestData("2e Jerichostraat 16 A B", Map("straat" -> "2e Jerichostraat", "huisnr" -> "16", "huisletter" -> "A", "huisnrtoev" -> "B")),
      AdresMatchTestData("Jodenbreestraat 25- DBI", Map("straat" -> "Jodenbreestraat", "huisnr" -> "25", "huisnrtoev" -> "- DBI")),
      AdresMatchTestData("H.W. Iordensweg 17afd. WRO", Map("straat" -> "H.W. Iordensweg", "huisnr" -> "17", "huisnrtoev" -> "afd. WRO")),
      AdresMatchTestData("Zuider Buiten Spaarne 22Koningstein", Map("straat" -> "Zuider Buiten Spaarne", "huisnr" -> "22", "huisnrtoev" -> "Koningstein")),
      AdresMatchTestData("'s-Gravenweg 766", Map("straat" -> "'s-Gravenweg", "huisnr" -> "766")),
      AdresMatchTestData("A/B Amstelbrouwerij XII", Map("straat" -> "A/B Amstelbrouwerij XII")))
    data
  }

}

case class AdresMatchTestData(test: String, expected: Map[String, String])