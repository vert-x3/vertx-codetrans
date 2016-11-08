package io.vertx.kotlin.core.buffer;

/**
 * Created by Sergey Mashkov
 */
public class Buffer {
  private final io.vertx.core.buffer.Buffer delegate;

  public Buffer(io.vertx.core.buffer.Buffer delegate) {
    this.delegate = delegate;
  }

  public static Buffer buffer(String string) {
    return new Buffer(io.vertx.core.buffer.Buffer.buffer(string));
  }

  public Buffer appendString(String str) {
    delegate.appendString(str);
    return this;
  }

  public String toString(String enc) {
    return this.delegate.toString(enc);
  }
}
