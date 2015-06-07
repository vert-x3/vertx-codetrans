package io.vertx.codetrans.expression;

import io.vertx.codetrans.CodeBuilder;
import io.vertx.codetrans.CodeWriter;

import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class StringLiteralModel extends ExpressionModel {

  final java.lang.String value;

  public StringLiteralModel(CodeBuilder builder, java.lang.String value) {
    super(builder);
    this.value = value;
  }

  @Override
  public boolean isStringDecl() {
    return true;
  }

  @Override
  void collectParts(List<Object> parts) {
    parts.add(value);
  }

  public java.lang.String getValue() {
    return value;
  }

  @Override
  public void render(CodeWriter writer) {
    writer.renderStringLiteral(value);
  }
}
