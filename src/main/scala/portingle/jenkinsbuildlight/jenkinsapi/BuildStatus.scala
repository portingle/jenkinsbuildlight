package portingle.jenkinsbuildlight.jenkinsapi

object BuildStatus extends Enumeration {
  type BuildStatus = Value

  val RebuildingBadBuild = Value(1)
  val Bad = Value(2)

  val RebuildingGoodBuild = Value(3)
  val Good = Value(4)

  val Disabled = Value(5)
  val UnknownBuildStatus = Value(6)

  class PimpedBuildStatus(v: BuildStatus)  {
    def isGood = (v == Good || v == RebuildingGoodBuild)
    def isBad = (v == Bad || v == RebuildingBadBuild)
    def isOther = !(isGood || isBad)
  }

  implicit def pimp(s: BuildStatus): PimpedBuildStatus = new PimpedBuildStatus(s)
}
