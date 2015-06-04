package io.vertx.codetrans;

import io.vertx.codegen.ClassKind;
import io.vertx.codegen.TypeInfo;

import java.util.Collections;
import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class JsonArrayLiteralModel extends ExpressionModel {

  private List<ExpressionModel> values;

  public JsonArrayLiteralModel() {
    this(Collections.emptyList());
  }

  private JsonArrayLiteralModel(List<ExpressionModel> values) {
    this.values = values;
  }

  public List<ExpressionModel> getValues() {
    return values;
  }

  @Override
  public ExpressionModel as(TypeInfo type) {
    if (type.getKind() != ClassKind.JSON_ARRAY) {
      throw new UnsupportedOperationException();
    }
    return this;
  }

  @Override
  public ExpressionModel onMethodInvocation(TypeInfo receiverType, MethodRef method, TypeInfo returnType, List<ExpressionModel> argumentModels, List<TypeInfo> argumenTypes) {
    String methodName = method.getName();
    switch (methodName) {
      case "add":
        return new JsonArrayLiteralModel(Helper.append(values, argumentModels.get(0)));
      default:
        throw new UnsupportedOperationException("Method " + method + " not yet implemented");
    }
  }

  @Override
  public void render(CodeWriter writer) {
    writer.renderJsonArray(this);
  }
}
