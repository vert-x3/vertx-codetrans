package io.vertx.scala.core.buffer

class Buffer(delegate: io.vertx.core.buffer.Buffer) {

  def appendString(str: String) = {
    delegate.appendString(str);
    this
  }

  def toString(enc: String): String = {
    this.delegate.toString(enc);
  }
}

object Buffer {
  def buffer(string: String):Buffer = {
    new Buffer(io.vertx.core.buffer.Buffer.buffer(string))
  }
}
