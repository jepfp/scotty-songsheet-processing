name := "scotty-songsheet-processing"

version := "1.0"

scalaVersion := "2.11.8"

EclipseKeys.withSource := true

libraryDependencies ++= Seq(
	"org.apache.pdfbox" % "pdfbox" % "2.0.0",
	"org.apache.pdfbox" % "pdfbox-tools" % "2.0.0",
	"com.typesafe.slick" %% "slick" % "3.1.1",
	"mysql" % "mysql-connector-java" % "5.1.24",
	"com.typesafe.play" %% "play-json" % "2.5.2",
	"net.java.truecommons" % "truecommons-io" % "2.5.0",
	"commons-io" % "commons-io" % "2.5",
	"com.jsuereth" %% "scala-arm" % "1.4",
	"org.scalatest" %% "scalatest" % "3.0.1" % "test",
  "org.scalamock" %% "scalamock-scalatest-support" % "3.4.2" % "test"
)

//Because of the database, which is set up for each test, integration tests run in sequence
parallelExecution in Test := false

mainClass in assembly := Some("ch.scotty.Main")