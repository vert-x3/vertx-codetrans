package io.vertx.codetrans.expression;

import io.vertx.codetrans.CodeBuilder;
import io.vertx.codetrans.CodeWriter;

import java.util.List;

/**
 * @author <a href="mailto:plopes@redhat.com">Paulo Lopes</a>
 */
public class NullLiteralModel extends ExpressionModel {

  public NullLiteralModel(CodeBuilder builder) {
    super(builder);
  }

  @Override
  public boolean isStringDecl() {
    return true;
  }

  @Override
  void collectParts(List<Object> parts) {
    parts.add(null);
  }

  public java.lang.String getValue() {
    return null;
  }

  @Override
  public void render(CodeWriter writer) {
    writer.renderNullLiteral();
  }
}
