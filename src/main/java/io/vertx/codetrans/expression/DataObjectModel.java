package io.vertx.codetrans.expression;

import io.vertx.codegen.type.TypeInfo;
import io.vertx.codetrans.CodeBuilder;
import io.vertx.codetrans.CodeWriter;
import io.vertx.codetrans.MethodSignature;

import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class DataObjectModel extends ExpressionModel {

  final ExpressionModel expression;

  public DataObjectModel(CodeBuilder builder, ExpressionModel expression) {
    super(builder);
    this.expression = expression;
  }

  @Override
  public ExpressionModel onMethodInvocation(TypeInfo receiverType, MethodSignature method, TypeInfo returnType, List<ExpressionModel> argumentModels, List<TypeInfo> argumenTypes) {
    String methodName = method.getName();
    if (DataObjectLiteralModel.isSet(methodName)) {
      return builder.render(writer -> {
        writer.renderDataObjectAssign(expression,
            DataObjectLiteralModel.unwrapSet(methodName),
            argumentModels.get(0));
      });
    }
    if (DataObjectLiteralModel.isGet(methodName)) {
      return builder.render(writer -> {
        writer.renderDataObjectMemberSelect(expression,
            DataObjectLiteralModel.unwrapSet(methodName));
      });
    }
    throw new UnsupportedOperationException("Unsupported method " + method + " on object model");
  }
  @Override
  public void render(CodeWriter writer) {
    expression.render(writer);
  }
}
