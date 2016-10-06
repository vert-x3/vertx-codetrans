package io.vertx.codetrans.expression;

import io.vertx.codetrans.CodeBuilder;
import io.vertx.codetrans.CodeWriter;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class IdentifierModel extends ExpressionModel {

  public final String name;
  final VariableScope scope;

  public IdentifierModel(CodeBuilder builder, String name, VariableScope scope) {
    super(builder);
    this.name = name;
    this.scope = scope;
  }

  @Override
  public void render(CodeWriter writer) {
    writer.renderIdentifier(name, scope);
  }
}
