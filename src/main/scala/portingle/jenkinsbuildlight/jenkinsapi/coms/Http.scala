package portingle.jenkinsbuildlight.jenkinsapi.coms

import java.io.InputStream
import java.net.URL

private object Http {
  def request(urlString: String): (Boolean, InputStream) =
    try {
      val url = new URL(urlString)
      val body = url.openStream
      (true, body)
    }
    catch {
      case ex: Exception => (false, null)
    }
}
