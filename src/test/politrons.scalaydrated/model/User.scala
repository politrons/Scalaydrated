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
    * Rehydrate shopping account method
    */
  def loadProduct(productName: String, productPrice: String) {
    products = Product(productName, productPrice) +: products
  }

}


