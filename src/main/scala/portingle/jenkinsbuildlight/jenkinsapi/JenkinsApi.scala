package portingle.jenkinsbuildlight.jenkinsapi

import BuildStatus._
import coms.XmlRequestor

object JenkinsApi {

  def buildStatus(api: String): Seq[Job] = {
    try {
      val xml = XmlRequestor.fetchAndParseURL(api)

      (xml \ "job").map(job => {
        val name = (job \ "name").text
        val color = (job \ "color").text

        color match {
          case "red" => Job(name, Bad, color)
          case "red_anime" => Job(name, RebuildingBadBuild, color)
          case "blue" => Job(name, Good, color)
          case "blue_anime" => Job(name, RebuildingGoodBuild, color)
          case "disabled" => Job(name, Disabled, color)
          case _ => Job(name, UnknownBuildStatus, color)
        }
      }
      )
    }
    catch {
      case e => println("failed fetch from Jenkins " + api + " due to " + e)
      Nil
    }
  }
}
