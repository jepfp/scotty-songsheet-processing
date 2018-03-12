name := "scotty-songsheet-processing"

version := "2.0-SNAPSHOT"

scalaVersion := "2.12.2"

EclipseKeys.withSource := true

libraryDependencies ++= Seq(
	"org.apache.pdfbox" % "pdfbox" % "2.0.6",
	"org.apache.pdfbox" % "pdfbox-tools" % "2.0.6",
	"org.bouncycastle" % "bcprov-jdk15on" % "1.57",
	"org.bouncycastle" % "bcmail-jdk15on" % "1.57",
	"org.bouncycastle" % "bcpkix-jdk15on" % "1.57",
	"com.typesafe.slick" %% "slick" % "3.2.2",
	"mysql" % "mysql-connector-java" % "5.1.24",
	"com.typesafe.play" % "play-json_2.12" % "2.6.9",
	"net.java.truecommons" % "truecommons-io" % "2.5.0",
	"commons-io" % "commons-io" % "2.5",
	"com.jsuereth" %% "scala-arm" % "2.0",
	"org.scalatest" %% "scalatest" % "3.0.1" % "test",
  "org.scalamock" %% "scalamock-scalatest-support" % "3.4.2" % "test",
	"com.github.pathikrit" % "better-files_2.11" % "2.17.1" % "test"
)

//Because of the database, which is set up for each test, integration tests run in sequence
parallelExecution in Test := false

mainClass in assembly := Some("ch.scotty.Main")