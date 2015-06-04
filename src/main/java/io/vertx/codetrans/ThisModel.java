package io.vertx.codetrans;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ThisModel extends ExpressionModel {

  @Override
  public void render(CodeWriter writer) {
    writer.renderThis();
  }
}
