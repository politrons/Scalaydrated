package model

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
  def loadAccount(username: String, password: String) {
    this.userName = username
    this.password = password
  }

  /**
    * Rehydrate product to be added in user model
    */
  def loadProduct(productId: String, productName: String, productPrice: String) {
    products = Product(productId, productName, productPrice) +: products
  }

  /**
    * Rehydrate productId to be deleted in user model
    */
  def removeProduct(productId: String): Unit = {
    products = products.filter(product => !product.productId.equals(productId))
  }

}


