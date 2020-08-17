package io.vertx.codetrans.expression;

import io.vertx.codetrans.CodeBuilder;
import io.vertx.codetrans.CodeWriter;

import java.util.List;

public class NewArrayModel extends ExpressionModel {

  private final String primitiveType;
  private final List<ExpressionModel> values;

  public NewArrayModel(CodeBuilder builder, String primitiveType, List<ExpressionModel> values) {
    super(builder);
    this.primitiveType = primitiveType;
    this.values = values;
  }

  @Override
  public void render(CodeWriter writer) {
    writer.renderNewArray(primitiveType, values);
  }
}
