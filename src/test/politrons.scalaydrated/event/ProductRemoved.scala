package event

import com.fasterxml.jackson.annotation.{JsonCreator, JsonProperty}
import event.Product.PRODUCT_ID
import politrons.scalaydrated.Event

case class ProductRemoved @JsonCreator()(@JsonProperty(PRODUCT_ID) var productId: String) extends Event {

  @JsonProperty(PRODUCT_ID) def getProductId: String = {
    productId
  }

}


