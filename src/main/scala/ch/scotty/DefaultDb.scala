package ch.scotty


import com.typesafe.config.{ConfigFactory, ConfigRenderOptions}
import slick.driver.MySQLDriver.api._


class DefaultDb extends Db {
  //val databaseConnectionConfigKey = "exampleScotty"
  val databaseConnectionConfigKey = "dockerdefault"

  //Enable to see information about what the db configuration is and where it comes from.
  printOutConfigInformation

  val db = Database.forConfig(databaseConnectionConfigKey)

  private def printOutConfigInformation = {
    val config = ConfigFactory.load()
    val options = ConfigRenderOptions.defaults().setFormatted(true)
    println("The following database configuration is used: \n" + config.getConfig(databaseConnectionConfigKey).root().render(options))
  }
}