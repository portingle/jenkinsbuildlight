package portingle.jenkinsbuildlight

import internal.{IndicatorColour, MonitorThread, JenkinsLightBuildMonitor}


object LoggingMonitor extends JenkinsLightBuildMonitor with App {
  //val api = "http://ci.jruby.org/api/xml"
  start("http://localhost:8080/api/xml")
  
  def start(api: String) {
    val thread = new MonitorThread(this.updateMonitor)
    thread.start(api)
  }

  protected def lightOn(i: IndicatorColour.Value) {
    println("" + i + ":ON")
  }

  protected def lightFlash(i: IndicatorColour.Value) {
    println("" + i + ":FLASH")
  }

  protected def lightOff(i: IndicatorColour.Value) {
    println("" + i + ":OFF")
  }
}
