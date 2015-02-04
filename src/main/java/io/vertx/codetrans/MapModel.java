package io.vertx.codetrans;

import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class MapModel extends ExpressionModel {

  private final ExpressionModel expression;

  public MapModel(ExpressionModel expression) {
    this.expression = expression;
  }

  @Override
  public ExpressionModel onMethodInvocation(String methodName, List<ExpressionModel> arguments) {
    switch (methodName) {
      case "get":
        return ExpressionModel.render(writer -> {
          writer.getLang().renderMapGet(expression, arguments.get(0), writer);
        });
      default:
        throw new UnsupportedOperationException("Map " + methodName + " method not supported");
    }
  }

  @Override
  public void render(CodeWriter writer) {
    expression.render(writer);
  }
}
