package politrons.scalaydrated

import java.io.IOException
import java.util.Calendar

import com.couchbase.client.java.document.json.JsonObject.{from, _}
import com.couchbase.client.java.document.json.{JsonArray, JsonObject}
import com.fasterxml.jackson.core.`type`.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper

import scala.reflect._

object PersistenceModel {

  private val TIME: String = "time"
  private val EVENTS: String = "events"
  private val mapper: ObjectMapper = new ObjectMapper
  private val eventMapping = collection.mutable.Map[Class[_ <: Event], (Model, Event) => Unit]()

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

  implicit class model(model: Model) {

    /**
      * This method will create the document where all events for that documentId will be appended.
      *
      * @param documentId for the new document
      * @return
      */
    def createDocument(documentId: String) {
      val userDocument: JsonObject = create.put(TIME, getTime)
      userDocument.put(EVENTS, JsonArray.create)
      val id = model.dao.insert(documentId, userDocument.toString)
      model.setId(id)
    }

    /**
      * This method will append events in the document created.
      *
      * @param event      instance for rehydrate of the model
      * @param action     function to apply over the model during rehydrate
      * @tparam E
      */
    def appendEvent[E <: Event, M <: Model](event: E, action: (M, E) => Unit) {
      val document = model.dao.getDocument(model.id)
      val jsonDocument = JsonObject.fromJson(document)
      jsonDocument.getArray(EVENTS).add(fromJson(event.encode))
      model.dao.replace(model.id, jsonDocument.toString)
      setMapping(event.getClass,action)
    }

    /**
      * Get the document from persistence layer and rehydrate the Model from the events using the id of the instance.
      **/
    def rehydrate() {
      val document = model.dao.getDocument(model.id)
      val jsonDocument = JsonObject.fromJson(document)
      deserialiseEvents(model, jsonDocument)
    }

    /**
      * Get the document from persistence layer and rehydrate the Model using the external id.
      * @param documentId of the document
      */
    def rehydrate(documentId:String) {
      val document = model.dao.getDocument(documentId)
      val jsonDocument = JsonObject.fromJson(document)
      deserialiseEvents(model, jsonDocument)
      model.setId(documentId)
    }


    import scala.collection.JavaConversions._

    private def deserialiseEvents(model: Model, document: JsonObject){
      val array: JsonArray = document.getArray(EVENTS)
      array.toList.toList.foreach { entry =>
        val json = from(entry.asInstanceOf[java.util.HashMap[String, JsonObject]])
        applyEvent(model, deserialiseEvent(json))
      }
    }

    private def deserialiseEvent(event: JsonObject): Event = {
      try {
        mapper.readValue(event.toString, new TypeReference[Event]() {})
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
      * @tparam E Event type to be used as generic
      */
    private def setMapping[E <: Event, M <: Model](clazz: Class[E], fn: (M, E) => Unit) {
      eventMapping += clazz -> fn.asInstanceOf[(Model, Event) => Unit]
    }

    private def applyEvent(model: Model, event: Event) {
      eventMapping(event.getClass).apply(model, event)
    }

    private def getTime: String = {
      Calendar.getInstance().getTime.toString
    }

  }
}

