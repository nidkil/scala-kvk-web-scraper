package nl.newparadigm.util

import scala.reflect.runtime.universe._
import scala.util.matching.Regex

object Util {

  def addTrailingSpace(str : String): String = {
    addTrailingSpace(Some(str))
  }
  
  def addTrailingSpace(str : Option[String]): String = {
    addTrailing(str, " ") 
  }

  def addTrailing(str : String, postfix : String): String = {
    addTrailing(Some(str), postfix)
  }
  
  def addTrailing(obj : Option[Any], postfix : String): String = {
    obj match {
      case Some(x) if x.toString().length() > 0 => x.toString().concat(postfix) 
      case Some(x) => ""
      case None => ""
    }
  }
  
  def template(prefix : String, obj : Option[Any], postfix : String): String = {
    obj match {
      case Some(x) if x.toString().length() > 0 => prefix.concat(x.toString()).concat(postfix) 
      case Some(x) => prefix.concat(postfix)
      case None => prefix.concat(postfix)
    }
  }

  def formatPostcode(postcode: String): String = {
    val pattern = """^([0-9]{4})([A-Z]{2})$""".r
    val matched = pattern.findFirstMatchIn(postcode)

    matched match {
      case Some(m) =>
        m.group(1) + " " + m.group(2)
      case None =>
        postcode
    }
  }

}