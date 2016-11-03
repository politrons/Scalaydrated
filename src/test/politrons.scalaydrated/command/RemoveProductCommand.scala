package command

import event.ProductRemoved
import politrons.scalaydrated.Command

class RemoveProductCommand(productId: String) extends Command[ProductRemoved] {

  override def event = ProductRemoved(productId)

}
