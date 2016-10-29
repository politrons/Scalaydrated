package event

import com.fasterxml.jackson.annotation.{JsonCreator, JsonProperty}
import event.ProductAdded.{PRODUCT_NAME, PRODUCT_PRICE}
import politrons.scalaydrated.Event

object ProductAdded {
  final val PRODUCT_NAME = "productName"
  final val PRODUCT_PRICE = "productPrice"
}

class ProductAdded @JsonCreator()(@JsonProperty(PRODUCT_NAME) var productName: String,
                                  @JsonProperty(PRODUCT_PRICE) var productPrice: String) extends Event {


  @JsonProperty("productName") def getProductName: String = {
    productName
  }

  @JsonProperty("productPrice") def getProductPrice: String = {
    productPrice
  }
}
