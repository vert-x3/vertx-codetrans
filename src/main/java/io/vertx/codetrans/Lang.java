package io.vertx.codetrans;

import com.sun.source.tree.LambdaExpressionTree;
import io.vertx.codegen.TypeInfo;

import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public interface Lang {

  Script loadScript(ClassLoader loader, String path) throws Exception;

  default void renderConditionals(List<ConditionalBlockModel> conditionals, StatementModel otherwise, CodeWriter writer) {
    for (int i = 0;i < conditionals.size();i++) {
      ConditionalBlockModel conditional = conditionals.get(i);
      writer.append(i == 0 ? "if " : " else if ");
      conditional.condition.render(writer);
      writer.append(" {\n");
      writer.indent();
      conditional.body.render(writer);
      writer.unindent();
      writer.append("}");
    }
    if (otherwise != null) {
      writer.append(" else {\n");
      writer.indent();
      otherwise.render(writer);
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

  void renderStatement(StatementModel statement, CodeWriter writer);

  default void renderBlock(BlockModel block, CodeWriter writer) {
    block.render(writer);
  }

  default void renderMemberSelect(ExpressionModel expression, String identifier, CodeWriter writer) {
    expression.render(writer);
    writer.append('.').append(identifier);
  }

  void renderMethodReference(ExpressionModel expression, String methodName, CodeWriter writer);

  void renderNew(ExpressionModel expression, TypeInfo type, List<ExpressionModel> argumentModels, CodeWriter writer);

  default void renderMethodInvocation(ExpressionModel expression,
                                      TypeInfo receiverType,
                                      String methodName,
                                      TypeInfo returnType,
                                      List<TypeInfo> parameterTypes,
                                      List<ExpressionModel> argumentModels,
                                      List<TypeInfo> argumentTypes,
                                      CodeWriter writer) {
    expression.render(writer);
    writer.append('.');
    writer.append(methodName);
    writer.append('(');
    for (int i = 0; i < argumentModels.size(); i++) {
      if (i > 0) {
        writer.append(", ");
      }
      argumentModels.get(i).render(writer);
    }
    writer.append(')');
  }

  default void renderBinary(BinaryExpressionModel expression, CodeWriter writer) {
    expression.left.render(writer);
    writer.append(" ").append(expression.op).append(" ");
    expression.right.render(writer);
  }

  default void renderNullLiteral(CodeWriter writer) {
    writer.append("null");
  }

  default void renderStringLiteral(String value, CodeWriter writer) {
    writer.append('"');
    writer.renderChars(value);
    writer.append('"');
  }

  default void renderCharLiteral(char value, CodeWriter writer) {
    writer.append('\'');
    writer.renderChars(Character.toString(value));
    writer.append('\'');
  }

  default void renderFloatLiteral(String value, CodeWriter writer) {
    writer.renderChars(value);
  }

  default void renderDoubleLiteral(String value, CodeWriter writer) {
    writer.renderChars(value);
  }

  default void renderBooleanLiteral(String value, CodeWriter writer) {
    writer.append(value);
  }

  default void renderLongLiteral(String value, CodeWriter writer) {
    writer.renderChars(value);
  }

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

  void renderJsonObjectToString(ExpressionModel expression, CodeWriter writer);

  void renderJsonArrayToString(ExpressionModel expression, CodeWriter writer);

  void renderJsonObjectMemberSelect(ExpressionModel expression, ExpressionModel name, CodeWriter writer);

  void renderDataObjectMemberSelect(ExpressionModel expression, ExpressionModel name, CodeWriter writer);

  default void renderJsonArrayGet(ExpressionModel expression, ExpressionModel index, CodeWriter writer) {
    expression.render(writer);
    writer.append('[');
    index.render(writer);
    writer.append(']');
  }

  void renderLambda(LambdaExpressionTree.BodyKind bodyKind, List<TypeInfo> parameterTypes, List<String> parameterNames, CodeModel body, CodeWriter writer);

  void renderEnumConstant(TypeInfo.Class.Enum type, String constant, CodeWriter writer);

  void renderThrow(String throwableType, ExpressionModel reason, CodeWriter writer);

  default void renderFragment(String fragment, CodeWriter writer) {
    FragmentParser renderer = new FragmentParser() {
      @Override
      public void onNewline() {
        writer.append('\n');
      }
      @Override
      public void onComment(char c) {
        writer.append(c);
      }
      @Override
      public void onBeginComment(boolean multiline) {
        writer.append(multiline ? "/*" : "//");
      }
      @Override
      public void onEndComment(boolean multiline) {
        if (multiline) {
          writer.append("*/");
        }
      }
    };
    renderer.parse(fragment);
  }

  //

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

  ExpressionModel staticFactory(TypeInfo.Class receiverType, String methodName, TypeInfo returnType, List<TypeInfo> parameterTypes, List<ExpressionModel> arguments, List<TypeInfo> argumentTypes);

  default ExpressionModel variable(TypeInfo type, boolean local, String name) {
    return ExpressionModel.render(name).as(type);
  }

  StatementModel variableDecl(TypeInfo type, String name, ExpressionModel initializer);

  StatementModel enhancedForLoop(String variableName, ExpressionModel expression, StatementModel body);

  StatementModel forLoop(StatementModel initializer, ExpressionModel condition, ExpressionModel update, StatementModel body);

  //

  ExpressionModel console(ExpressionModel expression);

}
