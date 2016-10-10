package io.vertx.codetrans.expression;

import io.vertx.codegen.type.TypeInfo;
import io.vertx.codetrans.CodeBuilder;
import io.vertx.codetrans.CodeWriter;
import io.vertx.codetrans.MethodSignature;

import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ParenthesizedModel extends ExpressionModel {
  
  final ExpressionModel expression;

  public ParenthesizedModel(CodeBuilder builder, ExpressionModel expression) {
    super(builder);
    this.expression = expression;
  }

  @Override
  public ExpressionModel as(TypeInfo type) {
    return expression.as(type);
  }

  @Override
  public ExpressionModel onMethodInvocation(TypeInfo receiverType, MethodSignature method, TypeInfo returnType, List<ExpressionModel> argumentModels, List<TypeInfo> argumenTypes) {
    return expression.onMethodInvocation(receiverType, method, returnType, argumentModels, argumenTypes);
  }

  @Override
  public ExpressionModel onField(String identifier) {
    return expression.onField(identifier);
  }

  @Override
  public ExpressionModel onMethodReference(MethodSignature signature) {
    return expression.onMethodReference(signature);
  }

  @Override
  public ExpressionModel onPostFixIncrement() {
    return expression.onPostFixIncrement();
  }

  @Override
  public ExpressionModel onPrefixIncrement() {
    return expression.onPrefixIncrement();
  }

  @Override
  public ExpressionModel onPostFixDecrement() {
    return expression.onPostFixDecrement();
  }

  @Override
  public ExpressionModel onPrefixDecrement() {
    return expression.onPrefixDecrement();
  }

  @Override
  public ExpressionModel onLogicalComplement() {
    return expression.onLogicalComplement();
  }

  @Override
  public ExpressionModel unaryMinus() {
    return expression.unaryMinus();
  }

  @Override
  public ExpressionModel unaryPlus() {
    return expression.unaryPlus();
  }

  @Override
  public void render(CodeWriter writer) {
    writer.renderParenthesized(expression);
  }
}
