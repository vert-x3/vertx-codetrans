package io.vertx.codetrans;

import io.vertx.codetrans.statement.StatementModel;

import java.util.Map;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class RunnableCompilationUnit {

  private final boolean verticle;
  private final MethodModel main;
  private final Map<String, MethodModel> methods;
  private final Map<String, StatementModel> fields;

  public RunnableCompilationUnit(boolean verticle, MethodModel main, Map<String, MethodModel> methods, Map<String, StatementModel> fields) {
    this.verticle = verticle;
    this.main = main;
    this.methods = methods;
    this.fields = fields;
  }

  public boolean isVerticle() {
    return verticle;
  }

  public MethodModel getMain() {
    return main;
  }

  public Map<String, MethodModel> getMethods() {
    return methods;
  }

  public Map<String, StatementModel> getFields() {
    return fields;
  }
}
