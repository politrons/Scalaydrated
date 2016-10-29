import java.util.UUID

import event.UserCreated
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
    val userName = UUID.randomUUID().toString
    val password = "password"
    val id = userName + password
    val documentId: String = user.createDocument(id)
    val event = new UserCreated(userName, password)
    user.appendEvent[UserCreated, User](documentId, event, classOf[UserCreated], (model, evt) => model.loadUserName(evt.userName, evt.password))
    user.rehydrate(documentId)
    assert(user.userName.equals(userName) && user.password.equals(password))
  }

  @Test
  def shoppingTest() {
    val userName = UUID.randomUUID().toString
    val documentId: String = user.createDocument(userName)
    val event = new UserCreated(documentId, "")
    user.appendEvent[UserCreated, User](documentId, event, classOf[UserCreated], (model, evt) => model.loadUserName(evt.userName, evt.password))
    user.rehydrate(documentId)
    assert(user.userName.equals(userName))
  }

}
