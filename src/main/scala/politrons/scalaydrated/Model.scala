package politrons.scalaydrated

trait Model {

  var dao: PersistenceDAO = _

  var id: String = _

  def setId(documentId: String) = this.id = documentId

  def setPersistence(persistence: PersistenceDAO) = dao = persistence

}
