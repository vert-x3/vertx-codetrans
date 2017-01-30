package io.vertx.scala.support

import io.vertx.codetrans.MethodExpressionTest
import io.vertx.core.Handler

/**
 * @author <a href="mailto:jochen.mader@codecentric.de">Jochen Mader</a>
 */

class SubHandler extends Handler[String] {
  def handle(event: String):Unit = {
    MethodExpressionTest.event = event
  }

  def instanceHandler(handler: Handler[String]):Unit = {
    handler.handle("hello_instance")
  }
}

object SubHandler {
  def create() = {
    new SubHandler()
  }
  def classHandler(handler: Handler[String]):Unit = {
    handler.handle("hello_class")
  }
}
