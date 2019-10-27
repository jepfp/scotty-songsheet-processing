name := "scotty-songsheet-processing"
organization := "ch.rebstokk"

version := "7.0.1-SNAPSHOT"

scalaVersion := "2.12.10"

libraryDependencies ++= Seq(
	"org.apache.pdfbox" % "pdfbox" % "2.0.17",
	"org.apache.pdfbox" % "pdfbox-tools" % "2.0.17",
	"org.bouncycastle" % "bcprov-jdk15on" % "1.63",
  "org.bouncycastle" % "bcmail-jdk15on" % "1.63",
  "org.bouncycastle" % "bcpkix-jdk15on" % "1.63",
	"com.typesafe.slick" %% "slick" % "3.3.2",
	"mysql" % "mysql-connector-java" % "8.0.17",
	"com.typesafe.play" %% "play-json" % "2.7.4",
	"net.java.truecommons" % "truecommons-io" % "2.5.0",
	"commons-io" % "commons-io" % "2.5",
	"com.jsuereth" % "scala-arm_2.12" % "2.0",
	"org.scalatest" %% "scalatest" % "3.0.8" % "test",
  "org.scalamock" % "scalamock-scalatest-support_2.12" % "3.6.0" % Test,
	"com.github.pathikrit" %% "better-files" % "3.8.0",
	"com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
	"ch.qos.logback" % "logback-classic" % "1.2.3"
)

//Because of the database, which is set up for each test, integration tests run in sequence
parallelExecution in Test := false

mainClass in assembly := Some("ch.scotty.Main")