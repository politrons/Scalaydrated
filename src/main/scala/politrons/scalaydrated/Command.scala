package politrons.scalaydrated

/**
  * Created by pabloperezgarcia on 01/11/2016.
  */
trait Command[E] {

  def event:E

}
