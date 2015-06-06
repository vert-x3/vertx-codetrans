package io.vertx.codetrans;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class IdentifierModel extends ExpressionModel {

  final String name;
  final IdentifierKind kind;

  public IdentifierModel(CodeBuilder builder, String name, IdentifierKind kind) {
    super(builder);
    this.name = name;
    this.kind = kind;
  }

  @Override
  public void render(CodeWriter writer) {
    writer.renderIdentifier(name, kind);
  }
}
