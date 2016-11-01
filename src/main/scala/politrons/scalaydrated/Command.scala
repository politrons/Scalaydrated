package politrons.scalaydrated

/**
  * Created by pabloperezgarcia on 01/11/2016.
  */
trait Command[M<:Model, E<:Event] {

  def event:E

  def action:(M, E) => Unit

}
