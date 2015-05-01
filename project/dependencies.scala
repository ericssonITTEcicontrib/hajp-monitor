import sbt._

object Dependencies {

  object Version {
    val akka = "2.3.9"
  }

  lazy val frontend = common ++ webjars ++ hajpSpecific

  val common = Seq(
    "com.typesafe.akka" %% "akka-actor" % Version.akka,
    "com.typesafe.akka" %% "akka-cluster" % Version.akka,
    "com.google.guava" % "guava" % "17.0"
  )

  val webjars = Seq(
    "org.webjars" %% "webjars-play" % "2.3.0",
    "org.webjars" % "requirejs" % "2.1.11-1",
    "org.webjars" % "underscorejs" % "1.6.0-3",
    "org.webjars" % "jquery" % "1.11.1",
    "org.webjars" % "d3js" % "3.5.3",
    "org.webjars" % "bootstrap" % "3.3.4" exclude("org.webjars", "jquery"),
    "org.webjars" % "bootswatch-yeti" % "3.3.1+2" exclude("org.webjars", "jquery"),
    "org.webjars" % "angularjs" % "1.3.14" exclude("org.webjars", "jquery"),
    "org.webjars" % "angular-ui-bootstrap" % "0.12.1" exclude("org.webjars", "angularjs"),
    "org.webjars" % "angular-chart.js" % "0.5.2" exclude("org.webjars", "angularjs")
  )

  val hajpSpecific = Seq(
    "com.ericsson.jenkinsci.hajp" % "hajp-common" % "1.0.7" % "provided")

  val metrics = Seq(
    "org.fusesource" % "sigar" % "1.6.4"
  )

}
