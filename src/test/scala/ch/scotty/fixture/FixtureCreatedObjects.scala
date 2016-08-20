package ch.scotty.fixture

class FixtureCreatedObjects {

  private var createdRows = scala.collection.mutable.Map[Class[_ <: AnyRef], AnyRef]()

  def addRow[B <: AnyRef](classOfCreatedRow: Class[B], createdRow: B) = {
    createdRows += (classOfCreatedRow -> createdRow)
  }

  def addRow[B <: AnyRef](createdRow: B) = {
    createdRows += (createdRow.getClass -> createdRow)
  }

  def findRow[B <: AnyRef](classOfRow: Class[B]): B = {
    createdRows.get(classOfRow).get.asInstanceOf[B]
  }

}
