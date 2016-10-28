package scaladration

/**
  * Created by pabloperezgarcia on 25/10/2016.
  */
trait PersistenceDAO {

  /**
    * Initialize the persistence layer
    */
  def init()

  /**
    * Find the Document by Id and map it into JsonObject
    */
  def getDocument(documentId: String): String

  /**
    * Receive a JsonDocument and insert into the bucket
    */
  def insert(documentId:String, document: String): String

  /**
    * Receive a JsonDocument and replace a previous document by this new one
    */
  def replace(documentId:String, document: String)

}
