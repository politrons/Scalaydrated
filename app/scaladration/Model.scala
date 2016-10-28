package scaladration


trait Model {

  var dao: PersistenceDAO = _

  def setPersistence(persistence: PersistenceDAO): Unit ={
    dao=persistence
  }

}
