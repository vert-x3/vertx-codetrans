package io.vertx.codetrans;

import com.sun.source.tree.LambdaExpressionTree;
import io.vertx.codegen.TypeInfo;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public interface Lang {

  Callable<?> compile(ClassLoader loader, String path) throws Exception;

  default void renderIfThenElse(ExpressionModel condition, StatementModel thenBody, StatementModel elseBody, CodeWriter writer) {
    writer.append("if ");
    condition.render(writer);
    writer.append(" {\n");
    writer.indent();
    thenBody.render(writer);
    writer.unindent();
    writer.append("}");
    if (elseBody != null) {
      writer.append(" else {\n");
      writer.indent();
      elseBody.render(writer);
      writer.unindent();
      writer.append("}");
    }
  }

  default void renderParenthesized(ExpressionModel expression, CodeWriter writer) {
    writer.append('(');
    expression.render(writer);
    writer.append(')');
  }

  default void renderEquals(ExpressionModel expression, ExpressionModel arg, CodeWriter writer) {
    expression.render(writer);
    writer.append(" == ");
    arg.render(writer);
  }

  default void renderConditionalExpression(ExpressionModel condition, ExpressionModel trueExpression, ExpressionModel falseExpression, CodeWriter writer) {
    condition.render(writer);
    writer.append(" ? ");
    trueExpression.render(writer);
    writer.append(" : ");
    falseExpression.render(writer);
  }

  default void renderAssign(ExpressionModel variable, ExpressionModel expression, CodeWriter writer) {
    variable.render(writer);
    writer.append(" = ");
    expression.render(writer);
  }

  default void renderBlock(List<StatementModel> statements, CodeWriter writer) {
    statements.forEach(statement -> {
      statement.render(writer);
      writer.append(";\n");
    });
  }

  default void renderMemberSelect(ExpressionModel expression, String identifier, CodeWriter writer) {
    expression.render(writer);
    writer.append('.').append(identifier);
  }

  default void renderMethodInvocation(ExpressionModel expression, String methodName, List<ExpressionModel> arguments, CodeWriter writer) {
    expression.render(writer);
    writer.append('.');
    writer.append(methodName);
    writer.append('(');
    for (int i = 0; i < arguments.size(); i++) {
      if (i > 0) {
        writer.append(", ");
      }
      arguments.get(i).render(writer);
    }
    writer.append(')');
  }

  default void renderBinary(ExpressionModel left, String op, ExpressionModel right, CodeWriter writer) {
    left.render(writer);
    writer.append(" ").append(op).append(" ");
    right.render(writer);
  }

  default void renderCharacters(String value, CodeWriter writer) {
    for (int i = 0;i < value.length();i++) {
      char c = value.charAt(i);
      switch (c) {
        case '\n':
          writer.append("\\n");
          break;
        case '"':
          writer.append("\\\"");
          break;
        case '\\':
          writer.append("\\\\");
          break;
        default:
          writer.append(c);
      }
    }
  }

  default void renderNullLiteral(CodeWriter writer) {
    writer.append("null");
  }

  default void renderStringLiteral(String value, CodeWriter writer) {
    writer.append('"');
    renderCharacters(value, writer);
    writer.append('"');
  }

  default void renderCharLiteral(char value, CodeWriter writer) {
    writer.append('\'');
    renderCharacters(Character.toString(value), writer);
    writer.append('\'');
  }

  void renderFloatLiteral(String value, CodeWriter writer);

  void renderDoubleLiteral(String value, CodeWriter writer);

  default void renderBooleanLiteral(String value, CodeWriter writer) {
    writer.append(value);
  }

  void renderLongLiteral(String value, CodeWriter writer);

  default void renderIntegerLiteral(String value, CodeWriter writer) {
    writer.append(value);
  }

  default void renderPostfixIncrement(ExpressionModel expression, CodeWriter writer) {
    expression.render(writer);
    writer.append("++");
  }

  default void renderPrefixIncrement(ExpressionModel expression, CodeWriter writer) {
    writer.append("++");
    expression.render(writer);
  }

  default void renderPostfixDecrement(ExpressionModel expression, CodeWriter writer) {
    expression.render(writer);
    writer.append("--");
  }

  default void renderPrefixDecrement(ExpressionModel expression, CodeWriter writer) {
    writer.append("--");
    expression.render(writer);
  }

  default void renderLogicalComplement(ExpressionModel expression, CodeWriter writer) {
    writer.append("!");
    expression.render(writer);
  }

  default void renderUnaryMinus(ExpressionModel expression, CodeWriter writer) {
    writer.append("-");
    expression.render(writer);
  }

  default void renderUnaryPlus(ExpressionModel expression, CodeWriter writer) {
    writer.append("+");
    expression.render(writer);
  }

  //

  void renderMapGet(ExpressionModel map, ExpressionModel arg, CodeWriter writer);

  void renderMapForEach(ExpressionModel map,
                        String keyName, TypeInfo keyType,
                        String valueName, TypeInfo valueType,
                        LambdaExpressionTree.BodyKind bodyKind, CodeModel block, CodeWriter writer);

  //

  String getExtension();

  void renderJsonObject(JsonObjectLiteralModel jsonObject, CodeWriter writer);

  void renderJsonArray(JsonArrayLiteralModel jsonArray, CodeWriter writer);

  void renderDataObject(DataObjectLiteralModel model, CodeWriter writer);

  void renderJsonObjectAssign(ExpressionModel expression, ExpressionModel name, ExpressionModel value, CodeWriter writer);

  void renderDataObjectAssign(ExpressionModel expression, ExpressionModel name, ExpressionModel value, CodeWriter writer);

  void renderJsonObjectMemberSelect(ExpressionModel expression, ExpressionModel name, CodeWriter writer);

  void renderDataObjectMemberSelect(ExpressionModel expression, ExpressionModel name, CodeWriter writer);

  void renderLambda(LambdaExpressionTree.BodyKind bodyKind, List<TypeInfo> parameterTypes, List<String> parameterNames, CodeModel body, CodeWriter writer);

  //

  default ExpressionModel nullLiteral() {
    return ExpressionModel.render(renderer -> renderer.getLang().renderNullLiteral(renderer));
  }

  default ExpressionModel stringLiteral(String value) {
    return ExpressionModel.render(renderer -> renderer.getLang().renderStringLiteral(value, renderer));
  }

  default ExpressionModel combine(ExpressionModel left, String op, ExpressionModel right) {
    return ExpressionModel.render(renderer -> renderer.getLang().renderBinary(left, op, right, renderer));
  }

  ExpressionModel classExpression(TypeInfo.Class type);

  ExpressionModel asyncResult(String identifier);

  ExpressionModel asyncResultHandler(LambdaExpressionTree.BodyKind bodyKind, String resultName, CodeModel body);

  ExpressionModel staticFactory(TypeInfo.Class type, String methodName, List<ExpressionModel> arguments);

  StatementModel variable(TypeInfo type, String name, ExpressionModel initializer);

  StatementModel enhancedForLoop(String variableName, ExpressionModel expression, StatementModel body);

  StatementModel forLoop(StatementModel initializer, ExpressionModel condition, ExpressionModel update, StatementModel body);

  //

  ExpressionModel console(ExpressionModel expression);

}
