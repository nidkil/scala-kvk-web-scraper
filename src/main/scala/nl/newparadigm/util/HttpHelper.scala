package nl.newparadigm.util

import java.net.{URLDecoder, URLEncoder}

trait HttpHelper {
  
  /**
   * URL decode the string.
   */
  def urlDecode(in: String) = URLDecoder.decode(in, "UTF-8")
  
  /**
   * URL encode the string.
   */
  def urlEncode(in: String) = URLEncoder.encode(in, "UTF-8")
}