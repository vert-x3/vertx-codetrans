package io.vertx.codetrans.expression;

import io.vertx.codegen.TypeInfo;
import io.vertx.codetrans.CodeBuilder;
import io.vertx.codetrans.CodeWriter;

import java.util.List;

/**
 * A plain Java class, for instance java.lang.Thread . It allows to escape the polyglot Vert.x
 * API, e.g:
 *
 * <code>Thread.sleep(1000)</code>
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class JavaClassModel extends ExpressionModel {

  final TypeInfo.Class type;

  public JavaClassModel(CodeBuilder builder, TypeInfo.Class type) {
    super(builder);
    this.type = type;
  }

  @Override
  public ExpressionModel onNew(List<ExpressionModel> arguments) {
    return builder.render((renderer) -> {
      renderer.renderNew(this, type, arguments);
    }).as(type);
  }

  @Override
  public void render(CodeWriter writer) {
    writer.renderJavaType(type);
  }
}
