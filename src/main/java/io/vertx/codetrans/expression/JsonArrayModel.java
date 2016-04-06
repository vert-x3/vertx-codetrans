package io.vertx.codetrans.expression;

import io.vertx.codegen.type.TypeInfo;
import io.vertx.codetrans.CodeBuilder;
import io.vertx.codetrans.CodeWriter;
import io.vertx.codetrans.MethodSignature;

import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class JsonArrayModel extends ExpressionModel {

  final ExpressionModel expression;

  public JsonArrayModel(CodeBuilder builder, ExpressionModel expression) {
    super(builder);
    this.expression = expression;
  }

  @Override
  public ExpressionModel onMethodInvocation(TypeInfo receiverType, MethodSignature method, TypeInfo returnType, List<ExpressionModel> argumentModels, List<TypeInfo> argumenTypes) {
    String methodName = method.getName();
    switch (methodName) {
      case "getString":
      case "getBoolean":
      case "getFloat":
      case "getDouble":
      case "getLong":
      case "getInteger":
        if (argumentModels.size() == 1) {
          return builder.render(writer -> {
            writer.renderJsonArrayGet(expression, argumentModels.get(0));
          });
        } else {
          throw unsupported("Invalid arguments " + argumentModels);
        }
      case "getJsonArray":
        return new JsonArrayModel(builder, builder.render(writer -> {
          writer.renderJsonArrayGet(expression, argumentModels.get(0));
        }));
      case "getJsonObject":
        return new JsonObjectModel(builder, builder.render(writer -> {
          writer.renderJsonArrayGet(expression, argumentModels.get(0));
        }));
      case "encode":
      case "encodePrettily": {
        return builder.jsonArrayEncoder(expression);
      }
      case "add": {
        return builder.render(writer -> {
          writer.renderJsonArrayAdd(expression, argumentModels.get(0));
        });
      }
      case "addNull": {
        return builder.render(writer -> {
          writer.renderJsonArrayAdd(expression, new NullLiteralModel(builder));
        });
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
