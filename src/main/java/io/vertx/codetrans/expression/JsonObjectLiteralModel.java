package io.vertx.codetrans.expression;

import io.vertx.codegen.type.ClassKind;
import io.vertx.codegen.type.TypeInfo;
import io.vertx.codetrans.CodeBuilder;
import io.vertx.codetrans.CodeWriter;
import io.vertx.codetrans.Helper;
import io.vertx.codetrans.MethodSignature;

import java.util.Collections;
import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class JsonObjectLiteralModel extends ExpressionModel {

  private final List<Member> entries;

  public JsonObjectLiteralModel(CodeBuilder builder) {
    this(builder, Collections.emptyList());
  }

  private JsonObjectLiteralModel(CodeBuilder builder, List<Member> entries) {
    super(builder);
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
  public ExpressionModel onMethodInvocation(TypeInfo receiverType, MethodSignature method, TypeInfo returnType, List<TypeInfo> typeArguments, List<ExpressionModel> argumentModels, List<TypeInfo> argumentTypes) {
    String methodName = method.getName();
    StringLiteralModel name = (StringLiteralModel) argumentModels.get(0);
    switch (methodName) {
      case "put":
        return new JsonObjectLiteralModel(builder, Helper.append(entries, new Member.Single(name.value).append(argumentModels.get(1))));
      case "putNull":
        return new JsonObjectLiteralModel(builder, Helper.append(entries, new Member.Single(name.value).append(new NullLiteralModel(builder))));
      default:
        throw new UnsupportedOperationException("Method " + method + " not yet implemented");
    }
  }

  @Override
  public void render(CodeWriter writer) {
    writer.renderJsonObject(this);
  }
}
