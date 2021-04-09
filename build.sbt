name := "scotty-songsheet-processing"
organization := "ch.rebstokk"

version := "8.0.0-SNAPSHOT"

scalaVersion := "2.13.5"

libraryDependencies ++= Seq(
	"org.apache.pdfbox" % "pdfbox" % "2.0.23",
	"org.apache.pdfbox" % "pdfbox-tools" % "2.0.23",
	"org.bouncycastle" % "bcprov-jdk15on" % "1.68",
  "org.bouncycastle" % "bcmail-jdk15on" % "1.68",
  "org.bouncycastle" % "bcpkix-jdk15on" % "1.68",
	"com.typesafe.slick" %% "slick" % "3.3.3",
	"mysql" % "mysql-connector-java" % "8.0.23",
	"com.typesafe.slick" %% "slick-hikaricp" % "3.3.3",
	"com.typesafe.play" %% "play-json" % "2.9.2",
	"net.java.truecommons" % "truecommons-io" % "2.5.0",
	"commons-io" % "commons-io" % "2.8.0",
	"org.scalatest" %% "scalatest" % "3.2.5" % Test,
	"org.scalamock" %% "scalamock" % "5.1.0" % Test,
	"com.github.pathikrit" %% "better-files" % "3.9.1",
	"com.typesafe.scala-logging" %% "scala-logging" % "3.9.3",
	"ch.qos.logback" % "logback-classic" % "1.2.3",
	"com.jcraft" % "jsch" % "0.1.55"

)

//Because of the database, which is set up for each test, integration tests run in sequence
parallelExecution in Test := false

publish / skip := true