package ch.scotty.fixture

import java.sql.Timestamp

import ch.scotty.generatedschema.Tables

object SongBuilder {
    class SongBuilder(title:Option[String], sectionId:Option[Long], lastEditUserId:Option[Long], tonality:Option[String]) {
      def withTitle(t:String) = new SongBuilder(Some(t), sectionId, lastEditUserId, tonality)
      def withSectionId(s:Long) = new SongBuilder(title, Some(s), lastEditUserId, tonality)
      def withLastEditUserId(u:Long) = new SongBuilder(title, sectionId, Some(u), tonality)
      def withTonality(t:Tonality.Value) = new SongBuilder(title, sectionId, lastEditUserId, Some(t.toString))

      def build() = Tables.LiedRow(0, title.get, Some(sectionId.get), None, None, Some(Timestamp.valueOf("2016-05-15 18:35:41")), Some(Timestamp.valueOf("2016-05-15 18:35:41")), None, lastEditUserId.get, tonality)
    }

    def songBuilder = new SongBuilder(None, None, None, None)
}
