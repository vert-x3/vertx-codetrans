package io.vertx.codetrans;

import io.vertx.codegen.ClassKind;
import io.vertx.codegen.TypeInfo;

import java.util.Collections;
import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class JsonObjectLiteralModel extends ExpressionModel {

  private final List<Member> entries;

  public JsonObjectLiteralModel() {
    this(Collections.emptyList());
  }

  private JsonObjectLiteralModel(List<Member> entries) {
    this.entries = entries;
  }

  public Iterable<Member> getMembers() {
    return entries;
  }

  @Override
  public ExpressionModel as(TypeInfo type) {
    if (type.getKind() != ClassKind.JSON_OBJECT) {
      throw new UnsupportedOperationException();
    }
    return this;
  }

  @Override
  public ExpressionModel onMethodInvocation(TypeInfo receiverType, MethodRef method, TypeInfo returnType, List<ExpressionModel> argumentModels, List<TypeInfo> argumenTypes) {
    String methodName = method.getName();
    switch (methodName) {
      case "put":
        return new JsonObjectLiteralModel(Helper.append(entries, new Member.Single(argumentModels.get(0)).append(argumentModels.get(1))));
      default:
        throw new UnsupportedOperationException("Method " + method + " not yet implemented");
    }
  }

  @Override
  public void render(CodeWriter writer) {
    writer.renderJsonObject(this);
  }
}
