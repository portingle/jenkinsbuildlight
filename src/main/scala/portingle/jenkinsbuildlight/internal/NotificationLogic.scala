package portingle.jenkinsbuildlight.internal

import portingle.jenkinsbuildlight.internal.IndicatorColour._
import portingle.jenkinsbuildlight.jenkinsapi.Job
import portingle.jenkinsbuildlight.jenkinsapi.BuildStatus._

trait NotificationLogic {

  protected def lightOn(i: IndicatorColour.Value)

  protected def lightFlash(i: IndicatorColour.Value)

  protected def lightOff(i: IndicatorColour.Value)

  def updateMonitor(jobs: Seq[Job]) {
    println(jobs)
    val (greenState, redState) = calculateStates(jobs.map(_.state))

    setGreenLight(greenState)
    setRedLight(redState)
  }


  protected def calculateStates(results: Seq[BuildStatus]): (Option[BuildStatus], Option[BuildStatus]) = {
    val highestBadPriority = results.filter(_.isBad).sorted.headOption
    val highestGoodPriority = results.filter(_.isGood).sorted.headOption

    val good = (highestGoodPriority, highestBadPriority) match {
      case (n, None) => n
      case (n, Some(Disabled)) => n
      case (n, Some(UnknownBuildStatus)) => n
      case (Some(RebuildingGoodBuild), _) => Some(RebuildingGoodBuild)
      case (_, Some(_)) => None
    }

    (good, highestBadPriority)
  }

  private def setGreenLight(job: Option[BuildStatus]) {
    job match {
      case Some(Good) => lightOn(GreenLight)
      case Some(RebuildingGoodBuild) => lightFlash(GreenLight)
      case _ => lightOff(GreenLight)
    }
  }

  private def setRedLight(job: Option[BuildStatus]) {
    job match {
      case Some(Bad) => lightOn(RedLight)
      case Some(RebuildingBadBuild) => lightFlash(RedLight)
      case _ => lightOff(RedLight)
    }
  }
}
