package ch.scotty.converter.songanize.load

import java.sql.Timestamp

import ch.scotty.IntegrationSpec
import ch.scotty.converter.songanize.{SonganizeDatabaseConnection, SonganizeSong}
import org.scalatest.EitherValues
import org.scalatest.Matchers._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class SongLoaderIntTest extends IntegrationSpec with SonganizeDatabaseConnection with EitherValues {


  "SongLoader" should "return songanize songs" in {
    //arrange
    val testee = new SongLoader()
    //act
    val result = Await.result(testee.load(Some(Set(961, 804))), Duration.Inf)
    //assert
    result shouldBe Vector(
      SonganizeSong(804, Timestamp.valueOf("1970-01-01 00:00:00.0"), Timestamp.valueOf("2019-09-19 20:29:51.0"), 25, "Cornerstone - Hillsong", "Cornerstone", "C", "2019/16/25", "15558586371162490379.pdf", "pdf"),
      SonganizeSong(961, Timestamp.valueOf("1970-01-01 00:00:00.0"), Timestamp.valueOf("2019-08-05 07:11:26.0"), 70, "Good Good Father", "", "dds", "2019/32/70", "1564981844473606191.pdf", "pdf")
    )
  }

  it should "return all songanize songs" in {
    //arrange
    val testee = new SongLoader()
    //act
    val result = Await.result(testee.load(None), Duration.Inf)
    //assert
    result should have size(256)
  }
}
