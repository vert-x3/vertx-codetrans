package io.vertx.codetrans.lang.groovy;

import com.sun.source.tree.LambdaExpressionTree;
import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyCodeSource;
import groovy.lang.Script;
import io.vertx.codegen.TypeInfo;
import io.vertx.codetrans.BinaryExpressionModel;
import io.vertx.codetrans.BlockModel;
import io.vertx.codetrans.CodeModel;
import io.vertx.codetrans.CodeWriter;
import io.vertx.codetrans.DataObjectLiteralModel;
import io.vertx.codetrans.ExpressionModel;
import io.vertx.codetrans.Helper;
import io.vertx.codetrans.JsonArrayLiteralModel;
import io.vertx.codetrans.JsonObjectLiteralModel;
import io.vertx.codetrans.LambdaExpressionModel;
import io.vertx.codetrans.Lang;
import io.vertx.codetrans.Member;
import io.vertx.codetrans.StatementModel;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class GroovyLang implements Lang {

  public GroovyLang() {
  }

  @Override
  public io.vertx.codetrans.Script loadScript(ClassLoader loader, String path) throws Exception {
    InputStream in = loader.getResourceAsStream(path + ".groovy");
    if (in != null) {
      String source = new Scanner(in,"UTF-8").useDelimiter("\\A").next();
      GroovyClassLoader compiler = new GroovyClassLoader(loader);
      Class clazz = compiler.parseClass(new GroovyCodeSource(source, path.replace('/', '.'), "/"));
      return new io.vertx.codetrans.Script() {
        @Override
        public String getSource() {
          return source;
        }

        @Override
        public void run(Map<String, Object> globals) throws Exception {
          Script script = (Script) clazz.newInstance();
          script.setBinding(new Binding(globals));
          script.run();
        }
      };
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
    writer.renderChars(value);
    writer.append('L');
  }

  @Override
  public void renderFloatLiteral(String value, CodeWriter writer) {
    writer.renderChars(value);
    writer.append('f');
  }

  @Override
  public void renderDoubleLiteral(String value, CodeWriter writer) {
    writer.renderChars(value);
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
    if (bodyKind == LambdaExpressionTree.BodyKind.EXPRESSION) {
      writer.append("\n");
    }
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
  public ExpressionModel asyncResultHandler(LambdaExpressionTree.BodyKind bodyKind, TypeInfo.Parameterized resultType, String resultName, CodeModel body) {
    return new LambdaExpressionModel(bodyKind, Collections.singletonList(resultType), Collections.singletonList(resultName), body);
  }

  @Override
  public ExpressionModel staticFactory(TypeInfo.Class receiverType, String methodName, TypeInfo returnType, List<TypeInfo> parameterTypes, List<ExpressionModel> arguments, List<TypeInfo> argumentTypes) {
    return ExpressionModel.render(writer -> {
      GroovyRenderer jsRenderer = (GroovyRenderer) writer;
      jsRenderer.imports.add(receiverType);
      writer.append(receiverType.getSimpleName()).append('.').append(methodName);
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
  public StatementModel variableDecl(TypeInfo type, String name, ExpressionModel initializer) {
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
        String name = member.getName().render(writer.getLang());
        if (unquote) {
          name = Helper.unwrapQuotedString(name);
        }
        writer.append(name);
        writer.append(":");
        if (member instanceof Member.Single) {
          ((Member.Single) member).getValue().render(writer);
        } else {
          renderJsonArray(((Member.Array) member).getValues(), writer);
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
  public void renderJsonArrayToString(ExpressionModel expression, CodeWriter writer) {
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

  @Override
  public void renderNew(ExpressionModel expression, TypeInfo type, List<ExpressionModel> argumentModels, CodeWriter writer) {
    writer.append("new ");
    expression.render(writer);
    writer.append('(');
    for (int i = 0; i < argumentModels.size(); i++) {
      if (i > 0) {
        writer.append(", ");
      }
      argumentModels.get(i).render(writer);
    }
    writer.append(')');
  }
}
