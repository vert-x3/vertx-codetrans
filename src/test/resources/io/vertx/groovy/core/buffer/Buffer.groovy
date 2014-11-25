package io.vertx.groovy.core.buffer;

import groovy.transform.CompileStatic

@CompileStatic
public class Buffer {
  final def io.vertx.core.buffer.Buffer delegate;
  public Buffer(io.vertx.core.buffer.Buffer delegate) {
    this.delegate = delegate;
  }
  public static Buffer buffer(String string) {
    return new Buffer(io.vertx.core.buffer.Buffer.buffer(string));
  }
  public String toString(String enc) {
    return this.delegate.toString(enc);
  }
}
