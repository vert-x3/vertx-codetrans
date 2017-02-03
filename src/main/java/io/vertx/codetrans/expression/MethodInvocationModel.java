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

  public final ExpressionModel expression;
  public final TypeInfo receiverType;
  public final MethodSignature method;
  public final TypeInfo returnType;
  public final List<TypeInfo> typeArguments;
  public final List<ExpressionModel> argumentModels;
  public final List<TypeInfo> argumentTypes;

  public MethodInvocationModel(CodeBuilder builder, ExpressionModel expression, TypeInfo receiverType, MethodSignature method,
                               TypeInfo returnType, List<TypeInfo> typeArguments, List<ExpressionModel> argumentModels, List<TypeInfo> argumentTypes) {
    super(builder);
    this.expression = expression;
    this.receiverType = receiverType;
    this.method = method;
    this.returnType = returnType;
    this.typeArguments = typeArguments;
    this.argumentModels = argumentModels;
    this.argumentTypes = argumentTypes;
  }

  @Override
  public void render(CodeWriter writer) {
    writer.renderMethodInvocation(expression, receiverType, method, returnType, typeArguments, argumentModels, argumentTypes);
  }
}
