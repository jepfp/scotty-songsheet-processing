package ch.scotty

import java.io.File

import org.scalatest.{Outcome, Suite, TestSuite}

trait TestFolder extends TestSuite {
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
