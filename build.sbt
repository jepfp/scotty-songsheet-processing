name := "scotty-songsheet-processing"

version := "1.0"

scalaVersion := "2.11.6"

EclipseKeys.withSource := true

libraryDependencies ++= Seq(
	"org.apache.pdfbox" % "pdfbox" % "2.0.0",
	"org.apache.pdfbox" % "pdfbox-tools" % "2.0.0",
	"com.typesafe.slick" %% "slick" % "3.1.1",
	"mysql" % "mysql-connector-java" % "5.1.24",
	"com.typesafe.play" %% "play-json" % "2.5.2",
	"net.java.truecommons" % "truecommons-io" % "2.5.0",
	"org.scalatest" % "scalatest_2.11" % "2.2.6" % "test"
)