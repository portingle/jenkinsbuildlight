package portingle.jenkinsbuildlight.jenkinsapi

object BuildStatus extends Enumeration {
  type BuildStatus = Value

  val Bad = Value(-2)
  val RebuildingBadBuild = Value(-1)
  val Good = Value(1)
  val RebuildingGoodBuild = Value(2)
  val Disabled = Value(0)

  class PimpedValue(v: BuildStatus) {
    def isGood = v.id > 0

    def isBad = !isGood
  }

  implicit def pimp(s: BuildStatus): PimpedValue = new PimpedValue(s)
}
