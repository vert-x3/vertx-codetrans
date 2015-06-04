package io.vertx.codetrans;

import io.vertx.codegen.TypeInfo;

import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class JsonObjectModel extends ExpressionModel {

  final ExpressionModel expression;

  public JsonObjectModel(ExpressionModel expression) {
    this.expression = expression;
  }

  @Override
  public ExpressionModel onMethodInvocation(TypeInfo receiverType, String methodName, TypeInfo returnType, List<TypeInfo> parameterTypes, List<ExpressionModel> argumentModels, List<TypeInfo> argumenTypes) {
    switch (methodName) {
      case "put":
        return ExpressionModel.render(writer -> {
          writer.renderJsonObjectAssign(expression, argumentModels.get(0), argumentModels.get(1));
        });
      case "encodePrettily": {
        return ExpressionModel.render(writer -> {
          writer.renderJsonObjectToString(expression);
        });
      }
      case "getString":
      case "getJsonObject":
      case "getInteger":
      case "getLong":
      case "getFloat":
      case "getDouble":
      case "getBoolean":
      case "getJsonArray":
      case "getValue":
        if (argumentModels.size() == 1) {
          return ExpressionModel.render( writer -> {
            writer.renderJsonObjectMemberSelect(expression, argumentModels.get(0));
          });
        } else {
          throw unsupported("Invalid arguments " + argumentModels);
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
