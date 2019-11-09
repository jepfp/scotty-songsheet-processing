package ch.scotty.converter.songanize

import java.sql.Blob

import com.jcraft.jsch.ChannelSftp
import com.typesafe.scalalogging.Logger
import javax.sql.rowset.serial.SerialBlob
import org.apache.pdfbox.io.IOUtils

import scala.util.Try

private class RemoteFileLoader(channelSftp: ChannelSftp) {


  val logger = Logger(classOf[RemoteFileLoader])

  def retrieveFile(remoteFilePath: String): Try[Blob] = {
    Try {
      val inputStream = channelSftp.get(remoteFilePath)
      val byteArray: Array[Byte] = IOUtils.toByteArray(inputStream)
      new SerialBlob(byteArray)
    }
  }

}



