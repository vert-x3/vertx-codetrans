package io.vertx.codetrans.expression;

import io.vertx.codegen.type.TypeInfo;
import io.vertx.codetrans.CodeBuilder;
import io.vertx.codetrans.CodeWriter;
import io.vertx.codetrans.MethodSignature;

import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class MethodInvocationModel extends ExpressionModel {

  final ExpressionModel expression;
  final TypeInfo receiverType;
  final MethodSignature method;
  final TypeInfo returnType;
  final List<ExpressionModel> argumentModels;
  final List<TypeInfo> argumentTypes;

  public MethodInvocationModel(CodeBuilder builder, ExpressionModel expression, TypeInfo receiverType, MethodSignature method, TypeInfo returnType, List<ExpressionModel> argumentModels, List<TypeInfo> argumentTypes) {
    super(builder);
    this.expression = expression;
    this.receiverType = receiverType;
    this.method = method;
    this.returnType = returnType;
    this.argumentModels = argumentModels;
    this.argumentTypes = argumentTypes;
  }

  @Override
  public void render(CodeWriter writer) {
    writer.renderMethodInvocation(expression, receiverType, method, returnType, argumentModels, argumentTypes);
  }
}
