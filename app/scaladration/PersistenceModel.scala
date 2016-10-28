package scaladration

import java.io.IOException

import com.couchbase.client.java.document.json.JsonObject.{from, _}
import com.couchbase.client.java.document.json.{JsonArray, JsonObject}
import com.fasterxml.jackson.core.`type`.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper

import scala.reflect._

object PersistenceModel {

  private val EVENTS: String = "events"
  private val mapper: ObjectMapper = new ObjectMapper
  private val eventMapping = collection.mutable.Map[Class[_ <: Event], (Model, Event) => Unit]()

  def initialize[M <: Model : ClassTag](persistenceDAO: PersistenceDAO): M = {
    val model = classTag[M].runtimeClass.newInstance.asInstanceOf[M]
    persistenceDAO.init()
    model.setPersistence(persistenceDAO)
    model
  }

  implicit class model(model: Model) {

    /**
      * This method will create the document where all events for that documentId will be appended.
      * @param documentId for the new document
      * @return
      */
    def createDocument(documentId: String): String = {
      val userDocument: JsonObject = create.put(EVENTS, JsonArray.create)
      model.dao.insert(documentId, userDocument.toString)
    }

    /**
      *  This method will append events in the document created.
      * @param documentId if of the document
      * @param event instance for rehydrate of the model
      * @param clazz event for key of the function mapping
      * @param action function to apply over the model during rehydrate
      * @tparam E
      */
    def appendEvent[E <: Event](documentId: String, event: Event, clazz: Class[E], action: (Model, E) => Unit) {
      val document = model.dao.getDocument(documentId)
      val jsonDocument = JsonObject.fromJson(document)
      jsonDocument.getArray(EVENTS).add(fromJson(event.encode))
      model.dao.replace(documentId, jsonDocument.toString)
      setMapping(clazz, action)
    }

    /**
      * Get the document from persistence layer and rehydrate the Model from the events.
      * @param documentId to get the document for rehydrate the model
      * @return
      */
    def rehydrate(documentId: String) = {
      val document = model.dao.getDocument(documentId)
      val jsonDocument = JsonObject.fromJson(document)
      deserialiseEvents(model, jsonDocument)
    }

    import scala.collection.JavaConversions._

    private def deserialiseEvents(model: Model, document: JsonObject): model = {
      val array: JsonArray = document.getArray(EVENTS)
      array.toList.toList.foreach { entry =>
        val json = from(entry.asInstanceOf[java.util.HashMap[String, JsonObject]])
        applyEvent(model, deserialiseEvent(json))
      }
      model
    }

    private def deserialiseEvent(event: JsonObject): Event = {
      try {
        mapper.readValue(event.toString, new TypeReference[Event]() {
        })
      }
      catch {
        case e: IOException =>
          throw new IllegalArgumentException("Exception parsing JSON: " + event, e)
      }
    }

    /**
      *
      * @param clazz className to be used as key
      * @param fn    function to be used in rehydrate
      * @tparam E      Event type to be used as generic
      */
    private def setMapping[E <: Event](clazz: Class[E], fn: (Model, E) => Unit) {
      eventMapping += clazz -> fn.asInstanceOf[(Model, Event) => Unit]
    }

    private def applyEvent(model: Model, event: Event) {
      eventMapping(event.getClass).apply(model, event)
    }

  }

}

