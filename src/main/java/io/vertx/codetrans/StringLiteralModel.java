package io.vertx.codetrans;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class StringLiteralModel extends ExpressionModel {

  final String value;

  public StringLiteralModel(String value) {
    this.value = value;
  }

  @Override
  public void render(CodeWriter writer) {
    writer.getLang().renderStringLiteral(value, writer);
  }
}
