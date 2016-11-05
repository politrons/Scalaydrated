package politrons.scalaydrated.command

import politrons.scalaydrated.event.ProductAdded
import politrons.scalaydrated.Command

class AddProductCommand(productId: String, productName: String,
                        productPrice: String) extends Command[ProductAdded] {

  override def event = ProductAdded(productId, productName, productPrice)

}
