package io.vertx.codetrans;

import io.vertx.codegen.TypeInfo;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ApiTypeModel extends ExpressionModel {

  private final TypeInfo.Class.Api type;

  public ApiTypeModel(Lang lang, TypeInfo.Class.Api type) {
    super(lang);
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
