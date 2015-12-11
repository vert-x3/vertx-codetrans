package io.vertx.codetrans.expression;

import io.vertx.codegen.type.ClassTypeInfo;
import io.vertx.codetrans.CodeBuilder;

import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class DataObjectClassModel extends ClassModel {

  final ClassTypeInfo type;

  public DataObjectClassModel(CodeBuilder builder, ClassTypeInfo type) {
    super(builder);
    this.type = type;
  }

  @Override
  public ExpressionModel onNew(List<ExpressionModel> arguments) {
    switch (arguments.size()) {
      case 0:
        return new DataObjectLiteralModel(builder, this.type);
      default:
        throw new UnsupportedOperationException();
    }
  }
}
