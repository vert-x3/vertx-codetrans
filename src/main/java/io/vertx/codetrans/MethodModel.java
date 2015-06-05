package io.vertx.codetrans;

import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class MethodModel extends CodeModel {

  final StatementModel statement;
  final List<String> parameterNames;

  public MethodModel(StatementModel statement, List<String> parameterNames) {
    this.statement = statement;
    this.parameterNames = parameterNames;
  }

  public StatementModel getStatement() {
    return statement;
  }

  public List<String> getParameterNames() {
    return parameterNames;
  }

  @Override
  public void render(CodeWriter writer) {
    statement.render(writer);
  }
}
