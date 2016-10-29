import java.util.UUID

import event.{ProductAdded, UserCreated}
import model.User
import org.junit.Test
import persistence.CouchbaseDAO
import politrons.scalaydrated.PersistenceModel._

/**
  * Created by pabloperezgarcia on 28/10/2016.
  */
class EventSourcingTest {

  /**
    * Return the actor model to be rehydrate
    */
  val user = initialize[User](new CouchbaseDAO())

  /**
    * In this particular example we use three methods of the API
    * 1º {createDocument} Create an empty document with an array of empty Events
    * 2º {appendEvent} Append an event into the document and pass the function to be applied in the rehydrate
    * 3º {rehydrate} Rehydrate the model with all events persisted
    */
  @Test
  def createAccountTest() {
    val (userName: String, password: String, id: String) = getCredentials
    val documentId: String = user.createDocument(id)
    val event = new UserCreated(userName, password)
    user.appendEvent[UserCreated, User](documentId, event, classOf[UserCreated],
      (model, evt) => model.loadAccount(evt.userName, evt.password))
    user.rehydrate(documentId)
    assert(user.userName.equals(userName) && user.password.equals(password))
  }

  @Test
  def shoppingTest() {
    val (userName: String, password: String, id: String) = getCredentials
    val documentId: String = user.createDocument(id)
    val event = new ProductAdded("Beans","1.00")
    user.appendEvent[ProductAdded, User](id, event, classOf[ProductAdded],
      (model, evt) => model.loadProduct(evt.productName, evt.productPrice))
    user.rehydrate(documentId)
    assert(user.products.size ==1)
  }

  def getCredentials: (String, String, String) = {
    val userName = UUID.randomUUID().toString
    val password = "password"
    val id = userName + password
    (userName, password, id)
  }


}
