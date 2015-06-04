package io.vertx.codetrans;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class MethodModel extends CodeModel {

  final StatementModel statement;

  public MethodModel(StatementModel statement) {
    this.statement = statement;
  }

  public StatementModel getStatement() {
    return statement;
  }

  @Override
  public void render(CodeWriter writer) {
    statement.render(writer);
  }
}
