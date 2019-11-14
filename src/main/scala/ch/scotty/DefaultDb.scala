package ch.scotty


import com.typesafe.config.{ConfigFactory, ConfigRenderOptions}
import slick.jdbc.MySQLProfile.api._

object DefaultDb{
  //def scotty = DefaultDb("exampleScotty")
  def scotty = new DefaultDb("dockerdefault")
  def songanize = new DefaultDb("songanize-prod")
}

class DefaultDb(databaseConnectionConfigKey : String) extends Db {
  //val databaseConnectionConfigKey = "exampleScotty"

  //Enable to see information about what the db configuration is and where it comes from.
  printOutConfigInformation

  val db = Database.forConfig(databaseConnectionConfigKey)

  private def printOutConfigInformation = {
    val config = ConfigFactory.load()
    val options = ConfigRenderOptions.defaults().setFormatted(true)
    println("The following database configuration is used: \n" + config.getConfig(databaseConnectionConfigKey).root().render(options))
  }
}