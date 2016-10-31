package event

import com.fasterxml.jackson.annotation.{JsonCreator, JsonProperty}
import event.UserCreated.{PASSWORD, USER_NAME}
import politrons.scalaydrated.Event

case class UserCreated @JsonCreator()(@JsonProperty(USER_NAME) var userName: String,
                                 @JsonProperty(PASSWORD) var password: String) extends Event {

  @JsonProperty(USER_NAME) def getUserName: String = {
    userName
  }

  @JsonProperty(PASSWORD) def getPassword: String = {
    password
  }
}

object UserCreated {
  final val USER_NAME = "userName"
  final val PASSWORD = "password"
}