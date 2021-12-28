package ch.scotty.fixture

import ch.scotty.Db
import ch.scotty.generatedschema.Tables
import slick.jdbc.MySQLProfile.api._

import java.sql.Timestamp
import scala.concurrent.Await
import scala.concurrent.duration.Duration

class RefDataFixture(implicit db: Db) {
  private val rubrikRows = Seq(
    Tables.RubrikRow(1, Some("Auferstehung"), Some(100)),
    Tables.RubrikRow(2, Some("Lobpreis"), Some(100)),
    Tables.RubrikRow(3, Some("Heilig Geist"), Some(300)),
    Tables.RubrikRow(6, Some("Andere"), Some(9999)),
    Tables.RubrikRow(7, Some("Anbetung"), Some(400)),
    Tables.RubrikRow(8, Some("Übergangslieder"), Some(500)),
    Tables.RubrikRow(9, Some("Maria"), Some(600)),
    Tables.RubrikRow(10, Some("Scharnierlieder"), Some(700)),
    Tables.RubrikRow(11, Some("Thereslieder"), Some(800)),
    Tables.RubrikRow(12, Some("Messlieder"), Some(900)),
    Tables.RubrikRow(13, Some("Dank"), Some(1000)),
    Tables.RubrikRow(14, Some("Bitte"), Some(1100)),
    Tables.RubrikRow(15, Some("Segen"), Some(1200)))

  private val userRows = Seq(
    Tables.UserRow(1, Some("philippjenni@bluemail.ch"), Some("48d59cd2e9b434f37f41f7f0848205b792e589fa"), Some("Philipp"), Some("Jenni"), None, Some("Luzern"), Some(true)),
    Tables.UserRow(2, Some("jakob_tschudi@hotmail.com"), Some("b53202e49655f9fd12ea1cc8f799c78cdb3c4510"), Some("Jakob"), Some("Tschudi"), None, Some("Luzern"), Some(true)),
    Tables.UserRow(3, Some("mail@simonhuwiler.ch"), Some("cdd5fa5c67bcf2fde37eff871f41ab472cba9ed4"), Some("Simon"), Some("Huwiler"), None, Some("Luzern"), Some(true)),
    Tables.UserRow(6, Some("martidominik@bluewin.ch"), Some("b8642257a2f8586eeeffcf8e8c8832732b7189"), Some("Dominik"), Some("Marti"), None, Some("Aarau"), Some(true))
  )

  private val songbookRows = Seq(
    Tables.LiederbuchRow(1, "Adoray Liederordner", Some("Der grosse Liederordner, der sämtlichen Adorays der Schweiz zur Verfügung steht."), "AL", false),
    Tables.LiederbuchRow(2, "Adoray Zug", Some("Sämtliche Lieder die während der Umstellung vom alten zum neuen Liederbuch vom Adoray Zug übernommen worden sind."), "ZG", false),
    Tables.LiederbuchRow(3, "Adonai", Some("Das Liederbuch Adonai von den Seligpreisungen Zug."), "AI", false),
    Tables.LiederbuchRow(4, "Dir singen wir 2", Some("Das klassische Grüne"), "DSW2", false),
    Tables.LiederbuchRow(5, "Adoray Zürich Liederbuch", Some("Das eigene Liederbuch von Adoray Zürich"), "ALZ", false),
    Tables.LiederbuchRow(6, "Adoray Brig", Some("Das eigene Liederbuch von Adoray Brig"), "BR", false),
    Tables.LiederbuchRow(7, "Adoray Aarau", Some("Das eigene Liederbuch von Adoray Aarau"), "AG", false),
    Tables.LiederbuchRow(8, "Adoray Basel", Some("Das eigene Liederbuch von Adoray Basel"), "BS", false),
    Tables.LiederbuchRow(9, "Adoray Bern", Some("Das eigene Liederbuch von Adoray Bern"), "BE", false),
    Tables.LiederbuchRow(10, "Adoray Chur", Some("Das eigene Liederbuch von Adoray Chur"), "GR", false),
    Tables.LiederbuchRow(11, "Adoray Freiburg", Some("Das eigene Liederbuch von Adoray Freiburg"), "FR", false),
    Tables.LiederbuchRow(12, "Adoray Gossau", Some("Das eigene Liederbuch von Adoray Gossau"), "GS", false),
    Tables.LiederbuchRow(13, "Adoray Kreuzlingen", Some("Das eigene Liederbuch von Adoray Kreuzlingen"), "KN", false),
    Tables.LiederbuchRow(14, "Adoray Luzern", Some("Das eigene Liederbuch von Adoray Luzern"), "LU", false),
    Tables.LiederbuchRow(15, "Adoray Schaffhausen", Some("Das eigene Liederbuch von Adoray Schaffhausen"), "SH", false),
    Tables.LiederbuchRow(16, "Adoray Schwyz", Some("Das eigene Liederbuch von Adoray Schwyz"), "SZ", false),
    Tables.LiederbuchRow(17, "Adoray Uznach", Some("Das eigene Liederbuch von Adoray Uznach"), "UZ", false),
    Tables.LiederbuchRow(18, "Adoray Solothurn", Some("Das eigene Liederbuch von Adoray Solothurn"), "SO", false),
    Tables.LiederbuchRow(19, "Anbetung Arlesheim", Some("Das eigene Liederbuch der Anbetung Arlesheim"), "AA", false),
    Tables.LiederbuchRow(20, "Harp'n'Bowl", Some("Das Liederbuch der Harp'n'Bowl-Veranstaltungen"), "HB", false),
    Tables.LiederbuchRow(21, "Adoray St. Gallen", Some("Das eigene Liederbuch von Adoray St. Gallen"), "SG", false),
    Tables.LiederbuchRow(22, "Jubilate Deo", Some("Das Liederbuch der Jugend 2000"), "JD", false),
    Tables.LiederbuchRow(23, "Gebetsgruppe Lugano", Some("Das eigene Liederbuch der Gebetsgruppe Logano"), "TI", false)
  )

