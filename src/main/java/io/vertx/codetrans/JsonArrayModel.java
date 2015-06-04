package io.vertx.codetrans;

import io.vertx.codegen.TypeInfo;

import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class JsonArrayModel extends ExpressionModel {

  final ExpressionModel expression;

  public JsonArrayModel(ExpressionModel expression) {
    this.expression = expression;
  }

  @Override
  public ExpressionModel onMethodInvocation(TypeInfo receiverType, String methodName, TypeInfo returnType, List<TypeInfo> parameterTypes, List<ExpressionModel> argumentModels, List<TypeInfo> argumenTypes) {
    switch (methodName) {
      case "getString":
      case "getBoolean":
      case "getFloat":
      case "getDouble":
      case "getLong":
      case "getInteger":
        if (argumentModels.size() == 1) {
          return ExpressionModel.render( writer -> {
            writer.renderJsonArrayGet(expression, argumentModels.get(0));
          });
        } else {
          throw unsupported("Invalid arguments " + argumentModels);
        }
      case "getJsonArray":
        return new JsonArrayModel(ExpressionModel.render( writer -> {
          writer.renderJsonArrayGet(expression, argumentModels.get(0));
        }));
      case "getJsonObject":
        return new JsonObjectModel(ExpressionModel.render( writer -> {
          writer.renderJsonArrayGet(expression, argumentModels.get(0));
        }));
      case "encodePrettily": {
        return ExpressionModel.render(writer -> {
          writer.renderJsonArrayToString(expression);
        });
      }
      default:
        throw unsupported("Method " + methodName);
    }
  }
  @Override
  public void render(CodeWriter writer) {
    expression.render(writer);
  }
}
