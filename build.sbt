organization := "com.ericsson.jenkinsci.hajp"

name := "hajp-monitor"

scalaVersion := "2.11.5"

lazy val `hajp-monitor` = (project in file(".")).enablePlugins(PlayScala)
  .settings(
    libraryDependencies ++= (Dependencies.frontend ++ Seq(filters, cache)),
    pipelineStages := Seq(digest, gzip)
  )

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

pomExtra := (
  <url>https://github.com/ericssonITTEcicontrib/hajp-monitor</url>
  <licenses>
      <license>
        <name>MIT License</name>
        <url>http://www.opensource.org/licenses/mit-license.php</url>
      </license>
  </licenses>
   <developers>
      <developer>
        <name>Daniel Yinanc</name>
        <organization>Ericsson</organization>
        <organizationUrl>http://www.ericsson.com</organizationUrl>
      </developer>
      <developer>
        <name>Scott Hebert</name>
        <organization>Ericsson</organization>
        <organizationUrl>http://www.ericsson.com</organizationUrl>
      </developer>
    </developers>
    <scm>
      <connection>scm:git:git@github.com:ericssonITTEcicontrib/hajp-monitor.git</connection>
      <developerConnection>scm:git:git@github.com:ericssonITTEcicontrib/hajp-monitor.git</developerConnection>
      <url>git@github.com:ericssonITTEcicontrib/hajp-monitor.git</url>
    </scm>)

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

releasePublishArtifactsAction := PgpKeys.publishSigned.value

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

