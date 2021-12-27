package ch.scotty.converter.scotty

import ch.scotty.fixture.SongFixture
import ch.scotty.fixture.SongFixture.VerseWithRefrain
import ch.scotty.{DatabaseConnection, IntegrationSpec}
import org.scalatest.TryValues
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

class LyricsConverterIntTest extends IntegrationSpec with DatabaseConnection with TryValues {


  "loadAndConvertLyrics" should "convert a real song as expected" in {
    //arrange
    val createdLiedRow = SongFixture.SongFixtureWithVersesAndRefrain.generate()
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
         |Jerusalem, Jerusalem,
         |leg’ Dein Gewand der Trauer ab!
         |Jerusalem, Jerusalem,
         |singe und tanze Deinem Gott!
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
    val optContent = testee.loadAndConvertLyrics(createdLiedRow.id)
    //assert
    optContent.get shouldBe expectedContent
  }

  it should "convert different lyrics correctly" in {
    //arrange
    val defaultRefrain1 = "    Jerusalem, Jerusalem, <br/>\nleg’ Dein Gewand der Trauer ab!  "
    val defaultRefrain2 = "    ref2<br>  "
    val defaultVerse1 = " <br/><br/><br/>d "
    val defaultVerse2 = ""
    val defaultVerse3 = "foo"

    val versesWithRefrain: Seq[VerseWithRefrain] =
      Seq(
        VerseWithRefrain(Some(defaultVerse1), None),
        VerseWithRefrain(Some(defaultVerse2), Some(defaultRefrain1)),
        VerseWithRefrain(None, None),
        VerseWithRefrain(Some(defaultVerse3), Some(defaultRefrain2)),
        VerseWithRefrain(None, Some(defaultRefrain2))
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
        |ref2""".stripMargin
    }
    //act
    val optContent = testee.loadAndConvertLyrics(createdLiedRow.id)
    //assert
    optContent.get shouldBe expectedContent
  }
}