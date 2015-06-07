package io.vertx.codetrans.expression;

import io.vertx.codetrans.CodeBuilder;

import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public abstract class ClassModel extends ExpressionModel {

  public ClassModel(CodeBuilder builder) {
    super(builder);
  }

  public ExpressionModel onNew(List<ExpressionModel> arguments) {
    throw new UnsupportedOperationException();
  }
}
