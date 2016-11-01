package command

import event.UserCreated
import model.User
import politrons.scalaydrated.Command

class CreateUserCommand(userName: String, password: String) extends Command[User, UserCreated] {

  override def event:UserCreated=UserCreated(userName, password)

  override def action: (User, UserCreated) => Unit = {
    (model, evt) => model.load(evt)
  }

}
