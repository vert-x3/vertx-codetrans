package io.vertx.codetrans.expression;

import io.vertx.codegen.type.TypeInfo;
import io.vertx.codetrans.CodeBuilder;
import io.vertx.codetrans.CodeWriter;
import io.vertx.codetrans.MethodSignature;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class JsonObjectModel extends ExpressionModel {

  static final Map<String, Class<?>> classMapping = new HashMap<>();

  static {
    classMapping.put("Integer", Integer.class);
    classMapping.put("Long", Long.class);
    classMapping.put("Float", Float.class);
    classMapping.put("Double", Double.class);
    classMapping.put("Boolean", Boolean.class);
    classMapping.put("JsonObject", JsonObject.class);
    classMapping.put("JsonArray", JsonArray.class);
    classMapping.put("Value", Object.class);
    classMapping.put("String", String.class);
  }

  final ExpressionModel expression;

  public JsonObjectModel(CodeBuilder builder, ExpressionModel expression) {
    super(builder);
    this.expression = expression;
  }

  @Override
  public ExpressionModel onMethodInvocation(TypeInfo receiverType, MethodSignature method, TypeInfo returnType, List<ExpressionModel> argumentModels, List<TypeInfo> argumenTypes) {
    String methodName = method.getName();
    switch (methodName) {
      case "put":
        return builder.render(writer -> {
          StringLiteralModel name = (StringLiteralModel) argumentModels.get(0);
          writer.renderJsonObjectAssign(expression, name.value, argumentModels.get(1));
        });
      case "putNull":
        return builder.render(writer -> {
          StringLiteralModel name = (StringLiteralModel) argumentModels.get(0);
          writer.renderJsonObjectAssign(expression, name.value, new NullLiteralModel(builder));
        });
      case "encodePrettily":
      case "encode": {
        return builder.jsonObjectEncoder(expression);
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
          return builder.render(writer -> {
            StringLiteralModel name = (StringLiteralModel) argumentModels.get(0);
            writer.renderJsonObjectMemberSelect(expression, classMapping.get(methodName.substring(3)), name.value);
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
