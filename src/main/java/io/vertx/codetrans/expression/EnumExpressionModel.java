package io.vertx.codetrans.expression;

import io.vertx.codegen.type.EnumTypeInfo;
import io.vertx.codetrans.CodeBuilder;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class EnumExpressionModel extends ExpressionModel {

  private final EnumTypeInfo type;

  public EnumExpressionModel(CodeBuilder builder, EnumTypeInfo type) {
    super(builder);
    this.type = type;
  }

  public EnumTypeInfo getType() {
    return type;
  }

  @Override
  public ExpressionModel onField(String identifier) {
    return new EnumFieldExpressionModel(builder, type, identifier);
  }
}
