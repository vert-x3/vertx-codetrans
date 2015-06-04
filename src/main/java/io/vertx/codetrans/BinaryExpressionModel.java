package io.vertx.codetrans;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class BinaryExpressionModel extends ExpressionModel {

  final ExpressionModel left;
  final String op;
  final ExpressionModel right;

  public BinaryExpressionModel(ExpressionModel left, String op, ExpressionModel right) {
    this.left = left;
    this.op = op;
    this.right = right;
  }

  public ExpressionModel getLeft() {
    return left;
  }

  public String getOp() {
    return op;
  }

  public ExpressionModel getRight() {
    return right;
  }

  @Override
  public void render(CodeWriter writer) {
    writer.getLang().renderBinary(this, writer);
  }
}
