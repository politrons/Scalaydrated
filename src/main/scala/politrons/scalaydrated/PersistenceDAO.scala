package politrons.scalaydrated

/**
  * Created by pabloperezgarcia on 25/10/2016.
  */
trait PersistenceDAO {

  /**
    * Initialize the persistence layer
    */
  def init()

  /**
    * Find the Document by Id and map it into String
    */
  def getDocument(documentId: String): String

  /**
    * Receive a String document and insert into the persistence service
    */
  def insert(documentId:String, document: String): String

  /**
    * Receive a documentId and document to replace the previous document by this new one
    */
  def replace(documentId:String, document: String)

}
