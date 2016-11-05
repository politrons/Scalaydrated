package politrons.scalaydrated.event

import com.fasterxml.jackson.annotation.{JsonCreator, JsonProperty}
import politrons.scalaydrated.event.Product.PRODUCT_ID
import politrons.scalaydrated.model.User
import politrons.scalaydrated.Event

case class ProductRemoved @JsonCreator()(@JsonProperty(PRODUCT_ID) var productId: String) extends Event[User] {

  @JsonProperty(PRODUCT_ID) def getProductId: String = {
    productId
  }

  override def action(user:User){
    user.load(productId)
  }

}


