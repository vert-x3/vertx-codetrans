package io.vertx.codetrans.expression;

import io.vertx.codegen.type.TypeInfo;
import io.vertx.codetrans.CodeBuilder;
import io.vertx.codetrans.CodeWriter;
import io.vertx.codetrans.MethodSignature;

import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ListModel extends ExpressionModel {

  private final ExpressionModel expression;

  public ListModel(CodeBuilder builder, ExpressionModel expression) {
    super(builder);
    this.expression = expression;
  }

  @Override
  public ExpressionModel onMethodInvocation(TypeInfo receiverType, MethodSignature method, TypeInfo returnType, List<TypeInfo> typeArguments, List<ExpressionModel> argumentModels, List<TypeInfo> argumentTypes) {
    String methodName = method.getName();
    switch (methodName) {
      case "add":
        return builder.render(writer -> {
          writer.renderListAdd(expression, argumentModels.get(0));
        });
      case "size":
        return builder.render(writer -> {
          writer.renderListSize(expression);
        });
      case "get":
        return builder.render(writer -> {
          writer.renderListGet(expression, argumentModels.get(0));
        });
      default:
        throw new UnsupportedOperationException("List " + method + " method not supported");
    }
  }

  @Override
  public void render(CodeWriter writer) {
    expression.render(writer);
  }
}
