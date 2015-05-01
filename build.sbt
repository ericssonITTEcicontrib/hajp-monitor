organization := "com.ericsson.jenkinsci.hajp"

name := "hajp-monitor"

scalaVersion := "2.11.5"

lazy val `hajp-monitor` = (project in file(".")).enablePlugins(PlayScala)
  .settings(
    libraryDependencies ++= (Dependencies.frontend ++ Seq(filters, cache)),
    pipelineStages := Seq(digest, gzip)
  )

// default credentials needed to resolve dependencies for compilation
// in order to publish, you require credentials that have write access.
// These can be placed in ~/.sbt/0.13/plugins/credentials.sbt
credentials in ThisBuild += Credentials("Artifactory Realm", "arm.mo.ca.am.ericsson.se", "artread", "\\{DESede\\}YNtyA/TMlbuQjz/BlYj9Pw==")

resolvers in ThisBuild ++= Seq("Artifactory Staging ITTE" at "https://arm.mo.ca.am.ericsson.se/artifactory/proj-jnkserv-staging-local/")

publishTo := {
  val artifactoryURL = "https://arm.mo.ca.am.ericsson.se/artifactory/"
  if (isSnapshot.value)
    Some("snapshots" at artifactoryURL + "proj-jnkserv-dev-local")
  else
    Some("releases"  at artifactoryURL + "proj-jnkserv-staging-local")
}

releaseSettings

// This block ensures distribution zip is actually packed in publish
lazy val dist = com.typesafe.sbt.SbtNativePackager.NativePackagerKeys.dist

publish <<= (publish) dependsOn  dist

publishLocal <<= (publishLocal) dependsOn dist

val distHack = TaskKey[File]("dist-hack", "Hack to publish dist")

artifact in distHack ~= { (art: Artifact) => art.copy(`type` = "zip", extension = "zip") }

val distHackSettings = Seq[Setting[_]] (
  distHack <<= (target in Universal, normalizedName, version) map { (targetDir, id, version) =>
    val packageName = "%s-%s" format(id, version)
    targetDir / (packageName + ".zip")
  }) ++ Seq(addArtifact(artifact in distHack, distHack).settings: _*)

seq(distHackSettings: _*)

