package ch.scotty.fixture

import java.net.URL

import ch.scotty.fixture.SongBuilder.songBuilder
import ch.scotty.generatedschema.Tables
import ch.scotty.{Db, SongsheetTestUtils}
import slick.dbio.Effect.Write
import slick.driver.MySQLDriver.api._
import slick.sql.FixedSqlAction

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

object SongFixture {

  def songFixtureBuilder = new SongFixtureBuilder(None, None)

  case class InsertableFile(inputFileUrl: URL) {
    def generateTableRows(): (Tables.FilemetadataRow, Tables.FileRow) = {
      val fm = Tables.FilemetadataRow(3, "sourcepdf", 0)
      val data = SongsheetTestUtils.readFileToBlob(inputFileUrl)
      val f = Tables.FileRow(0, 0, data, "fooFile.pdf", data.length().toString, "pdf")
      (fm, f)
    }
  }

  class SongFixtureBuilder(private val song: Option[Tables.LiedRow], private val file: Option[InsertableFile]) {
    private val createdRows = new FixtureCreatedObjects()

    def withSong(t: Tables.LiedRow) = new SongFixtureBuilder(Some(t), file)

    def withFile(f: InsertableFile) = new SongFixtureBuilder(song, Some(f))

    def buildAndExecute(implicit db: Db): FixtureCreatedObjects = {
      val songToInsert = song.get

      val songInsertAction: FixedSqlAction[_root_.ch.scotty.generatedschema.Tables.LiedRow, NoStream, Write] = ((Tables.Lied returning Tables.Lied.map(_.id) into ((item, id) => item.copy(id = id))) += songToInsert)
      if (file.isDefined) {
        val fileAndMetadataTuple = file.get.generateTableRows()
        val (fileMetadataToInsert, fileToInsert) = fileAndMetadataTuple
        val songWithFileInsertAction = songInsertAction.flatMap(insertedSong => {
          createdRows.addRow(insertedSong)
          (Tables.Filemetadata returning Tables.Filemetadata.map(_.id) into ((item, id) => item.copy(id = id))) += fileMetadataToInsert.copy(liedId = insertedSong.id)
        }).flatMap(insertedFileMetadata => {
          createdRows.addRow(insertedFileMetadata)
          (Tables.File returning Tables.File.map(_.id) into ((item, id) => item.copy(id = id))) += fileToInsert.copy(filemetadataId = insertedFileMetadata.id)
        })
        val future = db.db.run(songWithFileInsertAction.transactionally)
        val insertedFile: _root_.ch.scotty.generatedschema.Tables.FileRow = Await.result(future, Duration.Inf)
        createdRows.addRow(insertedFile)
      } else {
        val future = db.db.run(songInsertAction.transactionally)
        val insertedSong: _root_.ch.scotty.generatedschema.Tables.LiedRow = Await.result(future, Duration.Inf)
        createdRows.addRow(insertedSong)
      }
      createdRows
    }
  }

  /**
    * Use this DefaultSongFixture if you just need a song with a pdf.
    */
  object DefaultSongFixture {
    val title = "Revelation Song"
    val pdfUrl = getClass.getResource("revelationSong.pdf")

    def generateRevelationSong(implicit db: Db) : _root_.ch.scotty.generatedschema.Tables.LiedRow = {
      val aSong = songBuilder withTitle title withSectionId 1 withLastEditUserId 1 withTonality Tonality.CA build()
      val aFile = SongFixture.InsertableFile(pdfUrl)
      val createdRows = songFixtureBuilder withSong aSong withFile aFile buildAndExecute;
      createdRows.findRow(classOf[Tables.LiedRow])
    }
  }

}