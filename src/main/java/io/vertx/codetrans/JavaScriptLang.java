package io.vertx.codetrans;

import com.sun.source.tree.LambdaExpressionTree;
import io.vertx.codegen.Helper;
import io.vertx.codegen.TypeInfo;

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
  public void renderBlock(List<StatementModel> statements, CodeWriter writer) {
    if (writer instanceof JavaScriptRenderer) {
      Lang.super.renderBlock(statements, writer);
    } else {
      JavaScriptRenderer langRenderer = new JavaScriptRenderer(this);
      Lang.super.renderBlock(statements, langRenderer);
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

  public void renderDataObject(DataObjectModel model, CodeWriter writer) {
    renderJsonObject(model.getMembers(), writer, false);
  }

  public void renderJsonObject(JsonObjectModel jsonObject, CodeWriter writer) {
    renderJsonObject(jsonObject.getMembers(), writer, true);
  }

  public void renderJsonArray(JsonArrayModel jsonArray, CodeWriter writer) {
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
  public void renderDataObjectMemberSelect(ExpressionModel expression, ExpressionModel name, CodeWriter writer) {
    renderJsonObjectMemberSelect(expression, name, writer);
  }

  @Override
  public ExpressionModel asyncResultHandler(LambdaExpressionTree.BodyKind bodyKind, String resultName, CodeModel body) {
    return lambda(null, null, Arrays.asList(resultName, resultName + "_err"), body);
  }

  @Override
  public ExpressionModel lambda(LambdaExpressionTree.BodyKind bodyKind, List<TypeInfo> parameterTypes, List<String> parameterNames, CodeModel body) {
    return ExpressionModel.render((renderer) -> {
      renderer.append("function (");
      for (int i = 0; i < parameterNames.size(); i++) {
        if (i > 0) {
          renderer.append(", ");
        }
        renderer.append(parameterNames.get(i));
      }
      renderer.append(") {\n");
      renderer.indent();
      body.render(renderer);
      renderer.unindent();
      renderer.append("}");
    });
  }

  @Override
  public ExpressionModel staticFactory(TypeInfo.Class type, String methodName) {
    return ExpressionModel.render(renderer -> {
      JavaScriptRenderer jsRenderer = (JavaScriptRenderer) renderer;
      jsRenderer.modules.add(type);
      renderer.append(type.getSimpleName()).append('.').append(methodName);
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

  @Override
  public ExpressionModel asyncResult(String identifier) {
    return ExpressionModel.forMemberSelect((member) -> {
      switch (member) {
        case "succeeded":
          return ExpressionModel.forMethodInvocation((args) -> ExpressionModel.render("(" + identifier + " != null)"));
        case "result":
          return ExpressionModel.forMethodInvocation((args) -> ExpressionModel.render(identifier));
        case "cause":
          return ExpressionModel.forMethodInvocation((args) -> ExpressionModel.render(identifier + "_err"));
        case "failed":
          return ExpressionModel.forMethodInvocation((args) -> ExpressionModel.render("(" + identifier + " == null)"));
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
}
