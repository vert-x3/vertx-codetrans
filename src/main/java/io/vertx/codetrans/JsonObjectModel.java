package io.vertx.codetrans;

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
  public ExpressionModel onMethodInvocation(String methodName, List<ExpressionModel> arguments) {
    switch (methodName) {
      case "put":
        return ExpressionModel.render(writer -> {
          writer.getLang().renderJsonObjectAssign(expression, arguments.get(0), arguments.get(1), writer);
        });
      case "encodePrettily": {
        return ExpressionModel.render(writer -> {
          writer.getLang().renderJsonObjectToString(expression, writer);
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
        if (arguments.size() == 1) {
          return ExpressionModel.render( writer -> {
            writer.getLang().renderJsonObjectMemberSelect(expression, arguments.get(0), writer);
          });
        } else {
          throw unsupported("Invalid arguments " + arguments);
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
