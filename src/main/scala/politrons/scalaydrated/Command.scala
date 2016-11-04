package politrons.scalaydrated

/**
  * Created by pabloperezgarcia on 01/11/2016.
  */
trait Command[E] {

  /**
    * Every command generate an event which will be persistet to be used for the rehydration of the model
    * @return
    */
  def event:E

}
