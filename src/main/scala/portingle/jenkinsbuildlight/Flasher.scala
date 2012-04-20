package portingle.jenkinsbuildlight

import portingle.kmtronic.RelayInstance

trait Flasher {
  def add(relay: RelayInstance)

  def remove(relay: RelayInstance)
}
