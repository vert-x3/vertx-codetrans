package io.vertx.codetrans.expression;

import io.vertx.codegen.type.ApiTypeInfo;
import io.vertx.codetrans.CodeBuilder;
import io.vertx.codetrans.CodeWriter;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ApiTypeModel extends ExpressionModel {

  private final ApiTypeInfo type;

  public ApiTypeModel(CodeBuilder builder, ApiTypeInfo type) {
    super(builder);
    this.type = type;
  }

  public ApiTypeInfo getType() {
    return type;
  }

  @Override
  public void render(CodeWriter writer) {
    writer.renderApiType(type);
  }
}
