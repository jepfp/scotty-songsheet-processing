package ch.scotty.job.json.result

import java.io.File

import org.scalatest._

trait TestFolder extends FlatSpec {
  self: Suite =>
  var testFolder: File = _

  override def withFixture(test: NoArgTest): Outcome = {
    val tempFolder = System.getProperty("java.io.tmpdir")
    var folder: File = null

    folder = new File(tempFolder, "scalatest-" + System.nanoTime)
    folder.mkdirs()

    testFolder = folder

    try {
      super.withFixture(test)
    } finally {
      deleteFile(testFolder)
    }
  }

  private def deleteFile(file: File) {
    if (!file.exists) return
    if (file.isFile) {
      file.delete()
    } else {
      file.listFiles().foreach(deleteFile)
      file.delete()
    }
  }
}
