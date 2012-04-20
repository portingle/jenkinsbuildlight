package portingle.jenkinsbuildlight

import internal.{IndicatorColour, JenkinsLightBuildMonitor, MonitorThread}
import portingle.kmtronic.{RelayInstance, WindowsComPort, Relay}


object RelayAttachedMonitor extends JenkinsLightBuildMonitor with App {
  private val relay = new Relay(new WindowsComPort(5), 2)

  start("http://localhost:8080/api/xml")

  private val flasher = new Flasher {
    def add(relay: RelayInstance) = null
    def remove(relay: RelayInstance) = null
  }

  def start(api: String) {
    relay.open()
    val thread = new MonitorThread(this.jobProcessor)
    thread.start(api)
  }

  protected def lightOn(i: IndicatorColour.Value) {
    println("" + i + ":ON")
    val inst = relay(i)

    flasher.remove(inst)
    inst.powerOn()
  }

  protected def lightFlash(i: IndicatorColour.Value) {
    println("" + i + ":FLASH")
    val inst = relay(i)
    flasher.add(inst)
  }

  protected def lightOff(i: IndicatorColour.Value) {
    println("" + i + ":OFF")
    val inst = relay(i)
    flasher.remove(inst)
    inst.powerOff()
  }

  private implicit def relayNumber(i: IndicatorColour.Value): Int = i match {
    case IndicatorColour.GreenLight => 1
    case IndicatorColour.RedLight => 2
  }
}
