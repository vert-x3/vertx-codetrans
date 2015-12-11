package io.vertx.codetrans.expression;

import io.vertx.codegen.type.ClassTypeInfo;
import io.vertx.codetrans.CodeBuilder;

import java.util.List;

/**
* @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
*/
public class ThrowableClassModel extends ClassModel {

  private final ClassTypeInfo type;

  public ThrowableClassModel(CodeBuilder builder, ClassTypeInfo type) {
    super(builder);
    this.type = type;
  }

  @Override
  public ExpressionModel onNew(List<ExpressionModel> arguments) {
    if (arguments.size() == 0) {
      return new ThrowableModel(builder, type.getName(), null);
    } else if (arguments.size() == 1) {
      return new ThrowableModel(builder, type.getName(), arguments.get(0));
    }
    throw new UnsupportedOperationException("Only empty or String throwable constructor are accepted");
  }
}
