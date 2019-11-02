package ch.scotty.converter.songanize

import java.sql.Timestamp

case class SonganizeSong(
                          id: Long,
                          timestamp: Timestamp,
                          lastChange: Timestamp,
                          userId: Long,
                          filename: String,
                          title: String,
                          tonality: String,
                          pathOnFileSystem: String,
                          filenameOnFileSystem: String,
                          fileType: String)