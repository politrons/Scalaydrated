package command

import event.ProductAdded
import model.User
import politrons.scalaydrated.Command

class AddProductCommand(productId: String, productName: String,
                        productPrice: String) extends Command[User, ProductAdded] {

  override def event = ProductAdded(productId, productName, productPrice)

  override def action: (User, ProductAdded) => Unit = {
    (model, evt) => model.load(evt)
  }

}
