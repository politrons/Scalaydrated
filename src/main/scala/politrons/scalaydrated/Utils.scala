package politrons.scalaydrated

import com.couchbase.client.java.document.json.{JsonArray, JsonObject}
import politrons.scalaydrated.Constants.EVENTS

/**
  * Created by pabloperezgarcia on 01/11/2016.
  */
object Utils {

  implicit class anyUtils(any: Any) {

    def toJsonObject = JsonObject.from(any.asInstanceOf[java.util.HashMap[String, JsonObject]])

  }

  implicit class jsonObjectUtils (jsonObject:JsonObject){

    def events: JsonArray=jsonObject.getArray(EVENTS)

  }


}
