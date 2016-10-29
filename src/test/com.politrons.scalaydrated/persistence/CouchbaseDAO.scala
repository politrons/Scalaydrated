package persistence

import com.couchbase.client.java.document.JsonDocument
import com.couchbase.client.java.document.json.JsonObject
import com.couchbase.client.java.{AsyncBucket, CouchbaseAsyncCluster}
import com.politrons.scalaydrated.PersistenceDAO

class CouchbaseDAO extends PersistenceDAO {

  private var bucket: AsyncBucket = _

  /**
    * Initialize the cluster and get the bucket to be used for the API
    */
  def init() = {
    if (bucket == null) {
      val cluster = CouchbaseAsyncCluster.create("localhost")
      val bucket = cluster.openBucket("projectV", "politron").toBlocking.first()
      setBucket(bucket)
    }
  }

  /**
    * Find the Document by Id and map it into JsonObject
    */
  def getDocument(documentId: String): String = {
    val document = bucket.get(documentId).toBlocking.first()
    document.content().toString
  }

  /**
    * Receive a JsonDocument and insert into the bucket
    */
  def insert(documentId: String, document: String): String = {
    val couchbaseDocument = createDocument(documentId, document)
    bucket.insert(couchbaseDocument).toBlocking.first().id()
  }

  /**
    * Receive a JsonDocument and replace a previous document by this new one
    */
  def replace(documentId: String, document: String) {
    val couchbaseDocument = createDocument(documentId, document)
    bucket.replace(couchbaseDocument).toBlocking.first()
  }

  private def createDocument(documentId: String, document: String): JsonDocument = {
    JsonDocument.create(documentId, JsonObject.fromJson(document))
  }

  private def setBucket(bucket: AsyncBucket) {
    this.bucket = bucket
  }
}
