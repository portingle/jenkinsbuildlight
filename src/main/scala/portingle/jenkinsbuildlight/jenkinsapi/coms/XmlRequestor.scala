package portingle.jenkinsbuildlight.jenkinsapi.coms

import xml.{XML, Elem}

object XmlRequestor {

  def fetchAndParseURL(URL: String): Elem = {
    val body = Http request (URL)
    try {
      XML.load(body)
    } finally {
      body.close()
    }
  }
}
