package io.vertx.codetrans;

import com.sun.source.tree.LambdaExpressionTree;
import io.vertx.codegen.TypeInfo;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public interface Lang {

  Script loadScript(ClassLoader loader, String path) throws Exception;

  String getExtension();

  CodeWriter newWriter();

  default ExpressionModel nullLiteral() {
    return new LiteralModel.Null();
  }

  default ExpressionModel stringLiteral(String value) {
    return new LiteralModel.String(value);
  }

  default ExpressionModel booleanLiteral(String value) {
    return new LiteralModel.Boolean(value);
  }

  default ExpressionModel integerLiteral(String value) {
    return new LiteralModel.Integer(value);
  }

  default ExpressionModel longLiteral(String value) {
    return new LiteralModel.Long(value);
  }

  default ExpressionModel characterLiteral(char value) {
    return new LiteralModel.Character(value);
  }

  default ExpressionModel floatLiteral(String value) {
    return new LiteralModel.Float(value);
  }

  default ExpressionModel doubleLiteral(String value) {
    return new LiteralModel.Double(value);
  }

  default ExpressionModel combine(ExpressionModel left, String op, ExpressionModel right) {
    return new BinaryExpressionModel(left, op, right);
  }

  ExpressionModel classExpression(TypeInfo.Class type);

  ExpressionModel asyncResult(String identifier);

  ExpressionModel asyncResultHandler(LambdaExpressionTree.BodyKind bodyKind, TypeInfo.Parameterized resultType, String resultName, CodeModel body);

  default ApiTypeModel apiType(TypeInfo.Class.Api type) {
    return new ApiTypeModel(type);
  }

  default EnumExpressionModel enumType(TypeInfo.Class.Enum type) {
    return new EnumExpressionModel(type);
  }

  default ExpressionModel variable(TypeInfo type, boolean local, String name) {
    return ExpressionModel.render(name).as(type);
  }

  StatementModel variableDecl(TypeInfo type, String name, ExpressionModel initializer);

  StatementModel enhancedForLoop(String variableName, ExpressionModel expression, StatementModel body);

  StatementModel forLoop(StatementModel initializer, ExpressionModel condition, ExpressionModel update, StatementModel body);


  //

  ExpressionModel console(ExpressionModel expression);

  default ExpressionModel thisModel() {
    return new ThisModel();
  }

}
