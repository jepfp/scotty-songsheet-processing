package ch.scotty.fixture

import ch.scotty
import ch.scotty.fixture.SongBuilder.songBuilder
import ch.scotty.generatedschema.Tables
import ch.scotty.{Db, SongsheetTestUtils, generatedschema}
import slick.dbio.DBIOAction
import slick.dbio.Effect.Write
import slick.jdbc.MySQLProfile.api._

import java.net.URL
import scala.annotation.tailrec
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

object SongFixture {

  def songFixtureBuilder(song: Tables.LiedRow) = new SongFixtureBuilder(song, None, Seq.empty)

  case class InsertableFile(inputFileUrl: URL) {
    def generateTableRows(): (Tables.FilemetadataRow, Tables.FileRow) = {
      val fm = Tables.FilemetadataRow(3, "sourcepdf", 0)
      val data = SongsheetTestUtils.readFileToBlob(inputFileUrl)
      val f = Tables.FileRow(0, 0, data, "fooFile.pdf", data.length().toString, "pdf")
      (fm, f)
    }

    def generateMetadataRow(): Tables.FilemetadataRow = {
      Tables.FilemetadataRow(3, "sourcepdf", 0)
    }

    def generateFileRow(): Tables.FileRow = {
      val data = SongsheetTestUtils.readFileToBlob(inputFileUrl)
      Tables.FileRow(0, 0, data, "fooFile.pdf", data.length().toString, "pdf")
    }
  }

  case class VerseWithRefrain(verse: Option[String], refrain: Option[String])

  class SongFixtureBuilder(private val songToInsert: Tables.LiedRow, private val optFileToInsert: Option[InsertableFile], private val seqVersesWithRefrain: Seq[VerseWithRefrain]) {
    private val createdRows = new FixtureCreatedObjects()

    def withFile(f: InsertableFile) = new SongFixtureBuilder(songToInsert, Some(f), seqVersesWithRefrain)

    def withVersesAndRefrain(versesWithRefrain: Seq[VerseWithRefrain]) = new SongFixtureBuilder(songToInsert, optFileToInsert, versesWithRefrain)

    def buildAndExecute(implicit db: Db): FixtureCreatedObjects = {

      val insertAction = for (
        insertedSong: scotty.generatedschema.Tables.LiedRow <- insertSong();
        _ <- insertFile(insertedSong.id);
        _ <- insertVersesWithRefrain(insertedSong.id, seqVersesWithRefrain)
      ) yield ();

      val future = db.db.run(insertAction.transactionally)
      Await.result(future, Duration.Inf)

      createdRows
    }

    private def insertSong(): DBIOAction[scotty.generatedschema.Tables.LiedRow, NoStream, Write] = {
      ((Tables.Lied returning Tables.Lied.map(_.id) into ((item, id) => item.copy(id = id))) += songToInsert)
        .map(insertedSong => {
          createdRows.addRow(insertedSong)
          insertedSong
        })
    }

    private def insertFile(insertedSongId: Long): DBIOAction[_, NoStream, Write] = {
      optFileToInsert match {
        case Some(value) =>
          ((Tables.Filemetadata returning Tables.Filemetadata.map(_.id) into ((item, id) => item.copy(id = id))) += value.generateMetadataRow().copy(liedId = insertedSongId))
            .flatMap(insertedFileMetadata => {
              (Tables.File returning Tables.File.map(_.id) into ((item, id) => item.copy(id = id))) += value.generateFileRow().copy(filemetadataId = insertedFileMetadata.id)
            })
        case None =>
          DBIO.successful(None)
      }
    }

    @tailrec
    private def insertVersesWithRefrain(insertedSongId: Long, versesWithRefrainToBeInserted: Seq[VerseWithRefrain], acc: DBIOAction[_, NoStream, Write] = DBIOAction.successful(None)): DBIOAction[_, NoStream, Write] = {

      versesWithRefrainToBeInserted match {
        case Seq(first, tail @ _*) =>

          val refrainInsertAction: DBIOAction[Option[generatedschema.Tables.RefrainRow], NoStream, Write] = first.refrain match {
            case Some(refrainText) =>
              ((Tables.Refrain returning Tables.Refrain.map(_.id) into ((item, id) => item.copy(id = id))) += Tables.RefrainRow(0, Some(insertedSongId), None, Some(refrainText), None))
                .map(insertedRefrainRow => {
                  Some(insertedRefrainRow)
                })
            case None =>
              DBIOAction.successful(None)
          }

          val insertAction = refrainInsertAction
            .flatMap(optInsertedRefrainRow => {
              (Tables.Liedtext returning Tables.Liedtext.map(_.id) into ((item, id) => item.copy(id = id))) += Tables.LiedtextRow(0, insertedSongId, None, None, first.verse, optInsertedRefrainRow.map(_.id), None, None)
            })

          insertVersesWithRefrain(insertedSongId, tail, acc andThen insertAction)
        case _ =>
          acc
      }
    }

  }

