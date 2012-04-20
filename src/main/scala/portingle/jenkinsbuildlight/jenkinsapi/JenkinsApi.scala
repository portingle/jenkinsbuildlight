package portingle.jenkinsbuildlight.jenkinsapi

import BuildStatus._
import coms.XmlRequestor

object JenkinsApi {

  def buildStatus(api: String): Seq[Job] = {
    val xml = XmlRequestor.fetchAndParseURL(api)

    (xml \ "job").map(job => {
      val name = (job \ "name").text
      val color = (job \ "color").text
      color match {
        case "red" => Job(name, Bad)
        case "red_anime" => Job(name, RebuildingBadBuild)
        case "blue" => Job(name, Good)
        case "blue_anime" => Job(name, RebuildingGoodBuild)
        case "disabled" => Job(name, Disabled)
        case _ => throw new RuntimeException(name + " has unknown status '" + color + "'")
      }
    }
    )
  }
}
