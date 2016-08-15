package ch.scotty.fixture

import java.net.URL

import ch.scotty.generatedschema.Tables
import ch.scotty.{Db, SongsheetTestUtils}
import slick.dbio.DBIOAction
import slick.dbio.Effect.{Transactional, Write}
import slick.driver.MySQLDriver.api._

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
    def withSong(t: Tables.LiedRow) = new SongFixtureBuilder(Some(t), file)

    def withFile(f: InsertableFile) = new SongFixtureBuilder(song, Some(f))

    def buildAndExecute(implicit db: Db) = {
      val songToInsert = song.get

      var songInsertAction: DBIOAction[Long, NoStream, Write with Transactional] = ((Tables.Lied returning Tables.Lied.map(_.id)) += songToInsert).transactionally
      if (file.isDefined) {
        val fileAndMetadataTuple = file.get.generateTableRows()
        val (fileMetadataToInsert, fileToInsert) = fileAndMetadataTuple
        songInsertAction = songInsertAction.flatMap(songId => {
          (Tables.Filemetadata returning Tables.Filemetadata.map(_.id)) += fileMetadataToInsert.copy(liedId = songId)
        }).flatMap(fileMetadataId => {
          (Tables.File returning Tables.File.map(_.id)) += fileToInsert.copy(filemetadataId = fileMetadataId)
        }).transactionally
      }
      val future = db.db.run(songInsertAction)
      Await.result(future, Duration.Inf)

    }
  }

}