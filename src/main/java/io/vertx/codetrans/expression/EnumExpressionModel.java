package io.vertx.codetrans.expression;

import io.vertx.codegen.TypeInfo;
import io.vertx.codetrans.CodeBuilder;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class EnumExpressionModel extends ExpressionModel {

  private final TypeInfo.Class.Enum type;

  public EnumExpressionModel(CodeBuilder builder, TypeInfo.Class.Enum type) {
    super(builder);
    this.type = type;
  }

  public TypeInfo getType() {
    return type;
  }

  @Override
  public ExpressionModel onField(String identifier) {
    return builder.render(writer -> {
      writer.renderEnumConstant(type, identifier);
    });
  }
}
