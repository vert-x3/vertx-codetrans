package io.vertx.codetrans.expression;

import io.vertx.codetrans.CodeBuilder;
import io.vertx.codetrans.CodeWriter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class BinaryExpressionModel extends ExpressionModel {

  final ExpressionModel left;
  final String op;
  final ExpressionModel right;

  public BinaryExpressionModel(CodeBuilder builder, ExpressionModel left, String op, ExpressionModel right) {
    super(builder);
    this.left = left;
    this.op = op;
    this.right = right;
  }

  @Override
  public boolean isStringDecl() {
    return op.equals("+") && (left.isStringDecl() || right.isStringDecl());
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
    if (op.equals("+") && (left.isStringDecl() || right.isStringDecl())) {
      ArrayList<Object> parts = new ArrayList<>(); collectParts(parts);
      writer.renderStringLiteral(parts);
    } else {
      writer.renderBinary(this);
    }
  }

  void collectParts(List<Object> parts) {
    if (left.isStringDecl()) {
      left.collectParts(parts);
    } else {
      parts.add(left);
    }
    if (right.isStringDecl()) {
      right.collectParts(parts);
    } else {
      parts.add(right);
    }
  }
}
