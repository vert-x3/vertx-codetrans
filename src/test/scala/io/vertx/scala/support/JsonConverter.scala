
package io.vertx.scala.support

import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import scala.collection.JavaConverters._

/**
 * @author <a href="mailto:jochen.mader@codecentric.de">Jochen Mader</a>
 */
object JsonConverter {

  def toJsonObject(jsonObject: JsonObject) = {
    jsonObject
  }

  def toJsonArray(arr: JsonArray) = {
    arr
  }

//  def fromJsonObject(obj: JsonObject) = {
//    def m = new
//    obj.forEach { entry ->
//      if (entry.value instanceof JsonObject) {
//        m[entry.key] = fromJsonObject(entry.value)
//      } else if (entry.value instanceof JsonArray) {
//        m[entry.key] = fromJsonArray(entry.value)
//      } else {
//        m[entry.key] = entry.value
//      }
//    }
//    return m;
//  }
//
  def fromJsonArray(arr: JsonArray) = {
    arr
  }
}
