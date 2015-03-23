package io.vertx.codetrans;

import com.sun.source.tree.LambdaExpressionTree;
import io.vertx.codegen.Helper;
import io.vertx.codegen.TypeInfo;
import io.vertx.core.Handler;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
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
public class JavaScriptLang implements Lang {

  @Override
  public void renderLongLiteral(String value, CodeWriter writer) {
    renderCharacters(value, writer);
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
  public void renderBinary(ExpressionModel left, String op, ExpressionModel right, CodeWriter writer) {
    switch (op) {
      case "==":
        op = "===";
        break;
      case "!=":
        op = "!==";
        break;
    }
    Lang.super.renderBinary(left, op, right, writer);
  }

  @Override
  public Callable<?> compile(ClassLoader loader, String path) throws Exception {
    ScriptEngineManager mgr = new ScriptEngineManager();
    ScriptEngine engine = mgr.getEngineByName("nashorn");
    engine.put("__engine", engine);
    InputStream require = getClass().getClassLoader().getResourceAsStream("vertx-js/util/require.js");
    if (require == null) {
      throw new Exception("Not require.js");
    }
    engine.put(ScriptEngine.FILENAME, "require.js");
    engine.eval(new InputStreamReader(require));
    engine.eval("var console = require('vertx-js/util/console')");
    InputStream source = loader.getResourceAsStream(path + ".js");
    if (source == null) {
      throw new Exception("Could not find " + path + ".js");
    }
    return () -> engine.eval(new InputStreamReader(source));
  }

  static class JavaScriptRenderer extends CodeWriter {
    LinkedHashSet<TypeInfo.Class> modules = new LinkedHashSet<>();
    JavaScriptRenderer(Lang lang) {
      super(lang);
    }
  }

  @Override
  public void renderStatement(StatementModel statement, CodeWriter writer) {
    statement.render(writer);
    writer.append(";\n");
  }

  @Override
  public void renderBlock(BlockModel block, CodeWriter writer) {
    if (writer instanceof JavaScriptRenderer) {
      Lang.super.renderBlock(block, writer);
    } else {
      JavaScriptRenderer langRenderer = new JavaScriptRenderer(this);
      Lang.super.renderBlock(block, langRenderer);
      for (TypeInfo.Class module : langRenderer.modules) {
        writer.append("var ").append(module.getSimpleName()).append(" = require(\"").
            append(module.getModuleName()).append("-js/").append(Helper.convertCamelCaseToUnderscores(module.getSimpleName())).append("\");\n");
      }
      writer.append(langRenderer.getBuffer());
    }
  }

  @Override
  public String getExtension() {
    return "js";
  }

  @Override
  public ExpressionModel classExpression(TypeInfo.Class type) {
    return ExpressionModel.render("Java.type(\"" + type.getName() + "\")");
  }

  @Override
  public ExpressionModel console(ExpressionModel expression) {
    return ExpressionModel.render(renderer -> {
      renderer.append("console.log(");
      expression.render(renderer);
      renderer.append(")");
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
    writer.append("{\n");
    writer.indent();
    for (Iterator<Member> iterator = members.iterator();iterator.hasNext();) {
      Member member = iterator.next();
      String name = member.name.render(writer.getLang());
      if (unquote) {
        name = io.vertx.codetrans.Helper.unwrapQuotedString(name);
      }
      writer.append("\"").append(name).append("\" : ");
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
    writer.unindent().append("}");
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
    writer.append("JSON.stringify(");
    expression.render(writer);
    writer.append(")");
  }

  @Override
  public void renderDataObjectMemberSelect(ExpressionModel expression, ExpressionModel name, CodeWriter writer) {
    renderJsonObjectMemberSelect(expression, name, writer);
  }

  @Override
  public ExpressionModel asyncResultHandler(LambdaExpressionTree.BodyKind bodyKind, String resultName, CodeModel body) {
    return ExpressionModel.render(writer -> {
      renderLambda(null, null, Arrays.asList(resultName, resultName + "_err"), body, writer);
    });
  }

  @Override
  public void renderLambda(LambdaExpressionTree.BodyKind bodyKind, List<TypeInfo> parameterTypes, List<String> parameterNames, CodeModel body, CodeWriter writer) {
    writer.append("function (");
    for (int i = 0; i < parameterNames.size(); i++) {
      if (i > 0) {
        writer.append(", ");
      }
      writer.append(parameterNames.get(i));
    }
    writer.append(") {\n");
    writer.indent();
    body.render(writer);
    writer.unindent();
    writer.append("}");
  }

  @Override
  public void renderEnumConstant(TypeInfo.Class.Enum type, String constant, CodeWriter writer) {
    writer.append('\'').append(constant).append('\'');
  }

  @Override
  public void renderThrow(String throwableType, ExpressionModel reason, CodeWriter writer) {
    if (reason == null) {
      writer.append("throw ").append("\"an error occured\"");
    } else {
      writer.append("throw ");
      reason.render(writer);
    }
  }

  @Override
  public ExpressionModel staticFactory(TypeInfo.Class type, String methodName, List<TypeInfo> parameterTypes, List<ExpressionModel> arguments, List<TypeInfo> argumentTypes) {
    return ExpressionModel.render(writer -> {
      JavaScriptRenderer jsRenderer = (JavaScriptRenderer) writer;
      jsRenderer.modules.add(type);
      ExpressionModel expr = ExpressionModel.render(type.getSimpleName());
      renderMethodInvocation(expr, methodName, parameterTypes, arguments, argumentTypes, writer);
    });
  }

  @Override
  public StatementModel variable(TypeInfo type, String name, ExpressionModel initializer) {
    return StatementModel.render(renderer -> {
      renderer.append("var ").append(name);
      if (initializer != null) {
        renderer.append(" = ");
        initializer.render(renderer);
      }
    });
  }

  @Override
  public StatementModel enhancedForLoop(String variableName, ExpressionModel expression, StatementModel body) {
    return StatementModel.render((renderer) -> {
      renderer.append("Array.prototype.forEach.call(");
      expression.render(renderer);
      renderer.append(", function(").append(variableName).append(") {\n");
      renderer.indent();
      body.render(renderer);
      renderer.unindent();
      renderer.append("})");
    });
  }

  @Override
  public StatementModel forLoop(StatementModel initializer, ExpressionModel condition, ExpressionModel update, StatementModel body) {
    return StatementModel.render((renderer) -> {
      renderer.append("for (");
      initializer.render(renderer);
      renderer.append("; ");
      condition.render(renderer);
      renderer.append("; ");
      update.render(renderer);
      renderer.append(") {\n");
      renderer.indent();
      body.render(renderer);
      renderer.unindent();
      renderer.append("}");
    });
  }

  @Override
  public ExpressionModel asyncResult(String identifier) {
    return ExpressionModel.forMethodInvocation((member, args) -> {
      switch (member) {
        case "succeeded":
          return ExpressionModel.render(identifier + "_err == null");
        case "result":
          return ExpressionModel.render(identifier);
        case "cause":
          return ExpressionModel.render(identifier + "_err");
        case "failed":
          return ExpressionModel.render(identifier + "_err != null");
        default:
          throw new UnsupportedOperationException("Not implemented");
      }
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
    writer.append(".forEach(");
    renderLambda(bodyKind, Arrays.asList(valueType, keyType), Arrays.asList(valueName, keyName), block, writer);
    writer.append(")");
  }

  @Override
  public void renderMethodReference(ExpressionModel expression, String methodName, CodeWriter writer) {
    expression.render(writer);
    writer.append('.').append(methodName);
  }

  @Override
  public void renderMethodInvocation(ExpressionModel expression, String methodName, List<TypeInfo> parameterTypes, List<ExpressionModel> argumentModels, List<TypeInfo> argumentTypes, CodeWriter writer) {
    for (int i = 0;i < parameterTypes.size();i++) {
      TypeInfo parameterType = parameterTypes.get(i);
      TypeInfo argumentType = argumentTypes.get(i);
      if (parameterType instanceof TypeInfo.Parameterized && argumentType instanceof TypeInfo.Class.Api) {
        TypeInfo.Class.Api aaa = (TypeInfo.Class.Api) argumentType;
        if (aaa.isHandler()) {
          TypeInfo.Parameterized apiType = (TypeInfo.Parameterized) parameterType;
          if (apiType.getRaw().getName().equals(Handler.class.getName())) {
            ExpressionModel expressionModel = argumentModels.get(i);
            argumentModels.set(i, ExpressionModel.render(cw -> {
              expressionModel.render(cw);
              cw.append(".handle");
            }));
          }
        }
      }
    }
    Lang.super.renderMethodInvocation(expression, methodName, parameterTypes, argumentModels, argumentTypes, writer);
  }
}
