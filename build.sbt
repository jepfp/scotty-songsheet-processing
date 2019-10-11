name := "scotty-songsheet-processing"

version := "6.0-SNAPSHOT"

scalaVersion := "2.13.1"

libraryDependencies ++= Seq(
	"org.apache.pdfbox" % "pdfbox" % "2.0.17",
	"org.apache.pdfbox" % "pdfbox-tools" % "2.0.17",
	"org.bouncycastle" % "bcprov-jdk15on" % "1.64",
	"org.bouncycastle" % "bcmail-jdk15on" % "1.64",
	"org.bouncycastle" % "bcpkix-jdk15on" % "1.64",
	"com.typesafe.slick" %% "slick" % "3.3.2",
	"mysql" % "mysql-connector-java" % "8.0.17",
	"com.typesafe.play" %% "play-json" % "2.7.4",
	"net.java.truecommons" % "truecommons-iro" % "2.5.0",
	"commons-io" % "commons-io" % "2.5",
	"com.jsuereth" %% "scala-arm" % "2.0",
	"org.scalatest" %% "scalatest" % "3.0.8" % "test",
  "org.scalamock" %% "scalamock-scalatest-support" % "3.6.0" % "test",
	"com.github.pathikrit" %% "better-files" % "3.8.0"
)

//Because of the database, which is set up for each test, integration tests run in sequence
parallelExecution in Test := false

mainClass in assembly := Some("ch.scotty.Main")