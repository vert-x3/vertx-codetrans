package io.vertx.codetrans;

import io.vertx.codetrans.statement.StatementModel;

import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class MethodModel extends CodeModel {

  final StatementModel statement;
  final MethodSignature signature;
  final List<String> parameterNames;

  public MethodModel(StatementModel statement, MethodSignature signature, List<String> parameterNames) {
    this.statement = statement;
    this.signature = signature;
    this.parameterNames = parameterNames;
  }

  public StatementModel getStatement() {
    return statement;
  }

  public MethodSignature getSignature() {
    return signature;
  }

  public List<String> getParameterNames() {
    return parameterNames;
  }

  @Override
  public void render(CodeWriter writer) {
    statement.render(writer);
  }
}
