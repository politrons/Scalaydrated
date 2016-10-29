package event

import com.fasterxml.jackson.annotation.{JsonCreator, JsonProperty}
import politrons.scalaydrated.Event

class ProductAdded @JsonCreator()(@JsonProperty("productName") var productName: String,
                                  @JsonProperty("productPrice") var productPrice: String) extends Event {


  @JsonProperty("productName") def getProductName: String = {
    productName
  }

  @JsonProperty("productPrice") def getProductPrice: String = {
    productPrice
  }
}