  /**
   * Use this DefaultSongFixture if you just need a songToInsert with a pdf.
   */
  object DefaultSongFixture {
    val title = "Revelation Song"
    val pdfUrl = getClass.getResource("revelationSong.pdf")

    def generateRevelationSong(implicit db: Db): _root_.ch.scotty.generatedschema.Tables.LiedRow = {
      val aSong = (songBuilder withTitle title withSectionId 1 withLastEditUserId 1 withTonality Tonality.CA).build()
      val aFile = SongFixture.InsertableFile(pdfUrl)
      val createdRows = (songFixtureBuilder(aSong) withFile aFile).buildAndExecute;
      createdRows.findRow(classOf[Tables.LiedRow])
    }

    def generateSongWithVersesAndRefrain(implicit db: Db, versesWithRefrain: Seq[VerseWithRefrain]): _root_.ch.scotty.generatedschema.Tables.LiedRow = {
      val aSong = (songBuilder withTitle title withSectionId 1 withLastEditUserId 1 withTonality Tonality.CA).build()
      val createdRows = (songFixtureBuilder(aSong) withVersesAndRefrain (versesWithRefrain)).buildAndExecute;
      createdRows.findRow(classOf[Tables.LiedRow])
    }
  }

  /**
   * Use this SongFixtureWithVersesAndRefrain if you just need a song with some verses and refrains.
   */
  object SongFixtureWithVersesAndRefrain {
    val title = "Jerusalem (Dunkelheit bedeckt)"
    val defaultRefrain = "Jerusalem, Jerusalem, <br/>leg’ Dein Gewand der Trauer ab! <br/>Jerusalem, Jerusalem, <br/>singe und tanze Deinem Gott!"
    val defaultVerse1 = "|: Dunkelheit bedeckt alle Völker der Welt, <br/>auf Jerusalem werde Licht! :|<br/><br/>Blick empor, schaue aus, <br/>beben soll Dein Herz und sich öffnen weit.<br/>Auf den Armen trägt man die Töchter herbei,<br/>Deine Söhne kommen von fern."
    val defaultVerse2 = "|: Völker wandern hin, bringen Weihrauch und Gold, <br/>Herden von Kamelen sind Dein. :|<br/><br/>Von weit her kommen sie, <br/>prachtvoll ist Dein Glanz Deiner Herrlichkeit. <br/>Weihrauch steigt empor, weithin schallt Gottes Lob.<br/>Ruhmreich sind die Taten des Herrn."
    val defaultVerse3 = "|: Jubelt in der Stadt, alle, die ihr sie liebt, <br/>fröhlich sollt ihr sein und euch freun! :|<br/><br/>Stadt des Herrn nennt man Dich, <br/>ewig leuchtet Dir Gott der Herr als Licht.<br/>Zion singe laut, denn Dein König bringt Dir <br/>Freudenöl statt Trauergewand."

    val defaultVersesWithRefrain: Seq[VerseWithRefrain] =
      Seq(
        VerseWithRefrain(Some(defaultVerse1), None),
        VerseWithRefrain(Some(defaultVerse2), Some(defaultRefrain)),
        VerseWithRefrain(Some(defaultVerse3), Some(defaultRefrain))
      )

    def generate(versesWithRefrain: Seq[VerseWithRefrain] = defaultVersesWithRefrain)(implicit db: Db): _root_.ch.scotty.generatedschema.Tables.LiedRow = {
      val aSong = (songBuilder withTitle title withSectionId 1 withLastEditUserId 1 withTonality Tonality.CA).build()
      val createdRows = (songFixtureBuilder(aSong) withVersesAndRefrain(versesWithRefrain)).buildAndExecute;
      createdRows.findRow(classOf[Tables.LiedRow])
    }
  }

}