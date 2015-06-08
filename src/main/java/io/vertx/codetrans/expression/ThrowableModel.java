package io.vertx.codetrans.expression;

import io.vertx.codegen.TypeInfo;
import io.vertx.codetrans.CodeBuilder;
import io.vertx.codetrans.expression.ExpressionModel;

import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ThrowableModel extends ExpressionModel {

  private final String type;
  private final ExpressionModel reason;

  public ThrowableModel(CodeBuilder builder, String type, ExpressionModel reason) {
    super(builder);
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
