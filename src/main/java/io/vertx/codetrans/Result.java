package io.vertx.codetrans;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public abstract class Result {

  public static class Source extends Result {
    private final String value;
    public Source(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }
    @Override
    public String toString() {
      return "Source[" + value + "]";
    }
  }

  public static class Failure extends Result {
    private final Throwable cause;
    public Failure(Throwable cause) {
      this.cause = cause;
    }
    public Throwable getCause() {
      return cause;
    }
  }
}
