package model

import politrons.scalaydrated.Model

case class Product(var productName: String="",
                   var productPrice: String="") extends Model {

  def this() {
    this("")
  }

}


