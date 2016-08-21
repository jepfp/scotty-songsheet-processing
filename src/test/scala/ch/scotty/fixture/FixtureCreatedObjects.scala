package ch.scotty.fixture

class FixtureCreatedObjects {

  private var createdRows = scala.collection.mutable.Map[Class[_ <: AnyRef], AnyRef]()

  def throwExceptionIfTypeAlreadyInList(classToCheck: Class[_ <: AnyRef]) = {
    if(createdRows.get(classToCheck).isDefined){
      throw new IllegalArgumentException("You must not add an element of the same class twice to the list!")
    }
  }

  def addRow[B <: AnyRef](createdRow: B) = {
    throwExceptionIfTypeAlreadyInList(createdRow.getClass)
    createdRows += (createdRow.getClass -> createdRow)
  }

  def findRow[B <: AnyRef](classOfRow: Class[B]): B = {
    createdRows.get(classOfRow).get.asInstanceOf[B]
  }

}
