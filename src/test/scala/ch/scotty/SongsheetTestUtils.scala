package ch.scotty

import java.net.URL
import javax.sql.rowset.serial.SerialBlob

import org.apache.pdfbox.io.IOUtils
import resource.{ExtractableManagedResource, _}

object SongsheetTestUtils {
  def readFileToBlob(inputFileUrl: URL): java.sql.Blob = {
    val extractableResourceWithBlob: ExtractableManagedResource[SerialBlob] = for (inputStream <- managed(inputFileUrl.openStream())) yield {
      val byteArray: Array[Byte] = IOUtils.toByteArray(inputStream)
      new SerialBlob(byteArray)
    }
    // we need to investigate anyway if optional is not defined.
    extractableResourceWithBlob.opt.get
  }
}
