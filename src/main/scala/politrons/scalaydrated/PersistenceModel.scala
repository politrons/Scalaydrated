package politrons.scalaydrated

import java.io.IOException
import java.util.Calendar

import com.couchbase.client.java.document.json.JsonObject._
import com.couchbase.client.java.document.json.{JsonArray, JsonObject}
import com.fasterxml.jackson.core.`type`.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import politrons.scalaydrated.Constants.EVENTS

import scala.reflect._

/**
  * Created by pabloperezgarcia on 25/10/2016.
  */
object PersistenceModel {

  private val TIME: String = "time"
  private val mapper: ObjectMapper = new ObjectMapper

  /**
    * Initialize the persistence layer, create a new instance of the mode, and set the persistence layer in it
    *
    * @param persistenceDAO persistence layer to persist the document and events
    * @tparam M Model type to be used to be instantiated and set the persistence layer in it.
    * @return new instance of the model
    */
  def initialize[M <: Model : ClassTag](persistenceDAO: PersistenceDAO): M = {
    val model = classTag[M].runtimeClass.newInstance.asInstanceOf[M]
    persistenceDAO.init()
    model.setPersistence(persistenceDAO)
    model
  }

  implicit class model[M <: Model](model: M) {

    import Utils.{anyUtils, jsonObjectUtils}

    import scala.collection.JavaConversions._

    def dao: PersistenceDAO = model.dao

    /**
      * This method will create the document where all events for that documentId will be appended.
      *
      * @param documentId for the new document
      */
    def createDocument(documentId: String) {
      val jsonDocument = create.put(TIME, getTime)
      jsonDocument.put(EVENTS, JsonArray.create)
      val id = dao.insert(documentId, jsonDocument.toString)
      model.setId(id)
    }

    /**
      * This method will append events throw a command in the document created.
      *
      * @param command which will create an event
      */
    def appendEvent[E <: Event[M]](command: Command[E]) {
      val document = dao.getDocument(model.id)
      val jsonDocument = fromJson(document)
      jsonDocument.events.add(fromJson(command.event.encode))
      dao.replace(model.id, jsonDocument.toString)
    }

    /**
      * Get the document from persistence layer and rehydrate the Model from the events using the id of the instance.
      **/
    def rehydrate() {
      val document = dao.getDocument(model.id)
      deserialiseEvents(fromJson(document))
    }

    /**
      * Get the document from persistence layer and rehydrate the Model using the external id.
      *
      * @param documentId of the document
      */
    def rehydrate(documentId: String) {
      val document = dao.getDocument(documentId)
      deserialiseEvents(fromJson(document))
      model.setId(documentId)
    }

    private def deserialiseEvents(jsonDocument: JsonObject) {
      jsonDocument.events.toList.foreach { event =>
        applyEvent(model, deserialiseEvent(event.toJsonObject))
      }
    }

    private def deserialiseEvent(event: JsonObject): Event[M] = {
      try {
        mapper.readValue(event.toString, new TypeReference[Event[M]]() {})
      }
      catch {
        case e: IOException =>
          throw new IllegalArgumentException("Exception parsing JSON: " + event, e)
      }
    }

    private def applyEvent(model: M, event: Event[M]) {
      event.action(model)
    }

    private def getTime: String = {
      Calendar.getInstance().getTime.toString
    }

  }

}

