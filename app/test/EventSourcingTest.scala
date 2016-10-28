import persistance.CouchbaseDAO
import src.PersistenceModel._
import _root_.model.User
import events.UserCreated
import src.Model

/**
  * Created by pabloperezgarcia on 28/10/2016.
  */
class EventSourcingTest {

  val user = initialize[User](new CouchbaseDAO())

  def create() {
    val userName = "test"
      val documentId: String = user.createDocument(userName)
      val event = new UserCreated(documentId, "")

      user.appendEvent(documentId, event, classOf[UserCreated], getCreateUserAction)
      user.rehydrate(documentId)
  }

  private def getCreateUserAction: (Model, UserCreated) => Unit = {
    (model, evt) => user.loadUserName(evt.userName, evt.password)
  }

}
