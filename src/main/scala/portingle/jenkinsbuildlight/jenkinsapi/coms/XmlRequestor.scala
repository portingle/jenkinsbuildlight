package portingle.jenkinsbuildlight.jenkinsapi.coms

import xml.{XML, Elem}

object XmlRequestor {

  def fetchAndParseURL(URL: String): Elem = {
    val body = Http request (URL)
    val xml = XML.load(body)
    body.close()
    xml
  }
}
