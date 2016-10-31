package model

import event.{ProductAdded, ProductRemoved, UserCreated}
import politrons.scalaydrated.Model

case class User(var userName: String = "",
                var password: String = "",
                var products: List[Product] = List()) extends Model {

  def this() {
    this("")
  }

  /**
    * Rehydrate user account method
    */
  def load(evt:UserCreated) {
    this.userName = evt.userName
    this.password = evt.password
  }

  /**
    * Rehydrate product to be added in user model
    */
  def load(evt:ProductAdded) {
    products = Product(evt.productId, evt.productName, evt.productPrice) +: products
  }

  /**
    * Rehydrate productId to be deleted in user model
    */
  def load(evt: ProductRemoved): Unit = {
    products = products.filter(product => !product.productId.equals(evt.productId))
  }

}


