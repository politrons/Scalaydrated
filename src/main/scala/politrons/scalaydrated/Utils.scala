package politrons.scalaydrated

import com.couchbase.client.java.document.json.JsonObject

/**
  * Created by pabloperezgarcia on 01/11/2016.
  */
object Utils {

  implicit class anyUtils(any: Any) {

    def toJsonObject = JsonObject.from(any.asInstanceOf[java.util.HashMap[String, JsonObject]])

  }

//  implicit class jsonOject(jsonObject:JsonObject){
//    def toList[T]():List[T]
//  }

}
