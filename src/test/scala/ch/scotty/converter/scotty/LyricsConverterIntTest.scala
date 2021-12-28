package ch.scotty.converter.scotty

import ch.scotty.fixture.SongFixture
import ch.scotty.fixture.SongFixture.VerseWithRefrain
import ch.scotty.{DatabaseConnection, IntegrationSpec}
import org.scalatest.TryValues
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

class LyricsConverterIntTest extends IntegrationSpec with DatabaseConnection with TryValues {


  "loadAndConvertLyrics" should "convert a real song as expected" in {
    //arrange
    val testee = new LyricsConverter()

    val expectedContent = {
      """||: Dunkelheit bedeckt alle Völker der Welt,
         |auf Jerusalem werde Licht! :|
         |
         |Blick empor, schaue aus,
         |beben soll Dein Herz und sich öffnen weit.
         |Auf den Armen trägt man die Töchter herbei,
         |Deine Söhne kommen von fern.
         |
         |Jerusalem, Jerusalem,
         |leg’ Dein Gewand der Trauer ab!
         |Jerusalem, Jerusalem,
         |singe und tanze Deinem Gott!
         |
         ||: Völker wandern hin, bringen Weihrauch und Gold,
         |Herden von Kamelen sind Dein. :|
         |
         |Von weit her kommen sie,
         |prachtvoll ist Dein Glanz Deiner Herrlichkeit.
         |Weihrauch steigt empor, weithin schallt Gottes Lob.
         |Ruhmreich sind die Taten des Herrn.
         |
         ||: Jubelt in der Stadt, alle, die ihr sie liebt,
         |fröhlich sollt ihr sein und euch freun! :|
         |
         |Stadt des Herrn nennt man Dich,
         |ewig leuchtet Dir Gott der Herr als Licht.
         |Zion singe laut, denn Dein König bringt Dir
         |Freudenöl statt Trauergewand.""".stripMargin
    }
    //act
    val optContent = Await.result(testee.loadAndConvertLyrics(10), Duration.Inf)
    //assert
    optContent.get shouldBe expectedContent
  }

  it should "convert different lyrics correctly" in {
    //arrange
    val defaultRefrain1 = "    Jerusalem, Jerusalem, <br/>\nleg’ Dein Gewand der Trauer ab!  "
    val defaultRefrain2 = "    ref2<br>  "
    val defaultRefrain3 = "    <br> <br/>  <br>ref3<br>  "
    val defaultVerse1 = " <br/><br/><br/>d "
    val defaultVerse2 = ""
    val defaultVerse3 = "foo"

    val versesWithRefrain: Seq[VerseWithRefrain] =
      Seq(
        VerseWithRefrain(Some(defaultVerse1), None),
        VerseWithRefrain(Some(defaultVerse2), Some(defaultRefrain1)),
        VerseWithRefrain(None, None),
        VerseWithRefrain(Some(defaultVerse3), Some(defaultRefrain2)),
        VerseWithRefrain(None, Some(defaultRefrain3))
      )

    val createdLiedRow = SongFixture.SongFixtureWithVersesAndRefrain.generate(versesWithRefrain)
    val testee = new LyricsConverter()

    val expectedContent = {
      """d
        |
        |Jerusalem, Jerusalem,
        |leg’ Dein Gewand der Trauer ab!
        |
        |ref2
        |
        |foo
        |
        |ref3""".stripMargin
    }
    //act
    val optContent = Await.result(testee.loadAndConvertLyrics(createdLiedRow.id), Duration.Inf)
    //assert
    optContent.get shouldBe expectedContent
  }

  it should "Lied with only one refrain" in {
    //arrange
    val testee = new LyricsConverter()

    val expectedContent = {
      """|first line
        |second line
        |third line""".stripMargin
    }
    //act
    val optContent = Await.result(testee.loadAndConvertLyrics(11), Duration.Inf)
    //assert
    optContent.get shouldBe expectedContent
  }

  it should "Lied with one refrain assigned and one refrain not assigned" in {
    //arrange
    val testee = new LyricsConverter()

    val expectedContent = {
      """|refrain 1
        |
        |verse 1
        |
        |verse 2
        |
        |refrain 2""".stripMargin
    }
    //act
    val optContent = Await.result(testee.loadAndConvertLyrics(12), Duration.Inf)
    //assert
    optContent.get shouldBe expectedContent
  }
}