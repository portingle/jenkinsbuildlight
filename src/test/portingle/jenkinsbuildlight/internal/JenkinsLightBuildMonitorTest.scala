package portingle.jenkinsbuildlight.internal

import portingle.jenkinsbuildlight.jenkinsapi.BuildStatus._
import portingle.jenkinsbuildlight.jenkinsapi.{Job, BuildStatus}
import org.junit.{Assert, Test}

class JenkinsLightBuildMonitorTest {

  // Green / Red
  val testData: Seq[(Seq[BuildStatus.Value], (String, String))] = Seq(
    (Seq(Bad), ("off", "on")),
    (Seq(Bad, Good), ("off", "on")),
    (Seq(Bad, UnknownBuildStatus), ("off", "on")),
    (Seq(Bad, Disabled), ("off", "on")),
    (Seq(Bad, RebuildingGoodBuild), ("flash", "on")),
    (Seq(Bad, RebuildingBadBuild), ("off", "flash")),

    (Seq(Good), ("on", "off")),
    (Seq(Good, Bad), ("off", "on")),
    (Seq(Good, Disabled), ("on", "off")),
    (Seq(Good, UnknownBuildStatus), ("on", "off")),
    (Seq(Good, RebuildingGoodBuild), ("flash", "off")),
    (Seq(Good, RebuildingBadBuild), ("off", "flash")),

    (Seq(), ("off", "off")),
    (Seq(UnknownBuildStatus), ("off", "off")),
    (Seq(Disabled), ("off", "off")),
    (Seq(RebuildingBadBuild), ("off", "flash")),
    (Seq(RebuildingGoodBuild), ("flash", "off")),
    (Seq(RebuildingBadBuild, RebuildingGoodBuild), ("flash", "flash")),
    (Seq(), ("off", "off"))

  )

  def runTest(testCase: (Seq[Value], (String, String))): Option[String] = {
    val inputs = testCase._1
    val expectation = testCase._2

    val jobs = inputs.map {
      new Job("", _, "")
    }

    val testInstance = newTestInstance

    testInstance.updateMonitor(jobs)
    val actual = testInstance.value

    if (actual == expectation) {
      //println("PASS : %s ".format(inputs))
      None
    } else {
      val error = "expected %s but got %s for input (%s)".format(expectation, actual, inputs.mkString(","))
      println("FAIL : " + error)
      Some(error)
    }
  }

  @Test
  def test1 {
    val failures = testData.map {
      runTest(_)
    }.flatten
    Assert.assertTrue("Failed : " + failures, failures.length == 0)
  }

  def newTestInstance = new JenkinsLightBuildMonitor {
    var redValue: String = _
    var greenValue: String = _

    def value = {
      (greenValue, redValue)
    }

    protected def setValue(i: IndicatorColour.Value, v: String) {
      i match {
        case IndicatorColour.GreenLight => greenValue = v
        case IndicatorColour.RedLight => redValue = v
      }
    }

    protected def lightOn(i: IndicatorColour.Value) {
      setValue(i, "on")
    }

    protected def lightOff(i: IndicatorColour.Value) {
      setValue(i, "off")
    }

    protected def lightFlash(i: IndicatorColour.Value) {
      setValue(i, "flash")
    }
  }

}
