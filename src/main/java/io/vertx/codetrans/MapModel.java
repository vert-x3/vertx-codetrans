package io.vertx.codetrans;

import io.vertx.codegen.TypeInfo;

import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class MapModel extends ExpressionModel {

  private final ExpressionModel expression;

  public MapModel(ExpressionModel expression) {
    this.expression = expression;
  }

  @Override
  public ExpressionModel onMethodInvocation(TypeInfo receiverType, String methodName, TypeInfo returnType, List<TypeInfo> parameterTypes, List<ExpressionModel> argumentModels, List<TypeInfo> argumenTypes) {
    switch (methodName) {
      case "get":
        return ExpressionModel.render(writer -> {
          writer.renderMapGet(expression, argumentModels.get(0));
        });
      case "forEach":
        LambdaExpressionModel lambda = (LambdaExpressionModel) argumentModels.get(0);
        return ExpressionModel.render(writer -> {
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
        throw new UnsupportedOperationException("Map " + methodName + " method not supported");
    }
  }

  @Override
  public void render(CodeWriter writer) {
    expression.render(writer);
  }
}
