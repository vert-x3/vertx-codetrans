package io.vertx.codetrans.statement;

import io.vertx.codetrans.CodeWriter;
import io.vertx.codetrans.expression.ExpressionModel;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ReturnModel extends StatementModel {

  final ExpressionModel expression;

  public ReturnModel(ExpressionModel expression) {
    this.expression = expression;
  }

  public ExpressionModel getExpression() {
    return expression;
  }

  @Override
  public void render(CodeWriter writer) {
    writer.renderReturn(expression);
  }
}
