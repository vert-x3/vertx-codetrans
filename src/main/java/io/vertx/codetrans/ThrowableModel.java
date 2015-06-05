package io.vertx.codetrans;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ThrowableModel extends ExpressionModel {

  private final String type;
  private final ExpressionModel reason;

  public ThrowableModel(Lang lang, String type, ExpressionModel reason) {
    super(lang);
    this.type = type;
    this.reason = reason;
  }

  public String getType() {
    return type;
  }

  public ExpressionModel getReason() {
    return reason;
  }
}
