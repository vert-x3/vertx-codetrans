package io.vertx.codetrans.expression;

import io.vertx.codetrans.CodeBuilder;

import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class JsonArrayClassModel extends ClassModel {

  public JsonArrayClassModel(CodeBuilder builder) {
    super(builder);
  }

  @Override
  public ExpressionModel onNew(List<ExpressionModel> arguments) {
    switch (arguments.size()) {
      case 0:
        return new JsonArrayLiteralModel(builder);
      default:
        throw new UnsupportedOperationException();
    }
  }
}
