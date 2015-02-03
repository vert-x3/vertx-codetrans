package io.vertx.codetrans.annotations;

import io.vertx.codetrans.ExpressionModel;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class MapModel extends ExpressionModel {

  private final ExpressionModel expression;

  public MapModel(ExpressionModel expression) {
    this.expression = expression;
  }

  @Override
  public ExpressionModel onMemberSelect(String identifier) {
    return ExpressionModel.forMethodInvocation(args -> {
      switch (identifier) {
        case "get":
          return ExpressionModel.render(writer -> {
            writer.getLang().renderMapGet(expression, args.get(0), writer);
          });
        default:
          throw new UnsupportedOperationException("Map " + identifier + " method not supported");
      }
    });
  }
}
