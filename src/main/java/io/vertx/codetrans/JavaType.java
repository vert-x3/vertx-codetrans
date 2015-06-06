package io.vertx.codetrans;

import io.vertx.codegen.TypeInfo;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class JavaType extends ExpressionModel {

  final TypeInfo.Class type;

  public JavaType(CodeBuilder builder, TypeInfo.Class type) {
    super(builder);
    this.type = type;
  }

  @Override
  public void render(CodeWriter writer) {
    writer.renderJavaType(type);
  }
}
