package io.vertx.codetrans;

import com.sun.source.tree.LambdaExpressionTree;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyCodeSource;
import groovy.lang.Script;
import io.vertx.codegen.TypeInfo;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class GroovyLang implements Lang {

  public GroovyLang() {
  }

  @Override
  public Callable<?> compile(ClassLoader loader, String path) throws Exception {
    InputStream resource = loader.getResourceAsStream(path + ".groovy");
    if (resource != null) {
      try (InputStreamReader reader = new InputStreamReader(resource)) {
        GroovyClassLoader compiler = new GroovyClassLoader(loader);
        Class clazz = compiler.parseClass(new GroovyCodeSource(reader, path.replace('/', '.'), "/"));
        return () -> {
          Script script = (Script) clazz.newInstance();
          return script.run();
        };
      }
    }
    throw new Exception("Could not compile " + path);
  }

  static class GroovyRenderer extends CodeWriter {
    LinkedHashSet<TypeInfo.Class> imports = new LinkedHashSet<>();
    GroovyRenderer(Lang lang) {
      super(lang);
    }
  }

  @Override
  public void renderBinary(BinaryExpressionModel expression, CodeWriter writer) {
    if (Helper.isString(expression)) {
      Helper.renderInterpolatedString(expression, writer, "${", "}");
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
  public void renderBlock(BlockModel block, CodeWriter writer) {
    if (writer instanceof GroovyRenderer) {
      Lang.super.renderBlock(block, writer);
    } else {
      GroovyRenderer langRenderer = new GroovyRenderer(this);
      Lang.super.renderBlock(block, langRenderer);
      for (TypeInfo.Class importedType : langRenderer.imports) {
        String fqn = importedType.getName();
        if (importedType instanceof TypeInfo.Class.Api) {
          fqn = importedType.getName().replace("io.vertx.", "io.vertx.groovy.");
        }
        writer.append("import ").append(fqn).append('\n');
      }
      writer.append(langRenderer.getBuffer());
    }
  }

  @Override
  public String getExtension() {
    return "groovy";
  }

  @Override
  public void renderLongLiteral(String value, CodeWriter writer) {
    renderCharacters(value, writer);
    writer.append('L');
  }

  @Override
  public void renderFloatLiteral(String value, CodeWriter writer) {
    renderCharacters(value, writer);
    writer.append('f');
  }

  @Override
  public void renderDoubleLiteral(String value, CodeWriter writer) {
    renderCharacters(value, writer);
    writer.append('d');
  }

  @Override
  public ExpressionModel classExpression(TypeInfo.Class type) {
    return ExpressionModel.render(type.getName());
  }

  @Override
  public void renderLambda(LambdaExpressionTree.BodyKind bodyKind, List<TypeInfo> parameterTypes, List<String> parameterNames, CodeModel body, CodeWriter writer) {
    writer.append("{");
    for (int i = 0; i < parameterNames.size(); i++) {
      if (i == 0) {
        writer.append(" ");
      } else {
        writer.append(", ");
      }
      writer.append(parameterNames.get(i));
    }
    writer.append(" ->\n");
    writer.indent();
    body.render(writer);
    writer.unindent();
    writer.append("}");
  }

  @Override
  public void renderEnumConstant(TypeInfo.Class.Enum type, String constant, CodeWriter writer) {
    GroovyRenderer jsRenderer = (GroovyRenderer) writer;
    jsRenderer.imports.add(type);
    writer.append(type.getSimpleName()).append('.').append(constant);
  }

  @Override
  public void renderThrow(String throwableType, ExpressionModel reason, CodeWriter writer) {
    if (reason == null) {
      writer.append("throw new ").append(throwableType).append("()");
    } else {
      writer.append("throw new ").append(throwableType).append("(");
      reason.render(writer);
      writer.append(")");
    }
  }

  @Override
  public ExpressionModel asyncResult(String identifier) {
    return ExpressionModel.render(renderer -> renderer.append(identifier));
  }

  @Override
  public ExpressionModel asyncResultHandler(LambdaExpressionTree.BodyKind bodyKind, String resultName, CodeModel body) {
    return ExpressionModel.render(writer -> {
      renderLambda(null, null, Arrays.asList(resultName), body, writer);
    });
  }

  @Override
  public ExpressionModel staticFactory(TypeInfo.Class type, String methodName, List<TypeInfo> parameterTypes, List<ExpressionModel> arguments, List<TypeInfo> argumentTypes) {
    return ExpressionModel.render(writer -> {
      GroovyRenderer jsRenderer = (GroovyRenderer) writer;
      jsRenderer.imports.add(type);
      writer.append(type.getSimpleName()).append('.').append(methodName);
      writer.append('(');
      for (int i = 0;i < arguments.size();i++) {
        ExpressionModel argument = arguments.get(i);
        if (i > 0) {
          writer.append(", ");
        }
        argument.render(writer);
      }
      writer.append(')');
    });
  }

  @Override
  public StatementModel variable(TypeInfo type, String name, ExpressionModel initializer) {
    return StatementModel.render(renderer -> {
      renderer.append("def ").append(name);
      if (initializer != null) {
        renderer.append(" = ");
        initializer.render(renderer);
      }
    });
  }

  @Override
  public StatementModel enhancedForLoop(String variableName, ExpressionModel expression, StatementModel body) {
    return StatementModel.render(renderer -> {
      expression.render(renderer);
      renderer.append(".each { ").append(variableName).append(" ->\n");
      renderer.indent();
      body.render(renderer);
      renderer.unindent();
      renderer.append("}");
    });
  }

  @Override
  public StatementModel forLoop(StatementModel initializer, ExpressionModel condition, ExpressionModel update, StatementModel body) {
    return StatementModel.render(renderer -> {
      renderer.append("for (");
      initializer.render(renderer);
      renderer.append(';');
      condition.render(renderer);
      renderer.append(';');
      update.render(renderer);
      renderer.append(") {\n");
      renderer.indent();
      body.render(renderer);
      renderer.unindent();
      renderer.append("}");
    });
  }

  public void renderDataObject(DataObjectLiteralModel model, CodeWriter writer) {
    renderJsonObject(model.getMembers(), writer, false);
  }

  public void renderJsonObject(JsonObjectLiteralModel jsonObject, CodeWriter writer) {
    renderJsonObject(jsonObject.getMembers(), writer, true);
  }

  public void renderJsonArray(JsonArrayLiteralModel jsonArray, CodeWriter writer) {
    renderJsonArray(jsonArray.getValues(), writer);
  }

  private void renderJsonObject(Iterable<Member> members, CodeWriter writer, boolean unquote) {
    Iterator<Member> iterator = members.iterator();
    if (iterator.hasNext()) {
      writer.append("[\n").indent();
      while (iterator.hasNext()) {
        Member member = iterator.next();
        String name = member.name.render(writer.getLang());
        if (unquote) {
          name = Helper.unwrapQuotedString(name);
        }
        writer.append(name);
        writer.append(":");
        if (member instanceof Member.Single) {
          ((Member.Single) member).value.render(writer);
        } else {
          renderJsonArray(((Member.Array) member).values, writer);
        }
        if (iterator.hasNext()) {
          writer.append(',');
        }
        writer.append('\n');
      }
      writer.unindent().append("]");
    } else {
      writer.append("[:]");
    }
  }

  private void renderJsonArray(List<ExpressionModel> values, CodeWriter writer) {
    writer.append("[\n").indent();
    for (int i = 0;i < values.size();i++) {
      values.get(i).render(writer);
      if (i < values.size() - 1) {
        writer.append(',');
      }
      writer.append('\n');
    }
    writer.unindent().append(']');
  }

  @Override
  public void renderJsonObjectAssign(ExpressionModel expression, ExpressionModel name, ExpressionModel value, CodeWriter writer) {
    expression.render(writer);
    writer.append('.');
    name.render(writer);
    writer.append(" = ");
    value.render(writer);
  }

  @Override
  public void renderDataObjectAssign(ExpressionModel expression, ExpressionModel name, ExpressionModel value, CodeWriter writer) {
    renderJsonObjectAssign(expression, name, value, writer);
  }

  @Override
  public void renderJsonObjectMemberSelect(ExpressionModel expression, ExpressionModel name, CodeWriter writer) {
    expression.render(writer);
    writer.append('.');
    name.render(writer);
  }

  @Override
  public void renderJsonObjectToString(ExpressionModel expression, CodeWriter writer) {
    expression.render(writer);
    writer.append(".toString()");
  }

  @Override
  public void renderDataObjectMemberSelect(ExpressionModel expression, ExpressionModel name, CodeWriter writer) {
    renderJsonObjectMemberSelect(expression, name, writer);
  }

  @Override
  public ExpressionModel console(ExpressionModel expression) {
    return ExpressionModel.render(renderer -> {
      renderer.append("println(");
      expression.render(renderer);
      renderer.append(")");
    });
  }

  @Override
  public void renderMapGet(ExpressionModel map, ExpressionModel arg, CodeWriter writer) {
    map.render(writer);
    writer.append('[');
    arg.render(writer);
    writer.append(']');
  }

  @Override
  public void renderMapForEach(ExpressionModel map, String keyName, TypeInfo keyType, String valueName, TypeInfo valueType, LambdaExpressionTree.BodyKind bodyKind, CodeModel block, CodeWriter writer) {
    map.render(writer);
    writer.append(".each ");
    renderLambda(bodyKind, Arrays.asList(keyType, valueType), Arrays.asList(keyName, valueName), block, writer);
  }

  @Override
  public void renderMethodReference(ExpressionModel expression, String methodName, CodeWriter writer) {
    expression.render(writer);
    writer.append(".&").append(methodName);
  }
}
