package io.vertx.codetrans.expression;

import io.vertx.codegen.type.EnumTypeInfo;
import io.vertx.codetrans.CodeBuilder;
import io.vertx.codetrans.CodeWriter;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class EnumFieldExpressionModel extends ExpressionModel {

  private final EnumTypeInfo type;
  private final String identifier;

  public EnumFieldExpressionModel(CodeBuilder builder, EnumTypeInfo type, String identifier) {
    super(builder);
    this.type = type;
    this.identifier = identifier;
  }

  @Override
  public ExpressionModel toDataObjectValue() {
    return new StringLiteralModel(builder, identifier);
  }

  @Override
  public void render(CodeWriter writer) {
    writer.renderEnumConstant(type, identifier);
  }
}
