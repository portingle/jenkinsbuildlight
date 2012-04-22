package portingle.jenkinsbuildlight

import internal.{IndicatorColour, NotificationLogic, MonitorThread}
import jenkinsapi.Job
import portingle.kmtronic.{RelayInstance, WindowsComPort, TwoPortKMtronicRelay}


object RelayAttachedMonitor extends NotificationLogic with App {
  private val relay = new TwoPortKMtronicRelay(new WindowsComPort(5))

  start("http://localhost:8080/api/xml")

  def start(api: String) {
    relay.open()
    val thread = new MonitorThread(this.updateMonitor)
    thread.start(api)
  }

  override def updateMonitor(jobs: Seq[Job]) {

    try {
      super.updateMonitor(jobs)
    } catch {
      case ex => {
        println("failed updating monitors : " + ex)
        println("attempting to reopen the relay")
        try {
          relay.close()
          relay.open()
        } catch {
          case ex =>
        }
      }
    }
  }


  private val flasher = new Flasher {
    var state = true
    val OnIntervalMs = 1000
    val OffIntervalMs = 500

    @volatile var relays = List[RelayInstance]()

    val thread = new Thread() {
      override def run() {
        while (true) {
          try {
            state = !state

            relays.foreach {
              relay => if (state) {
                relay.powerOn()
              } else {
                relay.powerOff()
              }
            }

            if (state) {
              Thread.sleep(OnIntervalMs)
            } else {
              Thread.sleep(OffIntervalMs)
            }
          }
          catch {
            case ex => {
              println("had error while flashing : " + ex)
            }
          }
        }
      }
    }
    thread.setDaemon(true)
    thread.start()

    def add(relay: RelayInstance) {
      relays = relay :: relays.filterNot(_ == relay)
    }

    def remove(relay: RelayInstance) {
      relays = relays.filterNot(_ == relay)
    }
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
