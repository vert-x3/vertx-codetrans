package io.vertx.codetrans.lang.scala;

import io.vertx.codegen.type.ClassKind;
import io.vertx.codegen.type.ParameterizedTypeInfo;
import io.vertx.codegen.type.TypeInfo;
import io.vertx.codetrans.CodeBuilder;
import io.vertx.codetrans.CodeWriter;
import io.vertx.codetrans.MethodSignature;
import io.vertx.codetrans.expression.ApiModel;
import io.vertx.codetrans.expression.ExpressionModel;
import io.vertx.codetrans.expression.MethodInvocationModel;

import java.util.Arrays;
import java.util.List;

public class ScalaApiModel extends ApiModel {
  private ExpressionModel expression;

  public ScalaApiModel(CodeBuilder builder, ExpressionModel expression) {
    super(builder, expression);
    this.expression = expression;
  }

  @Override
  public ExpressionModel onMethodInvocation(TypeInfo receiverType, MethodSignature method, TypeInfo returnType, List<ExpressionModel> argumentModels, List<TypeInfo> argumentTypes) {
    if (argumentTypes.size() > 0) {
      TypeInfo last = argumentTypes.get(argumentTypes.size() - 1);
      if (last.getKind() == ClassKind.HANDLER && ((ParameterizedTypeInfo) last).getArg(0).getKind() == ClassKind.ASYNC_RESULT) {
        // Return an ExpressionModel that the composition of two MethodInvocationModel that
        // the first one calls the method that returns the future
        // the second one does the onComplete call

        int lastIndex = (method.getParameterTypes().size() - 1 < 0) ? 0 : method.getParameterTypes().size();
        MethodSignature futureMethodSignature = new MethodSignature(method.getName() + "Future", method.getParameterTypes().subList(0, lastIndex), false);
        List<ExpressionModel> futureArgumentModels = argumentModels.subList(0, (argumentModels.size() - 1 < 0) ? 0 : argumentModels.size() - 1);
        List<TypeInfo> futureArgumentTypes = argumentTypes.subList(0, (argumentTypes.size() - 1 < 0) ? 0 : argumentTypes.size() - 1);

        MethodInvocationModel futureModel = new MethodInvocationModel(builder, expression, receiverType, futureMethodSignature, returnType, futureArgumentModels, futureArgumentTypes);

        MethodSignature handlerMethodSignature = new MethodSignature("onComplete", Arrays.asList(method.getParameterTypes().get(method.getParameterTypes().size() - 1)), false);
        MethodInvocationModel completeMethod = new MethodInvocationModel(builder, expression, receiverType, handlerMethodSignature, returnType, Arrays.asList(argumentModels.get(argumentModels.size() - 1)), Arrays.asList(argumentTypes.get(argumentTypes.size() - 1)));


        return new ExpressionModel(builder) {
          @Override
          public void render(CodeWriter writer) {
            futureModel.render(writer);
            completeMethod.render(writer);
          }
        };
      }
    }


    return super.onMethodInvocation(receiverType, method, returnType, argumentModels, argumentTypes);
  }
}
