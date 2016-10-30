package event

import com.fasterxml.jackson.annotation.{JsonCreator, JsonProperty}
import event.Product.{PRODUCT_ID, PRODUCT_NAME, PRODUCT_PRICE}
import politrons.scalaydrated.Event

class ProductAdded @JsonCreator()(@JsonProperty(PRODUCT_ID) var productId: String,
                                  @JsonProperty(PRODUCT_NAME) var productName: String,
                                  @JsonProperty(PRODUCT_PRICE) var productPrice: String) extends Event {


  @JsonProperty("productId") def getProductId: String = {
    productId
  }

  @JsonProperty("productName") def getProductName: String = {
    productName
  }

  @JsonProperty("productPrice") def getProductPrice: String = {
    productPrice
  }
}

