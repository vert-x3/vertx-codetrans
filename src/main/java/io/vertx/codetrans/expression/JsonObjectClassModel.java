package io.vertx.codetrans.expression;

import io.vertx.codetrans.CodeBuilder;

import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class JsonObjectClassModel extends ClassModel {

  public JsonObjectClassModel(CodeBuilder builder) {
    super(builder);
  }

  @Override
  public ExpressionModel onNew(List<ExpressionModel> arguments) {
    switch (arguments.size()) {
      case 0:
        return new JsonObjectLiteralModel(builder);
      default:
        throw new UnsupportedOperationException();
    }
  }
}
