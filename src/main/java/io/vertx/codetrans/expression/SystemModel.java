package io.vertx.codetrans.expression;

import io.vertx.codetrans.CodeBuilder;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class SystemModel extends ClassModel {

  public SystemModel(CodeBuilder builder) {
    super(builder);
  }

  @Override
  public ExpressionModel onField(String identifier) {
    if (identifier.equals("out")) {
      return new ConsoleModel(builder);
    }
    throw new UnsupportedOperationException("Cannot select " + identifier + " on java.lang.System");
  }
}
