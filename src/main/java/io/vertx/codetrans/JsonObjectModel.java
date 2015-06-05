package io.vertx.codetrans;

import io.vertx.codegen.TypeInfo;

import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class JsonObjectModel extends ExpressionModel {

  final ExpressionModel expression;

  public JsonObjectModel(Lang lang, ExpressionModel expression) {
    super(lang);
    this.expression = expression;
  }

  @Override
  public ExpressionModel onMethodInvocation(TypeInfo receiverType, MethodRef method, TypeInfo returnType, List<ExpressionModel> argumentModels, List<TypeInfo> argumenTypes) {
    String methodName = method.getName();
    switch (methodName) {
      case "put":
        return lang.render(writer -> {
          writer.renderJsonObjectAssign(expression, argumentModels.get(0), argumentModels.get(1));
        });
      case "encodePrettily": {
        return lang.jsonObjectEncoder(expression);
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
          return lang.render(writer -> {
            writer.renderJsonObjectMemberSelect(expression, argumentModels.get(0));
          });
        } else {
          throw unsupported("Invalid arguments " + argumentModels);
        }
      default:
        throw unsupported("Method " + method);
    }
  }
  @Override
  public void render(CodeWriter writer) {
    expression.render(writer);
  }
}
