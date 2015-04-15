package io.vertx.codetrans;

import com.sun.source.tree.LambdaExpressionTree;
import io.vertx.codegen.Case;
import io.vertx.codegen.TypeInfo;
import org.jruby.embed.LocalContextScope;
import org.jruby.embed.ScriptingContainer;

import java.io.InputStream;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class RubyLang implements Lang {

  @Override
  public Script loadScript(ClassLoader loader, String path) throws Exception {
    String filename = path + ".rb";
    InputStream in = loader.getResourceAsStream(filename);
    if (in == null) {
      throw new Exception("Could not find " + filename);
    }
    String source = new Scanner(in,"UTF-8").useDelimiter("\\A").next();
    ScriptingContainer container = new ScriptingContainer(LocalContextScope.SINGLETHREAD);
    return new Script() {
      @Override
      public String getSource() {
        return source;
      }
      @Override
      public Void call() throws Exception {
        container.runScriptlet(source);
        return null;
      }
    };
  }

  @Override
  public void renderPrefixIncrement(ExpressionModel expression, CodeWriter writer) {
    expression.render(writer);
    writer.append("+=1");
  }

  @Override
  public void renderPostfixIncrement(ExpressionModel expression, CodeWriter writer) {
    expression.render(writer);
    writer.append("+=1");
  }

  @Override
  public void renderPostfixDecrement(ExpressionModel expression, CodeWriter writer) {
    expression.render(writer);
    writer.append("-=1");
  }

  @Override
  public void renderPrefixDecrement(ExpressionModel expression, CodeWriter writer) {
    expression.render(writer);
    writer.append("-=1");
  }

  @Override
  public void renderNullLiteral(CodeWriter writer) {
    writer.append("nil");
  }

  @Override
  public void renderBinary(BinaryExpressionModel expression, CodeWriter writer) {
    if (Helper.isString(expression)) {
      Helper.renderInterpolatedString(expression, writer, "#{", "}");
    } else {
      Lang.super.renderBinary(expression, writer);
    }
  }

  @Override
  public void renderStatement(StatementModel statement, CodeWriter writer) {
    statement.render(writer);
    writer.append("\n");
  }

  @Override
  public void renderMethodReference(ExpressionModel expression, String methodName, CodeWriter writer) {
    throw new UnsupportedOperationException("todo");
  }

  @Override
  public void renderFloatLiteral(String value, CodeWriter writer) {
    renderCharacters(value, writer);
  }

  @Override
  public void renderDoubleLiteral(String value, CodeWriter writer) {
    renderCharacters(value, writer);
  }

  @Override
  public void renderLongLiteral(String value, CodeWriter writer) {
    renderCharacters(value, writer);
  }

  @Override
  public void renderMapGet(ExpressionModel map, ExpressionModel arg, CodeWriter writer) {
    throw new UnsupportedOperationException("todo");
  }

  @Override
  public void renderMapForEach(ExpressionModel map, String keyName, TypeInfo keyType, String valueName, TypeInfo valueType, LambdaExpressionTree.BodyKind bodyKind, CodeModel block, CodeWriter writer) {
    throw new UnsupportedOperationException("todo");
  }

  @Override
  public String getExtension() {
    return "rb";
  }

  @Override
  public void renderJsonObject(JsonObjectLiteralModel jsonObject, CodeWriter writer) {
    throw new UnsupportedOperationException("todo");
  }

  @Override
  public void renderJsonArray(JsonArrayLiteralModel jsonArray, CodeWriter writer) {
    throw new UnsupportedOperationException("todo");
  }

  @Override
  public void renderDataObject(DataObjectLiteralModel model, CodeWriter writer) {
    throw new UnsupportedOperationException("todo");
  }

  @Override
  public void renderJsonObjectAssign(ExpressionModel expression, ExpressionModel name, ExpressionModel value, CodeWriter writer) {
    throw new UnsupportedOperationException("todo");
  }

  @Override
  public void renderDataObjectAssign(ExpressionModel expression, ExpressionModel name, ExpressionModel value, CodeWriter writer) {
    throw new UnsupportedOperationException("todo");
  }

  @Override
  public void renderJsonObjectToString(ExpressionModel expression, CodeWriter writer) {
    throw new UnsupportedOperationException("todo");
  }

  @Override
  public void renderJsonObjectMemberSelect(ExpressionModel expression, ExpressionModel name, CodeWriter writer) {
    throw new UnsupportedOperationException("todo");
  }

  @Override
  public void renderDataObjectMemberSelect(ExpressionModel expression, ExpressionModel name, CodeWriter writer) {
    throw new UnsupportedOperationException("todo");
  }

  @Override
  public void renderLambda(LambdaExpressionTree.BodyKind bodyKind, List<TypeInfo> parameterTypes, List<String> parameterNames, CodeModel body, CodeWriter writer) {
    throw new UnsupportedOperationException("todo");
  }

  @Override
  public void renderEnumConstant(TypeInfo.Class.Enum type, String constant, CodeWriter writer) {
    throw new UnsupportedOperationException("todo");
  }

  @Override
  public void renderThrow(String throwableType, ExpressionModel reason, CodeWriter writer) {
    throw new UnsupportedOperationException("todo");
  }

  @Override
  public ExpressionModel classExpression(TypeInfo.Class type) {
    return ExpressionModel.render(
        "Java::" + Case.CAMEL.format(Case.QUALIFIED.parse(type.getPackageName())) + "::" + type.getSimpleName()
    );
  }

  @Override
  public ExpressionModel asyncResult(String identifier) {
    throw new UnsupportedOperationException("todo");
  }

  @Override
  public ExpressionModel asyncResultHandler(LambdaExpressionTree.BodyKind bodyKind, String resultName, CodeModel body) {
    throw new UnsupportedOperationException("todo");
  }

  @Override
  public ExpressionModel staticFactory(TypeInfo.Class type, String methodName, List<TypeInfo> parameterTypes, List<ExpressionModel> arguments, List<TypeInfo> argumentTypes) {
    throw new UnsupportedOperationException("todo");
  }

  @Override
  public StatementModel variable(TypeInfo type, String name, ExpressionModel initializer) {
    return StatementModel.render(renderer -> {
      renderer.append(name);
      if (initializer != null) {
        renderer.append(" = ");
        initializer.render(renderer);
      }
    });
  }

  @Override
  public StatementModel enhancedForLoop(String variableName, ExpressionModel expression, StatementModel body) {
    throw new UnsupportedOperationException("todo");
  }

  @Override
  public StatementModel forLoop(StatementModel initializer, ExpressionModel condition, ExpressionModel update, StatementModel body) {
    throw new UnsupportedOperationException("todo");
  }

  @Override
  public ExpressionModel console(ExpressionModel expression) {
    throw new UnsupportedOperationException("todo");
  }
}
