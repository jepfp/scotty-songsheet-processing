package ch.scotty

import org.apache.pdfbox.io.IOUtils

import java.net.URL
import javax.sql.rowset.serial.SerialBlob
import scala.util.Using

object SongsheetTestUtils {
  def readFileToBlob(inputFileUrl: URL): java.sql.Blob = {
    val extractableResourceWithBlob =
      Using(inputFileUrl.openStream()) { inputStream =>
        val byteArray: Array[Byte] = IOUtils.toByteArray(inputStream)
        new SerialBlob(byteArray)
      }
    // we need to investigate anyway if optional is not defined.
    extractableResourceWithBlob.get
  }
}
