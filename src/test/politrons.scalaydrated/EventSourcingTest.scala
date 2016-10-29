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

  val user = initialize[User](new CouchbaseDAO())

  @Test
  def createAccountTest() {
    val (userName: String, password: String, id: String) = getDocumentId
    val documentId: String = user.createDocument(id)
    val event = new UserCreated(userName, password)
    user.appendEvent[UserCreated, User](documentId, event, classOf[UserCreated],
      (model, evt) => model.loadAccount(evt.userName, evt.password))
    user.rehydrate(documentId)
    assert(user.userName.equals(userName) && user.password.equals(password))
  }

  @Test
  def shoppingTest() {
    val (userName: String, password: String, id: String) = getDocumentId
    val documentId: String = user.createDocument(id)
    val event = new ProductAdded("Beans","1.00")
    user.appendEvent[ProductAdded, User](id, event, classOf[ProductAdded],
      (model, evt) => model.loadProduct(evt.productName, evt.productPrice))
    user.rehydrate(documentId)
    assert(user.products.size ==1)
  }

  def getDocumentId: (String, String, String) = {
    val userName = UUID.randomUUID().toString
    val password = "password"
    val id = userName + password
    (userName, password, id)
  }


}
