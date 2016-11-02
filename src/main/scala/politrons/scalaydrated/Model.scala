package politrons.scalaydrated

/**
  * Created by pabloperezgarcia on 25/10/2016.
  */
trait Model {

  var dao: PersistenceDAO = _

  var id: String = _

  def setId(documentId: String) = this.id = documentId

  def setPersistence(persistence: PersistenceDAO) = dao = persistence

}
