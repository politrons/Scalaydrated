import java.util.UUID

import event.UserCreated
import model.User
import politrons.scalaydrated.PersistenceModel._
import org.junit.Test
import persistence.CouchbaseDAO
import politrons.scalaydrated.Model
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
