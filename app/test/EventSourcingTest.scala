import java.util.UUID

import _root_.model.User
import events.UserCreated
import org.junit.Test
import persistance.CouchbaseDAO
import src.Model
import src.PersistenceModel._

/**
  * Created by pabloperezgarcia on 28/10/2016.
  */
class EventSourcingTest {

  val user = initialize[User](new CouchbaseDAO())

  @Test
  def create() {
    val userName = UUID.randomUUID().toString
    val documentId: String = user.createDocument(userName)
    val event = new UserCreated(documentId, "")
    user.appendEvent(documentId, event, classOf[UserCreated], getCreateUserAction)
    user.rehydrate(documentId)
    assert(user.userName.equals(userName))
  }

  private def getCreateUserAction: (Model, UserCreated) => Unit = {
    (model, evt) => user.loadUserName(evt.userName, evt.password)
  }

}
