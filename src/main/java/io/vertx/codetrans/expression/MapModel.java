package io.vertx.codetrans.expression;

import io.vertx.codegen.type.TypeInfo;
import io.vertx.codetrans.CodeBuilder;
import io.vertx.codetrans.CodeWriter;
import io.vertx.codetrans.MethodSignature;

import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class MapModel extends ExpressionModel {

  private final ExpressionModel expression;

  public MapModel(CodeBuilder builder, ExpressionModel expression) {
    super(builder);
    this.expression = expression;
  }

  @Override
  public ExpressionModel onMethodInvocation(TypeInfo receiverType, MethodSignature method, TypeInfo returnType, List<TypeInfo> typeArguments, List<ExpressionModel> argumentModels, List<TypeInfo> argumentTypes) {
    String methodName = method.getName();
    switch (methodName) {
      case "get":
        return builder.render(writer -> {
          writer.renderMapGet(expression, argumentModels.get(0));
        });
      case "put":
        return builder.render(writer -> {
          writer.renderMapPut(expression, argumentModels.get(0), argumentModels.get(1));
        });
      case "forEach":
        LambdaExpressionModel lambda = (LambdaExpressionModel) argumentModels.get(0);
        return builder.render(writer -> {
          writer.renderMapForEach(
              expression,
              lambda.getParameterNames().get(0),
              lambda.getParameterTypes().get(0),
              lambda.getParameterNames().get(1),
              lambda.getParameterTypes().get(1),
              lambda.getBodyKind(),
              lambda.getBody()
          );
        });
      default:
        throw new UnsupportedOperationException("Map " + method + " method not supported");
    }
  }

  @Override
  public void render(CodeWriter writer) {
    expression.render(writer);
  }
}
