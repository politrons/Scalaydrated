package politrons.scalaydrated.event

import com.fasterxml.jackson.annotation.{JsonCreator, JsonProperty}
import politrons.scalaydrated.event.Product.{PRODUCT_ID, PRODUCT_NAME, PRODUCT_PRICE}
import politrons.scalaydrated.model.User
import politrons.scalaydrated.Event

case class ProductAdded @JsonCreator()(@JsonProperty(PRODUCT_ID) var productId: String,
                                  @JsonProperty(PRODUCT_NAME) var productName: String,
                                  @JsonProperty(PRODUCT_PRICE) var productPrice: String) extends Event[User] {


  @JsonProperty(PRODUCT_ID) def getProductId: String = {
    productId
  }

  @JsonProperty(PRODUCT_NAME) def getProductName: String = {
    productName
  }

  @JsonProperty(PRODUCT_PRICE) def getProductPrice: String = {
    productPrice
  }

  override def action(user:User){
    user.load(productId, productName, productPrice)
  }

}

