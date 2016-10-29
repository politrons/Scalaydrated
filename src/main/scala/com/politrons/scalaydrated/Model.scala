package com.politrons.scalaydrated


trait Model {

  var dao: PersistenceDAO = _

  def setPersistence(persistence: PersistenceDAO): Unit ={
    dao=persistence
  }

}
