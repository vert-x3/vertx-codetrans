package io.vertx.codetrans.statement;

import io.vertx.codetrans.expression.ExpressionModel;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ConditionalBlockModel {

  final ExpressionModel condition;
  final StatementModel body;

  public ConditionalBlockModel(ExpressionModel condition, StatementModel body) {
    this.condition = condition;
    this.body = body;
  }

  public ExpressionModel getCondition() {
    return condition;
  }

  public StatementModel getBody() {
    return body;
  }
}
