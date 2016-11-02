package command

import event.ProductRemoved
import model.User
import politrons.scalaydrated.Command

class RemoveProductCommand(productId: String) extends Command[User, ProductRemoved] {

  override def event = ProductRemoved(productId)

  override def action: (User, ProductRemoved) => Unit = {
    (model, evt) => model.load(evt)
  }

}
