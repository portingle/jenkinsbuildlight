package portingle.jenkinsbuildlight.jenkinsapi

import BuildStatus._

case class Job(job: String, state: BuildStatus, code: String)
