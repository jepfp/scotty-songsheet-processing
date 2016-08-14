#Generation of Schema
- Use the encapsulated sbt project
- Manual changes are needed afterwords
  - Add SqlType("MEDIUMBLOB") because BLOB would be the automatic mapping which is to small
  
    `val data: Rep[java.sql.Blob] = column[java.sql.Blob]("data", SqlType("MEDIUMBLOB"))`
  - There will be some problem with the views as well (don't remember exactly :-) ).
   