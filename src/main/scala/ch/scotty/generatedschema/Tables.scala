package ch.scotty.generatedschema
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.driver.MySQLDriver
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.driver.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Array(EveryLiedInEveryLiederbuchView.schema, File.schema, Filemetadata.schema, Fkliederbuchlied.schema, Fkliedlied.schema, Language.schema, Lied.schema, Liederbuch.schema, LiedTableView.schema, Liedtext.schema, Liedview.schema, Logging.schema, Refrain.schema, Rubrik.schema, Settings.schema, User.schema).reduceLeft(_ ++ _)
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table EveryLiedInEveryLiederbuchView
   *  @param id Database column id SqlType(BIGINT), Default(0)
   *  @param titel Database column Titel SqlType(TEXT)
   *  @param idLiederbuch Database column id_liederbuch SqlType(BIGINT), Default(0)
   *  @param buchname Database column Buchname SqlType(TEXT), Default(None)
   *  @param rubrik Database column Rubrik SqlType(TEXT), Default(None)
   *  @param tonality Database column tonality SqlType(VARCHAR), Length(30,true), Default(None)
   *  @param createdAt Database column created_at SqlType(TIMESTAMP), Default(None)
   *  @param updatedAt Database column updated_at SqlType(TIMESTAMP), Default(None)
   *  @param lastedituserId Database column lastEditUser_id SqlType(BIGINT)
   *  @param email Database column email SqlType(VARCHAR), Length(150,true), Default(None) */
  case class EveryLiedInEveryLiederbuchViewRow(id: Long = 0L, titel: String, idLiederbuch: Long = 0L, buchname: Option[String] = None, rubrik: Option[String] = None, tonality: Option[String] = None, createdAt: Option[java.sql.Timestamp] = None, updatedAt: Option[java.sql.Timestamp] = None, lastedituserId: Long, email: Option[String] = None)
  /** GetResult implicit for fetching EveryLiedInEveryLiederbuchViewRow objects using plain SQL queries */
  implicit def GetResultEveryLiedInEveryLiederbuchViewRow(implicit e0: GR[Long], e1: GR[String], e2: GR[Option[String]], e3: GR[Option[java.sql.Timestamp]]): GR[EveryLiedInEveryLiederbuchViewRow] = GR{
    prs => import prs._
    EveryLiedInEveryLiederbuchViewRow.tupled((<<[Long], <<[String], <<[Long], <<?[String], <<?[String], <<?[String], <<?[java.sql.Timestamp], <<?[java.sql.Timestamp], <<[Long], <<?[String]))
  }
  /** Table description of table every_lied_in_every_liederbuch_view. Objects of this class serve as prototypes for rows in queries. */
  class EveryLiedInEveryLiederbuchView(_tableTag: Tag) extends Table[EveryLiedInEveryLiederbuchViewRow](_tableTag, "every_lied_in_every_liederbuch_view") {
    def * = (id, titel, idLiederbuch, buchname, rubrik, tonality, createdAt, updatedAt, lastedituserId, email) <> (EveryLiedInEveryLiederbuchViewRow.tupled, EveryLiedInEveryLiederbuchViewRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(titel), Rep.Some(idLiederbuch), buchname, rubrik, tonality, createdAt, updatedAt, Rep.Some(lastedituserId), email).shaped.<>({r=>import r._; _1.map(_=> EveryLiedInEveryLiederbuchViewRow.tupled((_1.get, _2.get, _3.get, _4, _5, _6, _7, _8, _9.get, _10)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), Default(0) */
    val id: Rep[Long] = column[Long]("id", O.Default(0L))
    /** Database column Titel SqlType(TEXT) */
    val titel: Rep[String] = column[String]("Titel")
    /** Database column id_liederbuch SqlType(BIGINT), Default(0) */
    val idLiederbuch: Rep[Long] = column[Long]("id_liederbuch", O.Default(0L))
    /** Database column Buchname SqlType(TEXT), Default(None) */
    val buchname: Rep[Option[String]] = column[Option[String]]("Buchname", O.Default(None))
    /** Database column Rubrik SqlType(TEXT), Default(None) */
    val rubrik: Rep[Option[String]] = column[Option[String]]("Rubrik", O.Default(None))
    /** Database column tonality SqlType(VARCHAR), Length(30,true), Default(None) */
    val tonality: Rep[Option[String]] = column[Option[String]]("tonality", O.Length(30,varying=true), O.Default(None))
    /** Database column created_at SqlType(TIMESTAMP), Default(None) */
    val createdAt: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("created_at", O.Default(None))
    /** Database column updated_at SqlType(TIMESTAMP), Default(None) */
    val updatedAt: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("updated_at", O.Default(None))
    /** Database column lastEditUser_id SqlType(BIGINT) */
    val lastedituserId: Rep[Long] = column[Long]("lastEditUser_id")
    /** Database column email SqlType(VARCHAR), Length(150,true), Default(None) */
    val email: Rep[Option[String]] = column[Option[String]]("email", O.Length(150,varying=true), O.Default(None))
  }
  /** Collection-like TableQuery object for table EveryLiedInEveryLiederbuchView */
  lazy val EveryLiedInEveryLiederbuchView = new TableQuery(tag => new EveryLiedInEveryLiederbuchView(tag))

  /** Entity class storing rows of table File
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param filemetadataId Database column filemetadata_id SqlType(BIGINT)
   *  @param data Database column data SqlType(MEDIUMBLOB)
   *  @param filename Database column filename SqlType(VARCHAR), Length(255,true)
   *  @param filesize Database column filesize SqlType(VARCHAR), Length(50,true)
   *  @param filetype Database column filetype SqlType(VARCHAR), Length(4,true) */
  case class FileRow(id: Long, filemetadataId: Long, data: java.sql.Blob, filename: String, filesize: String, filetype: String)
  /** GetResult implicit for fetching FileRow objects using plain SQL queries */
  implicit def GetResultFileRow(implicit e0: GR[Long], e1: GR[java.sql.Blob], e2: GR[String]): GR[FileRow] = GR{
    prs => import prs._
    FileRow.tupled((<<[Long], <<[Long], <<[java.sql.Blob], <<[String], <<[String], <<[String]))
  }
  /** Table description of table file. Objects of this class serve as prototypes for rows in queries. */
  class File(_tableTag: Tag) extends Table[FileRow](_tableTag, "file") {
    def * = (id, filemetadataId, data, filename, filesize, filetype) <> (FileRow.tupled, FileRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(filemetadataId), Rep.Some(data), Rep.Some(filename), Rep.Some(filesize), Rep.Some(filetype)).shaped.<>({r=>import r._; _1.map(_=> FileRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column filemetadata_id SqlType(BIGINT) */
    val filemetadataId: Rep[Long] = column[Long]("filemetadata_id")
    /** Database column data SqlType(MEDIUMBLOB) */
    val data: Rep[java.sql.Blob] = column[java.sql.Blob]("data")
    /** Database column filename SqlType(VARCHAR), Length(255,true) */
    val filename: Rep[String] = column[String]("filename", O.Length(255,varying=true))
    /** Database column filesize SqlType(VARCHAR), Length(50,true) */
    val filesize: Rep[String] = column[String]("filesize", O.Length(50,varying=true))
    /** Database column filetype SqlType(VARCHAR), Length(4,true) */
    val filetype: Rep[String] = column[String]("filetype", O.Length(4,varying=true))

    /** Foreign key referencing Filemetadata (database name file_ibfk_1) */
    lazy val filemetadataFk = foreignKey("file_ibfk_1", filemetadataId, Filemetadata)(r => r.id, onUpdate=ForeignKeyAction.Cascade, onDelete=ForeignKeyAction.Cascade)

    /** Uniqueness Index over (filemetadataId) (database name filemetadata_id_UNIQUE) */
    val index1 = index("filemetadata_id_UNIQUE", filemetadataId, unique=true)
  }
  /** Collection-like TableQuery object for table File */
  lazy val File = new TableQuery(tag => new File(tag))

  /** Entity class storing rows of table Filemetadata
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param filetype Database column filetype SqlType(ENUM), Length(10,false)
   *  @param liedId Database column lied_id SqlType(BIGINT) */
  case class FilemetadataRow(id: Long, filetype: String, liedId: Long)
  /** GetResult implicit for fetching FilemetadataRow objects using plain SQL queries */
  implicit def GetResultFilemetadataRow(implicit e0: GR[Long], e1: GR[String]): GR[FilemetadataRow] = GR{
    prs => import prs._
    FilemetadataRow.tupled((<<[Long], <<[String], <<[Long]))
  }
  /** Table description of table filemetadata. Objects of this class serve as prototypes for rows in queries. */
  class Filemetadata(_tableTag: Tag) extends Table[FilemetadataRow](_tableTag, "filemetadata") {
    def * = (id, filetype, liedId) <> (FilemetadataRow.tupled, FilemetadataRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(filetype), Rep.Some(liedId)).shaped.<>({r=>import r._; _1.map(_=> FilemetadataRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column filetype SqlType(ENUM), Length(10,false) */
    val filetype: Rep[String] = column[String]("filetype", O.Length(10,varying=false))
    /** Database column lied_id SqlType(BIGINT) */
    val liedId: Rep[Long] = column[Long]("lied_id")

    /** Foreign key referencing Lied (database name filemetadata_ibfk_1) */
    lazy val liedFk = foreignKey("filemetadata_ibfk_1", liedId, Lied)(r => r.id, onUpdate=ForeignKeyAction.Cascade, onDelete=ForeignKeyAction.Cascade)
  }
  /** Collection-like TableQuery object for table Filemetadata */
  lazy val Filemetadata = new TableQuery(tag => new Filemetadata(tag))

  /** Entity class storing rows of table Fkliederbuchlied
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param liederbuchId Database column liederbuch_id SqlType(BIGINT)
   *  @param liedId Database column lied_id SqlType(BIGINT)
   *  @param liednr Database column Liednr SqlType(VARCHAR), Length(20,true) */
  case class FkliederbuchliedRow(id: Long, liederbuchId: Long, liedId: Long, liednr: String)
  /** GetResult implicit for fetching FkliederbuchliedRow objects using plain SQL queries */
  implicit def GetResultFkliederbuchliedRow(implicit e0: GR[Long], e1: GR[String]): GR[FkliederbuchliedRow] = GR{
    prs => import prs._
    FkliederbuchliedRow.tupled((<<[Long], <<[Long], <<[Long], <<[String]))
  }
  /** Table description of table fkliederbuchlied. Objects of this class serve as prototypes for rows in queries. */
  class Fkliederbuchlied(_tableTag: Tag) extends Table[FkliederbuchliedRow](_tableTag, "fkliederbuchlied") {
    def * = (id, liederbuchId, liedId, liednr) <> (FkliederbuchliedRow.tupled, FkliederbuchliedRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(liederbuchId), Rep.Some(liedId), Rep.Some(liednr)).shaped.<>({r=>import r._; _1.map(_=> FkliederbuchliedRow.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column liederbuch_id SqlType(BIGINT) */
    val liederbuchId: Rep[Long] = column[Long]("liederbuch_id")
    /** Database column lied_id SqlType(BIGINT) */
    val liedId: Rep[Long] = column[Long]("lied_id")
    /** Database column Liednr SqlType(VARCHAR), Length(20,true) */
    val liednr: Rep[String] = column[String]("Liednr", O.Length(20,varying=true))

    /** Foreign key referencing Lied (database name fkLiederbuchLiedLied) */
    lazy val liedFk = foreignKey("fkLiederbuchLiedLied", liedId, Lied)(r => r.id, onUpdate=ForeignKeyAction.Cascade, onDelete=ForeignKeyAction.Cascade)
    /** Foreign key referencing Liederbuch (database name fkLiederbuchLiedLiederbuch) */
    lazy val liederbuchFk = foreignKey("fkLiederbuchLiedLiederbuch", liederbuchId, Liederbuch)(r => r.id, onUpdate=ForeignKeyAction.Cascade, onDelete=ForeignKeyAction.Cascade)

    /** Uniqueness Index over (liederbuchId,liedId) (database name PreventFromDifferentLiedNrForTheSameSongInASongbook) */
    val index1 = index("PreventFromDifferentLiedNrForTheSameSongInASongbook", (liederbuchId, liedId), unique=true)
    /** Uniqueness Index over (liederbuchId,liednr) (database name PreventFromHavingTheSameLiedNrTwiceInOneSongbook) */
    val index2 = index("PreventFromHavingTheSameLiedNrTwiceInOneSongbook", (liederbuchId, liednr), unique=true)
  }
  /** Collection-like TableQuery object for table Fkliederbuchlied */
  lazy val Fkliederbuchlied = new TableQuery(tag => new Fkliederbuchlied(tag))

  /** Entity class storing rows of table Fkliedlied
   *  @param lied1Id Database column lied1_id SqlType(BIGINT)
   *  @param lied2Id Database column lied2_id SqlType(BIGINT)
   *  @param comment Database column comment SqlType(TEXT), Default(None)
   *  @param `type` Database column type SqlType(INT), Default(None) */
  case class FkliedliedRow(lied1Id: Long, lied2Id: Long, comment: Option[String] = None, `type`: Option[Int] = None)
  /** GetResult implicit for fetching FkliedliedRow objects using plain SQL queries */
  implicit def GetResultFkliedliedRow(implicit e0: GR[Long], e1: GR[Option[String]], e2: GR[Option[Int]]): GR[FkliedliedRow] = GR{
    prs => import prs._
    FkliedliedRow.tupled((<<[Long], <<[Long], <<?[String], <<?[Int]))
  }
  /** Table description of table fkliedlied. Objects of this class serve as prototypes for rows in queries.
   *  NOTE: The following names collided with Scala keywords and were escaped: type */
  class Fkliedlied(_tableTag: Tag) extends Table[FkliedliedRow](_tableTag, "fkliedlied") {
    def * = (lied1Id, lied2Id, comment, `type`) <> (FkliedliedRow.tupled, FkliedliedRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(lied1Id), Rep.Some(lied2Id), comment, `type`).shaped.<>({r=>import r._; _1.map(_=> FkliedliedRow.tupled((_1.get, _2.get, _3, _4)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column lied1_id SqlType(BIGINT) */
    val lied1Id: Rep[Long] = column[Long]("lied1_id")
    /** Database column lied2_id SqlType(BIGINT) */
    val lied2Id: Rep[Long] = column[Long]("lied2_id")
    /** Database column comment SqlType(TEXT), Default(None) */
    val comment: Rep[Option[String]] = column[Option[String]]("comment", O.Default(None))
    /** Database column type SqlType(INT), Default(None)
     *  NOTE: The name was escaped because it collided with a Scala keyword. */
    val `type`: Rep[Option[Int]] = column[Option[Int]]("type", O.Default(None))

    /** Foreign key referencing Lied (database name fkLiedLiedLied1) */
    lazy val liedFk1 = foreignKey("fkLiedLiedLied1", lied1Id, Lied)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Lied (database name fkLiedLiedLied2) */
    lazy val liedFk2 = foreignKey("fkLiedLiedLied2", lied2Id, Lied)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)

    /** Index over (lied1Id,lied2Id) (database name PrimaryKey) */
    val index1 = index("PrimaryKey", (lied1Id, lied2Id))
  }
  /** Collection-like TableQuery object for table Fkliedlied */
  lazy val Fkliedlied = new TableQuery(tag => new Fkliedlied(tag))

  /** Entity class storing rows of table Language
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param code Database column code SqlType(VARCHAR), Length(5,true)
   *  @param name Database column name SqlType(VARCHAR), Length(45,true), Default(None) */
  case class LanguageRow(id: Long, code: String, name: Option[String] = None)
  /** GetResult implicit for fetching LanguageRow objects using plain SQL queries */
  implicit def GetResultLanguageRow(implicit e0: GR[Long], e1: GR[String], e2: GR[Option[String]]): GR[LanguageRow] = GR{
    prs => import prs._
    LanguageRow.tupled((<<[Long], <<[String], <<?[String]))
  }
  /** Table description of table language. Objects of this class serve as prototypes for rows in queries. */
  class Language(_tableTag: Tag) extends Table[LanguageRow](_tableTag, "language") {
    def * = (id, code, name) <> (LanguageRow.tupled, LanguageRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(code), name).shaped.<>({r=>import r._; _1.map(_=> LanguageRow.tupled((_1.get, _2.get, _3)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column code SqlType(VARCHAR), Length(5,true) */
    val code: Rep[String] = column[String]("code", O.Length(5,varying=true))
    /** Database column name SqlType(VARCHAR), Length(45,true), Default(None) */
    val name: Rep[Option[String]] = column[Option[String]]("name", O.Length(45,varying=true), O.Default(None))

    /** Uniqueness Index over (code) (database name code_UNIQUE) */
    val index1 = index("code_UNIQUE", code, unique=true)
    /** Uniqueness Index over (name) (database name name_UNIQUE) */
    val index2 = index("name_UNIQUE", name, unique=true)
  }
  /** Collection-like TableQuery object for table Language */
  lazy val Language = new TableQuery(tag => new Language(tag))

  /** Entity class storing rows of table Lied
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param titel Database column Titel SqlType(TEXT)
   *  @param rubrikId Database column rubrik_id SqlType(BIGINT), Default(None)
   *  @param stichwoerter Database column Stichwoerter SqlType(TEXT), Default(None)
   *  @param bemerkungen Database column Bemerkungen SqlType(TEXT), Default(None)
   *  @param createdAt Database column created_at SqlType(TIMESTAMP), Default(None)
   *  @param updatedAt Database column updated_at SqlType(TIMESTAMP), Default(None)
   *  @param externallink Database column externalLink SqlType(TEXT), Default(None)
   *  @param lastedituserId Database column lastEditUser_id SqlType(BIGINT)
   *  @param tonality Database column tonality SqlType(VARCHAR), Length(30,true), Default(None) */
  case class LiedRow(id: Long, titel: String, rubrikId: Option[Long] = None, stichwoerter: Option[String] = None, bemerkungen: Option[String] = None, createdAt: Option[java.sql.Timestamp] = None, updatedAt: Option[java.sql.Timestamp] = None, externallink: Option[String] = None, lastedituserId: Long, tonality: Option[String] = None)
  /** GetResult implicit for fetching LiedRow objects using plain SQL queries */
  implicit def GetResultLiedRow(implicit e0: GR[Long], e1: GR[String], e2: GR[Option[Long]], e3: GR[Option[String]], e4: GR[Option[java.sql.Timestamp]]): GR[LiedRow] = GR{
    prs => import prs._
    LiedRow.tupled((<<[Long], <<[String], <<?[Long], <<?[String], <<?[String], <<?[java.sql.Timestamp], <<?[java.sql.Timestamp], <<?[String], <<[Long], <<?[String]))
  }
  /** Table description of table lied. Objects of this class serve as prototypes for rows in queries. */
  class Lied(_tableTag: Tag) extends Table[LiedRow](_tableTag, "lied") {
    def * = (id, titel, rubrikId, stichwoerter, bemerkungen, createdAt, updatedAt, externallink, lastedituserId, tonality) <> (LiedRow.tupled, LiedRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(titel), rubrikId, stichwoerter, bemerkungen, createdAt, updatedAt, externallink, Rep.Some(lastedituserId), tonality).shaped.<>({r=>import r._; _1.map(_=> LiedRow.tupled((_1.get, _2.get, _3, _4, _5, _6, _7, _8, _9.get, _10)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column Titel SqlType(TEXT) */
    val titel: Rep[String] = column[String]("Titel")
    /** Database column rubrik_id SqlType(BIGINT), Default(None) */
    val rubrikId: Rep[Option[Long]] = column[Option[Long]]("rubrik_id", O.Default(None))
    /** Database column Stichwoerter SqlType(TEXT), Default(None) */
    val stichwoerter: Rep[Option[String]] = column[Option[String]]("Stichwoerter", O.Default(None))
    /** Database column Bemerkungen SqlType(TEXT), Default(None) */
    val bemerkungen: Rep[Option[String]] = column[Option[String]]("Bemerkungen", O.Default(None))
    /** Database column created_at SqlType(TIMESTAMP), Default(None) */
    val createdAt: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("created_at", O.Default(None))
    /** Database column updated_at SqlType(TIMESTAMP), Default(None) */
    val updatedAt: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("updated_at", O.Default(None))
    /** Database column externalLink SqlType(TEXT), Default(None) */
    val externallink: Rep[Option[String]] = column[Option[String]]("externalLink", O.Default(None))
    /** Database column lastEditUser_id SqlType(BIGINT) */
    val lastedituserId: Rep[Long] = column[Long]("lastEditUser_id")
    /** Database column tonality SqlType(VARCHAR), Length(30,true), Default(None) */
    val tonality: Rep[Option[String]] = column[Option[String]]("tonality", O.Length(30,varying=true), O.Default(None))

    /** Foreign key referencing Rubrik (database name liedRubrik) */
    lazy val rubrikFk = foreignKey("liedRubrik", rubrikId, Rubrik)(r => Rep.Some(r.id), onUpdate=ForeignKeyAction.Cascade, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing User (database name liedLastEditUser) */
    lazy val userFk = foreignKey("liedLastEditUser", lastedituserId, User)(r => r.id, onUpdate=ForeignKeyAction.Cascade, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Lied */
  lazy val Lied = new TableQuery(tag => new Lied(tag))

  /** Entity class storing rows of table Liederbuch
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param buchname Database column Buchname SqlType(TEXT), Default(None)
   *  @param beschreibung Database column Beschreibung SqlType(TEXT), Default(None)
   *  @param mnemonic Database column mnemonic SqlType(VARCHAR), Length(5,true)
   *  @param locked Database column locked SqlType(BIT), Default(false) */
  case class LiederbuchRow(id: Long, buchname: Option[String] = None, beschreibung: Option[String] = None, mnemonic: String, locked: Boolean = false)
  /** GetResult implicit for fetching LiederbuchRow objects using plain SQL queries */
  implicit def GetResultLiederbuchRow(implicit e0: GR[Long], e1: GR[Option[String]], e2: GR[String], e3: GR[Boolean]): GR[LiederbuchRow] = GR{
    prs => import prs._
    LiederbuchRow.tupled((<<[Long], <<?[String], <<?[String], <<[String], <<[Boolean]))
  }
  /** Table description of table liederbuch. Objects of this class serve as prototypes for rows in queries. */
  class Liederbuch(_tableTag: Tag) extends Table[LiederbuchRow](_tableTag, "liederbuch") {
    def * = (id, buchname, beschreibung, mnemonic, locked) <> (LiederbuchRow.tupled, LiederbuchRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), buchname, beschreibung, Rep.Some(mnemonic), Rep.Some(locked)).shaped.<>({r=>import r._; _1.map(_=> LiederbuchRow.tupled((_1.get, _2, _3, _4.get, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column Buchname SqlType(TEXT), Default(None) */
    val buchname: Rep[Option[String]] = column[Option[String]]("Buchname", O.Default(None))
    /** Database column Beschreibung SqlType(TEXT), Default(None) */
    val beschreibung: Rep[Option[String]] = column[Option[String]]("Beschreibung", O.Default(None))
    /** Database column mnemonic SqlType(VARCHAR), Length(5,true) */
    val mnemonic: Rep[String] = column[String]("mnemonic", O.Length(5,varying=true))
    /** Database column locked SqlType(BIT), Default(false) */
    val locked: Rep[Boolean] = column[Boolean]("locked", O.Default(false))
  }
  /** Collection-like TableQuery object for table Liederbuch */
  lazy val Liederbuch = new TableQuery(tag => new Liederbuch(tag))

  /** Entity class storing rows of table LiedTableView
   *  @param id Database column id SqlType(BIGINT), Default(0)
   *  @param titel Database column Titel SqlType(TEXT)
   *  @param rubrik Database column Rubrik SqlType(TEXT), Default(None)
   *  @param tonality Database column tonality SqlType(VARCHAR), Length(30,true), Default(None)
   *  @param createdAt Database column created_at SqlType(TIMESTAMP), Default(None)
   *  @param updatedAt Database column updated_at SqlType(TIMESTAMP), Default(None)
   *  @param lastedituserId Database column lastEditUser_id SqlType(BIGINT)
   *  @param email Database column email SqlType(VARCHAR), Length(150,true), Default(None) */
  case class LiedTableViewRow(id: Long = 0L, titel: String, rubrik: Option[String] = None, tonality: Option[String] = None, createdAt: Option[java.sql.Timestamp] = None, updatedAt: Option[java.sql.Timestamp] = None, lastedituserId: Long, email: Option[String] = None)
  /** GetResult implicit for fetching LiedTableViewRow objects using plain SQL queries */
  implicit def GetResultLiedTableViewRow(implicit e0: GR[Long], e1: GR[String], e2: GR[Option[String]], e3: GR[Option[java.sql.Timestamp]]): GR[LiedTableViewRow] = GR{
    prs => import prs._
    LiedTableViewRow.tupled((<<[Long], <<[String], <<?[String], <<?[String], <<?[java.sql.Timestamp], <<?[java.sql.Timestamp], <<[Long], <<?[String]))
  }
  /** Table description of table lied_table_view. Objects of this class serve as prototypes for rows in queries. */
  class LiedTableView(_tableTag: Tag) extends Table[LiedTableViewRow](_tableTag, "lied_table_view") {
    def * = (id, titel, rubrik, tonality, createdAt, updatedAt, lastedituserId, email) <> (LiedTableViewRow.tupled, LiedTableViewRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(titel), rubrik, tonality, createdAt, updatedAt, Rep.Some(lastedituserId), email).shaped.<>({r=>import r._; _1.map(_=> LiedTableViewRow.tupled((_1.get, _2.get, _3, _4, _5, _6, _7.get, _8)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), Default(0) */
    val id: Rep[Long] = column[Long]("id", O.Default(0L))
    /** Database column Titel SqlType(TEXT) */
    val titel: Rep[String] = column[String]("Titel")
    /** Database column Rubrik SqlType(TEXT), Default(None) */
    val rubrik: Rep[Option[String]] = column[Option[String]]("Rubrik", O.Default(None))
    /** Database column tonality SqlType(VARCHAR), Length(30,true), Default(None) */
    val tonality: Rep[Option[String]] = column[Option[String]]("tonality", O.Length(30,varying=true), O.Default(None))
    /** Database column created_at SqlType(TIMESTAMP), Default(None) */
    val createdAt: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("created_at", O.Default(None))
    /** Database column updated_at SqlType(TIMESTAMP), Default(None) */
    val updatedAt: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("updated_at", O.Default(None))
    /** Database column lastEditUser_id SqlType(BIGINT) */
    val lastedituserId: Rep[Long] = column[Long]("lastEditUser_id")
    /** Database column email SqlType(VARCHAR), Length(150,true), Default(None) */
    val email: Rep[Option[String]] = column[Option[String]]("email", O.Length(150,varying=true), O.Default(None))
  }
  /** Collection-like TableQuery object for table LiedTableView */
  lazy val LiedTableView = new TableQuery(tag => new LiedTableView(tag))

  /** Entity class storing rows of table Liedtext
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param liedId Database column lied_id SqlType(BIGINT)
   *  @param ueberschrift Database column Ueberschrift SqlType(TEXT), Default(None)
   *  @param ueberschrifttyp Database column UeberschriftTyp SqlType(TEXT), Default(None)
   *  @param strophe Database column Strophe SqlType(TEXT), Default(None)
   *  @param refrainId Database column refrain_id SqlType(BIGINT), Default(None)
   *  @param reihenfolge Database column Reihenfolge SqlType(INT), Default(None)
   *  @param languageId Database column language_id SqlType(BIGINT), Default(None) */
  case class LiedtextRow(id: Long, liedId: Long, ueberschrift: Option[String] = None, ueberschrifttyp: Option[String] = None, strophe: Option[String] = None, refrainId: Option[Long] = None, reihenfolge: Option[Int] = None, languageId: Option[Long] = None)
  /** GetResult implicit for fetching LiedtextRow objects using plain SQL queries */
  implicit def GetResultLiedtextRow(implicit e0: GR[Long], e1: GR[Option[String]], e2: GR[Option[Long]], e3: GR[Option[Int]]): GR[LiedtextRow] = GR{
    prs => import prs._
    LiedtextRow.tupled((<<[Long], <<[Long], <<?[String], <<?[String], <<?[String], <<?[Long], <<?[Int], <<?[Long]))
  }
  /** Table description of table liedtext. Objects of this class serve as prototypes for rows in queries. */
  class Liedtext(_tableTag: Tag) extends Table[LiedtextRow](_tableTag, "liedtext") {
    def * = (id, liedId, ueberschrift, ueberschrifttyp, strophe, refrainId, reihenfolge, languageId) <> (LiedtextRow.tupled, LiedtextRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(liedId), ueberschrift, ueberschrifttyp, strophe, refrainId, reihenfolge, languageId).shaped.<>({r=>import r._; _1.map(_=> LiedtextRow.tupled((_1.get, _2.get, _3, _4, _5, _6, _7, _8)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column lied_id SqlType(BIGINT) */
    val liedId: Rep[Long] = column[Long]("lied_id")
    /** Database column Ueberschrift SqlType(TEXT), Default(None) */
    val ueberschrift: Rep[Option[String]] = column[Option[String]]("Ueberschrift", O.Default(None))
    /** Database column UeberschriftTyp SqlType(TEXT), Default(None) */
    val ueberschrifttyp: Rep[Option[String]] = column[Option[String]]("UeberschriftTyp", O.Default(None))
    /** Database column Strophe SqlType(TEXT), Default(None) */
    val strophe: Rep[Option[String]] = column[Option[String]]("Strophe", O.Default(None))
    /** Database column refrain_id SqlType(BIGINT), Default(None) */
    val refrainId: Rep[Option[Long]] = column[Option[Long]]("refrain_id", O.Default(None))
    /** Database column Reihenfolge SqlType(INT), Default(None) */
    val reihenfolge: Rep[Option[Int]] = column[Option[Int]]("Reihenfolge", O.Default(None))
    /** Database column language_id SqlType(BIGINT), Default(None) */
    val languageId: Rep[Option[Long]] = column[Option[Long]]("language_id", O.Default(None))

    /** Foreign key referencing Language (database name liedtextLanguage) */
    lazy val languageFk = foreignKey("liedtextLanguage", languageId, Language)(r => Rep.Some(r.id), onUpdate=ForeignKeyAction.Cascade, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Lied (database name liedtextLied) */
    lazy val liedFk = foreignKey("liedtextLied", liedId, Lied)(r => r.id, onUpdate=ForeignKeyAction.Cascade, onDelete=ForeignKeyAction.Cascade)
    /** Foreign key referencing Refrain (database name liedtextRefrain) */
    lazy val refrainFk = foreignKey("liedtextRefrain", refrainId, Refrain)(r => Rep.Some(r.id), onUpdate=ForeignKeyAction.Cascade, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Liedtext */
  lazy val Liedtext = new TableQuery(tag => new Liedtext(tag))

  /** Entity class storing rows of table Liedview
   *  @param id Database column id SqlType(BIGINT), Default(0)
   *  @param liednr Database column Liednr SqlType(VARCHAR), Length(20,true), Default(None)
   *  @param titel Database column Titel SqlType(TEXT)
   *  @param idLiederbuch Database column id_liederbuch SqlType(BIGINT), Default(0)
   *  @param buchname Database column Buchname SqlType(TEXT), Default(None)
   *  @param rubrik Database column Rubrik SqlType(TEXT), Default(None)
   *  @param tonality Database column tonality SqlType(VARCHAR), Length(30,true), Default(None)
   *  @param createdAt Database column created_at SqlType(TIMESTAMP), Default(None)
   *  @param updatedAt Database column updated_at SqlType(TIMESTAMP), Default(None)
   *  @param lastedituserId Database column lastEditUser_id SqlType(BIGINT)
   *  @param email Database column email SqlType(VARCHAR), Length(150,true), Default(None) */
  case class LiedviewRow(id: Long = 0L, liednr: Option[String] = None, titel: String, idLiederbuch: Long = 0L, buchname: Option[String] = None, rubrik: Option[String] = None, tonality: Option[String] = None, createdAt: Option[java.sql.Timestamp] = None, updatedAt: Option[java.sql.Timestamp] = None, lastedituserId: Long, email: Option[String] = None)
  /** GetResult implicit for fetching LiedviewRow objects using plain SQL queries */
  implicit def GetResultLiedviewRow(implicit e0: GR[Long], e1: GR[Option[String]], e2: GR[String], e3: GR[Option[java.sql.Timestamp]]): GR[LiedviewRow] = GR{
    prs => import prs._
    LiedviewRow.tupled((<<[Long], <<?[String], <<[String], <<[Long], <<?[String], <<?[String], <<?[String], <<?[java.sql.Timestamp], <<?[java.sql.Timestamp], <<[Long], <<?[String]))
  }
  /** Table description of table liedview. Objects of this class serve as prototypes for rows in queries. */
  class Liedview(_tableTag: Tag) extends Table[LiedviewRow](_tableTag, "liedview") {
    def * = (id, liednr, titel, idLiederbuch, buchname, rubrik, tonality, createdAt, updatedAt, lastedituserId, email) <> (LiedviewRow.tupled, LiedviewRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), liednr, Rep.Some(titel), Rep.Some(idLiederbuch), buchname, rubrik, tonality, createdAt, updatedAt, Rep.Some(lastedituserId), email).shaped.<>({r=>import r._; _1.map(_=> LiedviewRow.tupled((_1.get, _2, _3.get, _4.get, _5, _6, _7, _8, _9, _10.get, _11)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), Default(0) */
    val id: Rep[Long] = column[Long]("id", O.Default(0L))
    /** Database column Liednr SqlType(VARCHAR), Length(20,true), Default(None) */
    val liednr: Rep[Option[String]] = column[Option[String]]("Liednr", O.Length(20,varying=true), O.Default(None))
    /** Database column Titel SqlType(TEXT) */
    val titel: Rep[String] = column[String]("Titel")
    /** Database column id_liederbuch SqlType(BIGINT), Default(0) */
    val idLiederbuch: Rep[Long] = column[Long]("id_liederbuch", O.Default(0L))
    /** Database column Buchname SqlType(TEXT), Default(None) */
    val buchname: Rep[Option[String]] = column[Option[String]]("Buchname", O.Default(None))
    /** Database column Rubrik SqlType(TEXT), Default(None) */
    val rubrik: Rep[Option[String]] = column[Option[String]]("Rubrik", O.Default(None))
    /** Database column tonality SqlType(VARCHAR), Length(30,true), Default(None) */
    val tonality: Rep[Option[String]] = column[Option[String]]("tonality", O.Length(30,varying=true), O.Default(None))
    /** Database column created_at SqlType(TIMESTAMP), Default(None) */
    val createdAt: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("created_at", O.Default(None))
    /** Database column updated_at SqlType(TIMESTAMP), Default(None) */
    val updatedAt: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("updated_at", O.Default(None))
    /** Database column lastEditUser_id SqlType(BIGINT) */
    val lastedituserId: Rep[Long] = column[Long]("lastEditUser_id")
    /** Database column email SqlType(VARCHAR), Length(150,true), Default(None) */
    val email: Rep[Option[String]] = column[Option[String]]("email", O.Length(150,varying=true), O.Default(None))
  }
  /** Collection-like TableQuery object for table Liedview */
  lazy val Liedview = new TableQuery(tag => new Liedview(tag))

  /** Entity class storing rows of table Logging
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param table Database column table SqlType(VARCHAR), Length(45,true), Default(None)
   *  @param message Database column message SqlType(TEXT), Default(None)
   *  @param userId Database column user_id SqlType(BIGINT), Default(None)
   *  @param timestamp Database column timestamp SqlType(TIMESTAMP), Default(None)
   *  @param logger Database column logger SqlType(VARCHAR), Length(256,true), Default(None)
   *  @param level Database column level SqlType(VARCHAR), Length(32,true), Default(None)
   *  @param thread Database column thread SqlType(INT), Default(None)
   *  @param file Database column file SqlType(VARCHAR), Length(255,true), Default(None)
   *  @param line Database column line SqlType(VARCHAR), Length(10,true), Default(None) */
  case class LoggingRow(id: Long, table: Option[String] = None, message: Option[String] = None, userId: Option[Long] = None, timestamp: Option[java.sql.Timestamp] = None, logger: Option[String] = None, level: Option[String] = None, thread: Option[Int] = None, file: Option[String] = None, line: Option[String] = None)
  /** GetResult implicit for fetching LoggingRow objects using plain SQL queries */
  implicit def GetResultLoggingRow(implicit e0: GR[Long], e1: GR[Option[String]], e2: GR[Option[Long]], e3: GR[Option[java.sql.Timestamp]], e4: GR[Option[Int]]): GR[LoggingRow] = GR{
    prs => import prs._
    LoggingRow.tupled((<<[Long], <<?[String], <<?[String], <<?[Long], <<?[java.sql.Timestamp], <<?[String], <<?[String], <<?[Int], <<?[String], <<?[String]))
  }
  /** Table description of table logging. Objects of this class serve as prototypes for rows in queries. */
  class Logging(_tableTag: Tag) extends Table[LoggingRow](_tableTag, "logging") {
    def * = (id, table, message, userId, timestamp, logger, level, thread, file, line) <> (LoggingRow.tupled, LoggingRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), table, message, userId, timestamp, logger, level, thread, file, line).shaped.<>({r=>import r._; _1.map(_=> LoggingRow.tupled((_1.get, _2, _3, _4, _5, _6, _7, _8, _9, _10)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column table SqlType(VARCHAR), Length(45,true), Default(None) */
    val table: Rep[Option[String]] = column[Option[String]]("table", O.Length(45,varying=true), O.Default(None))
    /** Database column message SqlType(TEXT), Default(None) */
    val message: Rep[Option[String]] = column[Option[String]]("message", O.Default(None))
    /** Database column user_id SqlType(BIGINT), Default(None) */
    val userId: Rep[Option[Long]] = column[Option[Long]]("user_id", O.Default(None))
    /** Database column timestamp SqlType(TIMESTAMP), Default(None) */
    val timestamp: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("timestamp", O.Default(None))
    /** Database column logger SqlType(VARCHAR), Length(256,true), Default(None) */
    val logger: Rep[Option[String]] = column[Option[String]]("logger", O.Length(256,varying=true), O.Default(None))
    /** Database column level SqlType(VARCHAR), Length(32,true), Default(None) */
    val level: Rep[Option[String]] = column[Option[String]]("level", O.Length(32,varying=true), O.Default(None))
    /** Database column thread SqlType(INT), Default(None) */
    val thread: Rep[Option[Int]] = column[Option[Int]]("thread", O.Default(None))
    /** Database column file SqlType(VARCHAR), Length(255,true), Default(None) */
    val file: Rep[Option[String]] = column[Option[String]]("file", O.Length(255,varying=true), O.Default(None))
    /** Database column line SqlType(VARCHAR), Length(10,true), Default(None) */
    val line: Rep[Option[String]] = column[Option[String]]("line", O.Length(10,varying=true), O.Default(None))

    /** Foreign key referencing User (database name loggingUser) */
    lazy val userFk = foreignKey("loggingUser", userId, User)(r => Rep.Some(r.id), onUpdate=ForeignKeyAction.Cascade, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Logging */
  lazy val Logging = new TableQuery(tag => new Logging(tag))

  /** Entity class storing rows of table Refrain
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param liedId Database column lied_id SqlType(BIGINT), Default(None)
   *  @param reihenfolge Database column Reihenfolge SqlType(INT), Default(None)
   *  @param refrain Database column Refrain SqlType(TEXT), Default(None)
   *  @param languageId Database column language_id SqlType(BIGINT), Default(None) */
  case class RefrainRow(id: Long, liedId: Option[Long] = None, reihenfolge: Option[Int] = None, refrain: Option[String] = None, languageId: Option[Long] = None)
  /** GetResult implicit for fetching RefrainRow objects using plain SQL queries */
  implicit def GetResultRefrainRow(implicit e0: GR[Long], e1: GR[Option[Long]], e2: GR[Option[Int]], e3: GR[Option[String]]): GR[RefrainRow] = GR{
    prs => import prs._
    RefrainRow.tupled((<<[Long], <<?[Long], <<?[Int], <<?[String], <<?[Long]))
  }
  /** Table description of table refrain. Objects of this class serve as prototypes for rows in queries. */
  class Refrain(_tableTag: Tag) extends Table[RefrainRow](_tableTag, "refrain") {
    def * = (id, liedId, reihenfolge, refrain, languageId) <> (RefrainRow.tupled, RefrainRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), liedId, reihenfolge, refrain, languageId).shaped.<>({r=>import r._; _1.map(_=> RefrainRow.tupled((_1.get, _2, _3, _4, _5)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column lied_id SqlType(BIGINT), Default(None) */
    val liedId: Rep[Option[Long]] = column[Option[Long]]("lied_id", O.Default(None))
    /** Database column Reihenfolge SqlType(INT), Default(None) */
    val reihenfolge: Rep[Option[Int]] = column[Option[Int]]("Reihenfolge", O.Default(None))
    /** Database column Refrain SqlType(TEXT), Default(None) */
    val refrain: Rep[Option[String]] = column[Option[String]]("Refrain", O.Default(None))
    /** Database column language_id SqlType(BIGINT), Default(None) */
    val languageId: Rep[Option[Long]] = column[Option[Long]]("language_id", O.Default(None))

    /** Foreign key referencing Language (database name refrainLanguage) */
    lazy val languageFk = foreignKey("refrainLanguage", languageId, Language)(r => Rep.Some(r.id), onUpdate=ForeignKeyAction.Cascade, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Lied (database name refrainLied) */
    lazy val liedFk = foreignKey("refrainLied", liedId, Lied)(r => Rep.Some(r.id), onUpdate=ForeignKeyAction.Cascade, onDelete=ForeignKeyAction.Cascade)
  }
  /** Collection-like TableQuery object for table Refrain */
  lazy val Refrain = new TableQuery(tag => new Refrain(tag))

  /** Entity class storing rows of table Rubrik
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param rubrik Database column Rubrik SqlType(TEXT), Default(None)
   *  @param reihenfolge Database column Reihenfolge SqlType(INT), Default(None) */
  case class RubrikRow(id: Long, rubrik: Option[String] = None, reihenfolge: Option[Int] = None)
  /** GetResult implicit for fetching RubrikRow objects using plain SQL queries */
  implicit def GetResultRubrikRow(implicit e0: GR[Long], e1: GR[Option[String]], e2: GR[Option[Int]]): GR[RubrikRow] = GR{
    prs => import prs._
    RubrikRow.tupled((<<[Long], <<?[String], <<?[Int]))
  }
  /** Table description of table rubrik. Objects of this class serve as prototypes for rows in queries. */
  class Rubrik(_tableTag: Tag) extends Table[RubrikRow](_tableTag, "rubrik") {
    def * = (id, rubrik, reihenfolge) <> (RubrikRow.tupled, RubrikRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), rubrik, reihenfolge).shaped.<>({r=>import r._; _1.map(_=> RubrikRow.tupled((_1.get, _2, _3)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column Rubrik SqlType(TEXT), Default(None) */
    val rubrik: Rep[Option[String]] = column[Option[String]]("Rubrik", O.Default(None))
    /** Database column Reihenfolge SqlType(INT), Default(None) */
    val reihenfolge: Rep[Option[Int]] = column[Option[Int]]("Reihenfolge", O.Default(None))
  }
  /** Collection-like TableQuery object for table Rubrik */
  lazy val Rubrik = new TableQuery(tag => new Rubrik(tag))

  /** Entity class storing rows of table Settings
   *  @param key Database column key SqlType(VARCHAR), PrimaryKey, Length(50,true)
   *  @param value Database column value SqlType(TEXT), Default(None) */
  case class SettingsRow(key: String, value: Option[String] = None)
  /** GetResult implicit for fetching SettingsRow objects using plain SQL queries */
  implicit def GetResultSettingsRow(implicit e0: GR[String], e1: GR[Option[String]]): GR[SettingsRow] = GR{
    prs => import prs._
    SettingsRow.tupled((<<[String], <<?[String]))
  }
  /** Table description of table settings. Objects of this class serve as prototypes for rows in queries. */
  class Settings(_tableTag: Tag) extends Table[SettingsRow](_tableTag, "settings") {
    def * = (key, value) <> (SettingsRow.tupled, SettingsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(key), value).shaped.<>({r=>import r._; _1.map(_=> SettingsRow.tupled((_1.get, _2)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column key SqlType(VARCHAR), PrimaryKey, Length(50,true) */
    val key: Rep[String] = column[String]("key", O.PrimaryKey, O.Length(50,varying=true))
    /** Database column value SqlType(TEXT), Default(None) */
    val value: Rep[Option[String]] = column[Option[String]]("value", O.Default(None))
  }
  /** Collection-like TableQuery object for table Settings */
  lazy val Settings = new TableQuery(tag => new Settings(tag))

  /** Entity class storing rows of table User
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param email Database column email SqlType(VARCHAR), Length(150,true), Default(None)
   *  @param hash Database column hash SqlType(VARCHAR), Length(200,true), Default(None)
   *  @param firstname Database column firstname SqlType(VARCHAR), Length(100,true), Default(None)
   *  @param lastname Database column lastname SqlType(VARCHAR), Length(100,true), Default(None)
   *  @param rights Database column rights SqlType(TEXT), Default(None)
   *  @param additionalinfos Database column additionalInfos SqlType(TEXT), Default(None)
   *  @param active Database column active SqlType(BIT), Default(None) */
  case class UserRow(id: Long, email: Option[String] = None, hash: Option[String] = None, firstname: Option[String] = None, lastname: Option[String] = None, rights: Option[String] = None, additionalinfos: Option[String] = None, active: Option[Boolean] = None)
  /** GetResult implicit for fetching UserRow objects using plain SQL queries */
  implicit def GetResultUserRow(implicit e0: GR[Long], e1: GR[Option[String]], e2: GR[Option[Boolean]]): GR[UserRow] = GR{
    prs => import prs._
    UserRow.tupled((<<[Long], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String], <<?[Boolean]))
  }
  /** Table description of table user. Objects of this class serve as prototypes for rows in queries. */
  class User(_tableTag: Tag) extends Table[UserRow](_tableTag, "user") {
    def * = (id, email, hash, firstname, lastname, rights, additionalinfos, active) <> (UserRow.tupled, UserRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), email, hash, firstname, lastname, rights, additionalinfos, active).shaped.<>({r=>import r._; _1.map(_=> UserRow.tupled((_1.get, _2, _3, _4, _5, _6, _7, _8)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column email SqlType(VARCHAR), Length(150,true), Default(None) */
    val email: Rep[Option[String]] = column[Option[String]]("email", O.Length(150,varying=true), O.Default(None))
    /** Database column hash SqlType(VARCHAR), Length(200,true), Default(None) */
    val hash: Rep[Option[String]] = column[Option[String]]("hash", O.Length(200,varying=true), O.Default(None))
    /** Database column firstname SqlType(VARCHAR), Length(100,true), Default(None) */
    val firstname: Rep[Option[String]] = column[Option[String]]("firstname", O.Length(100,varying=true), O.Default(None))
    /** Database column lastname SqlType(VARCHAR), Length(100,true), Default(None) */
    val lastname: Rep[Option[String]] = column[Option[String]]("lastname", O.Length(100,varying=true), O.Default(None))
    /** Database column rights SqlType(TEXT), Default(None) */
    val rights: Rep[Option[String]] = column[Option[String]]("rights", O.Default(None))
    /** Database column additionalInfos SqlType(TEXT), Default(None) */
    val additionalinfos: Rep[Option[String]] = column[Option[String]]("additionalInfos", O.Default(None))
    /** Database column active SqlType(BIT), Default(None) */
    val active: Rep[Option[Boolean]] = column[Option[Boolean]]("active", O.Default(None))

    /** Uniqueness Index over (email) (database name email_UNIQUE) */
    val index1 = index("email_UNIQUE", email, unique=true)
  }
  /** Collection-like TableQuery object for table User */
  lazy val User = new TableQuery(tag => new User(tag))
}
