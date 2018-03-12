import sbt.Keys._
import sbt._

/**
 * This is a simple sbt setup generating Slick code from the given
 * database before compiling the projects code.
 */
object dbReverseEngineerer extends Build {
  val slickVersion = "3.2.2"

  lazy val mainProject = Project(
    id="main",
    base=file("."),
    settings = Project.defaultSettings ++ Seq(
      scalaVersion := "2.12",
      libraryDependencies ++= List(
        "com.typesafe.slick" %% "slick" % slickVersion,
        "com.typesafe.slick" %% "slick-codegen" % slickVersion,
        "org.slf4j" % "slf4j-nop" % "1.7.12"
      ),
      slick <<= slickCodeGenTask // register manual sbt command
      // the following line is commented out because we want to run the generation manually by executing the task "gen-tables"
      //sourceGenerators in Compile <+= slickCodeGenTask // register automatic code generation on every compile, remove for only manual use
    )
  )

  // code generation task
  lazy val slick = TaskKey[Seq[File]]("gen-tables")
  lazy val slickCodeGenTask = (scalaSource in Compile, dependencyClasspath in Compile, runner in Compile, streams) map { (dir, cp, r, s) =>
    val outputDir = (dir).getPath
    val url = "jdbc:mysql://localhost:3306/dockerdefault?user=root&password=sumsang100"
    val jdbcDriver = "com.mysql.jdbc.Driver"
    val slickDriver = "slick.driver.MySQLDriver"
    val pkg = "ch.scotty.generatedschema"
    toError(r.run("slick.codegen.SourceCodeGenerator", cp.files, Array(slickDriver, jdbcDriver, url, outputDir, pkg), s.log))
    val fname = outputDir + "/ch/scotty/generatedschema/Tables.scala"
    Seq(file(fname))
  }
}