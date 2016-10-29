package event

import com.fasterxml.jackson.annotation.{JsonCreator, JsonProperty}
import com.politrons.scalaydrated.Event


class UserCreated @JsonCreator()(@JsonProperty("userName")var userName: String,
                                 @JsonProperty("password")var password: String) extends Event{


  @JsonProperty("userName") def getUserName: String = {
    userName
  }

  @JsonProperty("password") def getPassword: String = {
    password
  }
}
