package io.vertx.scala.support

/**
 * @author <a href="mailto:jochen.mader@codecentric.de">Jochen Mader</a>
 */
object CollectionFactory {
  def createMap() = {
    Map("foo" -> "foo_value")
  }
  def wrapMap(map: Map[String,String]) = {
    map
  }
}
