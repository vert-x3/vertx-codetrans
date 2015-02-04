package io.vertx.codetrans;

import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class DataObjectModel extends ExpressionModel {

  final ExpressionModel expression;

  public DataObjectModel(ExpressionModel expression) {
    this.expression = expression;
  }

  @Override
  public ExpressionModel onMethodInvocation(String methodName, List<ExpressionModel> arguments) {
    if (DataObjectLiteralModel.isSet(methodName)) {
      return ExpressionModel.render( writer -> {
        writer.getLang().renderDataObjectAssign(expression,
            ExpressionModel.render(DataObjectLiteralModel.unwrapSet(methodName)),
            arguments.get(0), writer);
      });
    }
    if (DataObjectLiteralModel.isGet(methodName)) {
      return ExpressionModel.render( writer -> {
        writer.getLang().renderDataObjectMemberSelect(expression,
            ExpressionModel.render(DataObjectLiteralModel.unwrapSet(methodName)), writer);
      });
    }
    throw new UnsupportedOperationException("TODO");
  }
  @Override
  public void render(CodeWriter writer) {
    expression.render(writer);
  }
}
