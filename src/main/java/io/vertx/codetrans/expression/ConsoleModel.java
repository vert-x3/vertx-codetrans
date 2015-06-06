package io.vertx.codetrans.expression;

import io.vertx.codegen.TypeInfo;
import io.vertx.codetrans.CodeBuilder;
import io.vertx.codetrans.MethodSignature;

import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ConsoleModel extends ExpressionModel {

  public ConsoleModel(CodeBuilder builder) {
    super(builder);
  }

  @Override
  public ExpressionModel onMethodInvocation(TypeInfo receiverType, MethodSignature method, TypeInfo returnType, List<ExpressionModel> argumentModels, List<TypeInfo> argumenTypes) {
    if (method.getName().equals("println") && method.getParameterTypes().size() == 1) {
      return builder.console(argumentModels.get(0));
    }
    throw new UnsupportedOperationException("Cannot invoke method " + method + " on System.out");
  }
}
