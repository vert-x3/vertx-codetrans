package io.vertx.codetrans.expression;

import io.vertx.codegen.TypeInfo;
import io.vertx.codetrans.CodeBuilder;
import io.vertx.codetrans.CodeWriter;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ApiTypeModel extends ExpressionModel {

  private final TypeInfo.Class.Api type;

  public ApiTypeModel(CodeBuilder builder, TypeInfo.Class.Api type) {
    super(builder);
    this.type = type;
  }

  public TypeInfo.Class.Api getType() {
    return type;
  }

  @Override
  public void render(CodeWriter writer) {
    writer.renderApiType(type);
  }
}