  private val songRows = Seq(
    Tables.LiedRow(803, "Wer ist wie er / Praise Adonai", Some(7), None, None, Some(Timestamp.valueOf("2015-01-16 15:34:49")), Some(Timestamp.valueOf("2016-05-15 18:35:41")), None, 1, Some("A / fis (3♯)")),
    Tables.LiedRow(324, "Bless the Lord, my soul", Some(2), None, None, Some(Timestamp.valueOf("2015-01-16 15:34:13")), Some(Timestamp.valueOf("2016-04-26 16:26:50")), None, 2, Some("F / d (1♭)")),
    Tables.LiedRow(10, "Jerusalem (Dunkelheit bedeckt)", Some(2), None, None, Some(Timestamp.valueOf("2015-01-16 15:34:13")), Some(Timestamp.valueOf("2016-04-26 16:26:50")), None, 2, Some("F / d (1♭)")),
    Tables.LiedRow(11, "Lied with only one refrain", Some(2), None, None, Some(Timestamp.valueOf("2015-01-16 15:34:13")), Some(Timestamp.valueOf("2016-04-26 16:26:50")), None, 2, Some("F / d (1♭)")),
    Tables.LiedRow(12, "Lied with one refrain assigned and one refrain not assigned", Some(2), None, None, Some(Timestamp.valueOf("2015-01-16 15:34:13")), Some(Timestamp.valueOf("2016-04-26 16:26:50")), None, 2, Some("F / d (1♭)"))
  )

  private val fkLiederbuchLiedRows = Seq(
    Tables.FkliederbuchliedRow(763, 18, 803, "129"),
    Tables.FkliederbuchliedRow(1296, 2, 803, "53"),
    Tables.FkliederbuchliedRow(2324, 8, 803, "4"),
    Tables.FkliederbuchliedRow(2325, 8, 10, "10"),
    Tables.FkliederbuchliedRow(2326, 8, 11, "11"),
    Tables.FkliederbuchliedRow(2327, 8, 12, "12")
  )

  private val refrainRows = Seq(
    Tables.RefrainRow(1, Some(10), None, Some("Jerusalem, Jerusalem, <br/>leg’ Dein Gewand der Trauer ab! <br/>Jerusalem, Jerusalem, <br/>singe und tanze Deinem Gott!"), None),
    Tables.RefrainRow(2, Some(11), None, Some(" first line <br>    second line<br/>third line "), None),
    Tables.RefrainRow(3, Some(12), None, Some("refrain 1"), None),
    Tables.RefrainRow(4, Some(12), None, Some("refrain 2"), None)
  )

  private val liedtextRows = Seq(
    Tables.LiedtextRow(2, 10, None, None, Some("|: Dunkelheit bedeckt alle Völker der Welt, <br/>auf Jerusalem werde Licht! :|<br/><br/>Blick empor, schaue aus, <br/>beben soll Dein Herz und sich öffnen weit.<br/>Auf den Armen trägt man die Töchter herbei,<br/>Deine Söhne kommen von fern."), None, Some(1), None),
    Tables.LiedtextRow(1, 10, None, None, Some("|: Völker wandern hin, bringen Weihrauch und Gold, <br/>Herden von Kamelen sind Dein. :|<br/><br/>Von weit her kommen sie, <br/>prachtvoll ist Dein Glanz Deiner Herrlichkeit. <br/>Weihrauch steigt empor, weithin schallt Gottes Lob.<br/>Ruhmreich sind die Taten des Herrn."), Some(1), Some(2), None),
    Tables.LiedtextRow(3, 10, None, None, Some("|: Jubelt in der Stadt, alle, die ihr sie liebt, <br/>fröhlich sollt ihr sein und euch freun! :|<br/><br/>Stadt des Herrn nennt man Dich, <br/>ewig leuchtet Dir Gott der Herr als Licht.<br/>Zion singe laut, denn Dein König bringt Dir <br/>Freudenöl statt Trauergewand."), Some(1), Some(3), None),
    Tables.LiedtextRow(4, 12, None, None, Some("verse 1"), Some(3), None, None),
    Tables.LiedtextRow(5, 12, None, None, Some("verse 2"), Some(3), None, None)
  )

  def insertRefData() = {
    val rowsInsertAction = DBIO.seq(
      Tables.Rubrik.forceInsertAll(rubrikRows),
      Tables.User.forceInsertAll(userRows),
      Tables.Liederbuch.forceInsertAll(songbookRows),
      Tables.Lied.forceInsertAll(songRows),
      Tables.Fkliederbuchlied.forceInsertAll(fkLiederbuchLiedRows),
      Tables.Refrain.forceInsertAll(refrainRows),
      Tables.Liedtext.forceInsertAll(liedtextRows)
    )

    val future = db.db.run(rowsInsertAction)
    Await.result(future, Duration.Inf)
  }

}
