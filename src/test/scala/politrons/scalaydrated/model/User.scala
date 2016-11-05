package politrons.scalaydrated.model

import politrons.scalaydrated.Model

class User(var userName: String = "",
                var password: String = "",
                var products: List[Product] = List()) extends Model {

  def this() {
    this("")
  }

  /**
    * Rehydrate user account method
    */
  def load(username:String, password:String) {
    this.userName = username
    this.password = password
  }

  /**
    * Rehydrate product to be added in user model
    */
  def load(productId:String, productName:String,productPrice:String ) {
    products = Product(productId, productName,productPrice) +: products
  }

  /**
    * Rehydrate productId to be deleted in user model
    */
  def load(productId: String): Unit = {
    products = products.filter(product => !product.productId.equals(productId))
  }

}


