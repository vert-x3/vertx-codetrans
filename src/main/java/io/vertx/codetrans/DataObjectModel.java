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
  public ExpressionModel onMethodInvocation(TypeInfo receiverType, MethodRef method, TypeInfo returnType, List<ExpressionModel> argumentModels, List<TypeInfo> argumenTypes) {
    String methodName = method.getName();
    if (DataObjectLiteralModel.isSet(methodName)) {
      return ExpressionModel.render( writer -> {
        writer.renderDataObjectAssign(expression,
            ExpressionModel.render(DataObjectLiteralModel.unwrapSet(methodName)),
            argumentModels.get(0));
      });
    }
    if (DataObjectLiteralModel.isGet(methodName)) {
      return ExpressionModel.render( writer -> {
        writer.renderDataObjectMemberSelect(expression,
            ExpressionModel.render(DataObjectLiteralModel.unwrapSet(methodName)));
      });
    }
    throw new UnsupportedOperationException("Unsupported method " + method + " on object model");
  }
  @Override
  public void render(CodeWriter writer) {
    expression.render(writer);
  }
}
