package command

import event.UserCreated
import politrons.scalaydrated.Command

class CreateUserCommand(userName: String, password: String) extends Command[UserCreated] {

  override def event = UserCreated(userName, password)

}
