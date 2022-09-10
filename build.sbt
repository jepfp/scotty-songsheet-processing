name := "scotty-songsheet-processing"
organization := "ch.rebstokk"

scalaVersion := "2.13.7"

credentials += Credentials(Path.userHome / ".sbt" / "space-songhsip-internal.credentials")

resolvers += "space-songhsip-internal" at "https://maven.pkg.jetbrains.space/songship/p/songship/songhsip-internal"
publishTo := Some("space-songhsip-internal" at "https://maven.pkg.jetbrains.space/songship/p/songship/songhsip-internal")

libraryDependencies ++= Seq(
	"org.apache.pdfbox" % "pdfbox" % "2.0.25",
	"org.apache.pdfbox" % "pdfbox-tools" % "2.0.25",
	"org.bouncycastle" % "bcprov-jdk15on" % "1.70",
  "org.bouncycastle" % "bcmail-jdk15on" % "1.70",
  "org.bouncycastle" % "bcpkix-jdk15on" % "1.70",
	"com.sksamuel.scrimage" % "scrimage-core" % "4.0.24",
	"com.sksamuel.scrimage" % "scrimage-filters" % "4.0.24",
	"com.typesafe.slick" %% "slick" % "3.3.3",
	"mysql" % "mysql-connector-java" % "8.0.27",
	"com.typesafe.slick" %% "slick-hikaricp" % "3.3.3",
	"com.typesafe.play" %% "play-json" % "2.9.2",
	"net.java.truecommons" % "truecommons-io" % "2.5.0",
	"commons-io" % "commons-io" % "2.11.0",
	"org.scalatest" %% "scalatest" % "3.2.10" % Test,
	"org.scalamock" %% "scalamock" % "5.1.0" % Test,
	"com.github.pathikrit" %% "better-files" % "3.9.1",
	"com.typesafe.scala-logging" %% "scala-logging" % "3.9.4",
	"ch.qos.logback" % "logback-classic" % "1.2.9",
	"com.jcraft" % "jsch" % "0.1.55"

)

//Because of the database, which is set up for each test, integration tests run in sequence
parallelExecution in Test := false

import sbtrelease.ReleasePlugin.autoImport.ReleaseTransformations._
releaseProcess := Seq[ReleaseStep](
	checkSnapshotDependencies,
	inquireVersions,
	runClean,
	runTest,
	setReleaseVersion,
	commitReleaseVersion,
	tagRelease,
	releaseStepTask(publishLocal),
	setNextVersion,
	commitNextVersion,
	pushChanges
)