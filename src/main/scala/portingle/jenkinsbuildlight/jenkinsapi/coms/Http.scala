package portingle.jenkinsbuildlight.jenkinsapi.coms

import java.io.InputStream
import java.net.URL

private object Http {
  def request(urlString: String): InputStream = {
    val url = new URL(urlString)
    url.openStream
  }

}
