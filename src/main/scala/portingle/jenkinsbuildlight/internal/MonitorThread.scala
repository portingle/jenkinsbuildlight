package portingle.jenkinsbuildlight.internal

import portingle.jenkinsbuildlight.jenkinsapi.{JenkinsApi, Job}


class MonitorThread(callback: (Seq[Job]) => Unit) {

  final def start(api: String) {
    val t = new Thread {

      override def run() {
        while (true) {
          val results = JenkinsApi.buildStatus(api)
          callback(results)
          Thread.sleep(1000)
        }
      }
    }
    t.start()
  }
}







