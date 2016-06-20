package io.vertx.codetrans;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class CustomException extends Exception {

  private final int code;

  public CustomException(int code) {
    this.code = code;
  }

  public int getCode() {
    return code;
  }
}
