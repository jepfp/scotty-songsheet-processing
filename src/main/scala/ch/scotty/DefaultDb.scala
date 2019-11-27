package ch.scotty


import com.typesafe.config.{ConfigFactory, ConfigRenderOptions}
import com.typesafe.scalalogging.Logger
import slick.jdbc.MySQLProfile.api._

object DefaultDb{
  //def scotty = DefaultDb("exampleScotty")
  def scotty = new DefaultDb("dockerdefault")
  def songanize = new DefaultDb("songanize-prod")
}

class DefaultDb(databaseConnectionConfigKey : String) extends Db {
  //val databaseConnectionConfigKey = "exampleScotty"
  val logger = Logger(classOf[DefaultDb])

  //Enable to see information about what the db configuration is and where it comes from.
  printOutConfigInformation

  val db = Database.forConfig(databaseConnectionConfigKey)

  private def printOutConfigInformation = {
    val config = ConfigFactory.load()
    val options = ConfigRenderOptions.defaults().setFormatted(true)
    logger.info("The following database configuration is used: \n" + config.getConfig(databaseConnectionConfigKey).root().render(options))
  }
}