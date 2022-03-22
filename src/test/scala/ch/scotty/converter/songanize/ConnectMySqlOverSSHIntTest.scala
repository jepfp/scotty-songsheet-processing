package ch.scotty.converter.songanize

import ch.scotty.IntegrationSpec
import com.jcraft.jsch.{ChannelSftp, JSch}
import org.scalatest.EitherValues
import slick.jdbc.MySQLProfile.api._

import java.util.Properties
import scala.concurrent.Await
import scala.concurrent.duration.Duration

class ConnectMySqlOverSSHIntTest extends IntegrationSpec with EitherValues {


  ignore should "return songanize songs" in {
    connectSsh()
  }

  private def connectSsh() = {

    var lport = 3306
    var rport = 3306

    try {
      val jsch = new JSch()
      val host = "SET IP"
      val user = "SET USER"
      val session = jsch.getSession(user, host, 22)
      session.setPassword("SET CORRECT PASSWORD")
      val config = new Properties
      config.put("StrictHostKeyChecking", "no")
      session.setConfig(config)
      session.connect
      val assinged_port = session.setPortForwardingL(lport, "127.0.0.1", rport)
      System.out.println("localhost:" + assinged_port + " -> " + host + ":" + rport)


      val db = Database.forConfig("songanize-prod")
      val query = db.run(sql"""select user_login from wp_users where id = 70""".as[String])
      val result = Await.result(query, Duration.Inf)



      println("jep: " + result)

      val channelSftp = session.openChannel("sftp").asInstanceOf[ChannelSftp]
      channelSftp.connect()
      channelSftp.get("/private/files/2019/32/70/1564981844473606191.pdf", "output.pdf")

      channelSftp.disconnect()
      session.disconnect()
    } catch {
      case e: Exception =>
        System.out.println(e)
    }

  }
}
