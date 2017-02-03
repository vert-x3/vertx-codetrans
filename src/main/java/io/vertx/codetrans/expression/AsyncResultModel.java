package io.vertx.codetrans.expression;

import io.vertx.codegen.type.TypeInfo;
import io.vertx.codetrans.CodeBuilder;
import io.vertx.codetrans.CodeWriter;
import io.vertx.codetrans.MethodSignature;

import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class AsyncResultModel extends ExpressionModel {

  final String name;
  final TypeInfo type;

  public AsyncResultModel(CodeBuilder builder, String name, TypeInfo type) {
    super(builder);
    this.name = name;
    this.type = type;
  }

  @Override
  public ExpressionModel onMethodInvocation(TypeInfo receiverType, MethodSignature method, TypeInfo returnType, List<TypeInfo> typeArguments, List<ExpressionModel> argumentModels, List<TypeInfo> argumentTypes) {
    switch (method.getName()) {
      case "succeeded":
        return new Succeeded(builder);
      case "failed":
        return new Failed(builder);
      case "cause":
        return new Cause(builder); // Need to cast to throwable with "as"
      case "result":
        return new Value(builder).as(type);
      default:
        throw new UnsupportedOperationException();
    }
  }

  @Override
  public void render(CodeWriter writer) {
    throw new UnsupportedOperationException("It is not be possible to render an async result directly");
  }

  public class Succeeded extends ExpressionModel {
    public Succeeded(CodeBuilder builder) {
      super(builder);
    }
    @Override
    public void render(CodeWriter writer) {
      writer.renderAsyncResultSucceeded(type, name);
    }
  }

  public class Failed extends ExpressionModel {
    public Failed(CodeBuilder builder) {
      super(builder);
    }
    @Override
    public void render(CodeWriter writer) {
      writer.renderAsyncResultFailed(type, name);
    }
  }

  public class Value extends ExpressionModel {
    public Value(CodeBuilder builder) {
      super(builder);
    }
    @Override
    public void render(CodeWriter writer) {
      writer.renderAsyncResultValue(type, name);
    }
  }

  public class Cause extends ExpressionModel {
    public Cause(CodeBuilder builder) {
      super(builder);
    }
    @Override
    public void render(CodeWriter writer) {
      writer.renderAsyncResultCause(type, name);
    }
  }
}
