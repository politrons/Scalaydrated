package model

import src.Model


case class User(var userName: String = "",
                var password: String = "") extends Model {

  def this() {
    this("")
  }

  /**
    * Rehydrate method for event sourcing
    */
  def loadUserName(username: String, password: String) {
    this.userName = username
    this.password = password
  }

}

