package ch.scotty.converter

import java.nio.file.{Files, Paths}

import com.typesafe.config.{Config, ConfigFactory}

private class ExportPathResolverAndCreator(conf : Config) {

  val config = conf.getConfig("converter")

  def this(){
    this(ConfigFactory.load())
  }

  def resolve(filename: String) : String = {
    val basePath = config.getString("exportBaseDir")
    Files.createDirectories(Paths.get(basePath))
    val path = Paths.get(basePath, filename).toAbsolutePath
    val pathString = path.toString
    println(s"Output path: $pathString")
    pathString
  }

}