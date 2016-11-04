package politrons.scalaydrated

import java.util.Calendar

import com.fasterxml.jackson.annotation.{JsonProperty, JsonTypeInfo}
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper

@JsonTypeInfo(
  use = JsonTypeInfo.Id.CLASS,
  include = JsonTypeInfo.As.PROPERTY,
  property = "event")
trait Event[M<:Model] {

  @JsonProperty("time")
  val time: String = Calendar.getInstance().getTime.toString

  def encode: String = {
    val objectMapper = new ObjectMapper with ScalaObjectMapper
    objectMapper.registerModule(DefaultScalaModule)
    objectMapper.writeValueAsString(this)
  }

  /**
    * Method that execute an action over the model to give it the state that contains for that event.
    * @param model which we will give state
    */
  def action(model:M): Unit

}
