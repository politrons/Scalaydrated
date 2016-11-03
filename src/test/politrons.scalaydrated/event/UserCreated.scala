package event

import com.fasterxml.jackson.annotation.{JsonCreator, JsonProperty}
import event.UserCreated.{PASSWORD, USER_NAME}
import model.User
import politrons.scalaydrated.Event

case class UserCreated @JsonCreator()(@JsonProperty(USER_NAME) var userName: String,
                                 @JsonProperty(PASSWORD) var password: String) extends Event[User] {

  @JsonProperty(USER_NAME) def getUserName: String = {
    userName
  }

  @JsonProperty(PASSWORD) def getPassword: String = {
    password
  }

  override def action(user:User){
    user.load(this)
  }
}

object UserCreated {
  final val USER_NAME = "userName"
  final val PASSWORD = "password"
}