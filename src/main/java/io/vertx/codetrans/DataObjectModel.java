package io.vertx.codetrans;

import io.vertx.codegen.TypeInfo;

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
  public ExpressionModel onMethodInvocation(String methodName, TypeInfo returnType, List<TypeInfo> parameterTypes, List<ExpressionModel> argumentModels, List<TypeInfo> argumenTypes) {
    if (DataObjectLiteralModel.isSet(methodName)) {
      return ExpressionModel.render( writer -> {
        writer.getLang().renderDataObjectAssign(expression,
            ExpressionModel.render(DataObjectLiteralModel.unwrapSet(methodName)),
            argumentModels.get(0), writer);
      });
    }
    if (DataObjectLiteralModel.isGet(methodName)) {
      return ExpressionModel.render( writer -> {
        writer.getLang().renderDataObjectMemberSelect(expression,
            ExpressionModel.render(DataObjectLiteralModel.unwrapSet(methodName)), writer);
      });
    }
    throw new UnsupportedOperationException("Unsupported method " + methodName + " on object model");
  }
  @Override
  public void render(CodeWriter writer) {
    expression.render(writer);
  }
}
