package io.vertx.codetrans;

import java.util.Map;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class RunnableCompilationUnit {

  private final MethodModel main;
  private final Map<String, MethodModel> members;

  public RunnableCompilationUnit(MethodModel main, Map<String, MethodModel> members) {
    this.main = main;
    this.members = members;
  }

  public MethodModel getMain() {
    return main;
  }

  public Map<String, MethodModel> getMembers() {
    return members;
  }
}
