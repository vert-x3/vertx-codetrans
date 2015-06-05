package io.vertx.codetrans.lang.js;

import com.sun.source.tree.LambdaExpressionTree;
import io.vertx.codegen.TypeInfo;
import io.vertx.codetrans.CodeModel;
import io.vertx.codetrans.CodeWriter;
import io.vertx.codetrans.ExpressionModel;
import io.vertx.codetrans.LambdaExpressionModel;
import io.vertx.codetrans.Lang;
import io.vertx.codetrans.Script;
import io.vertx.codetrans.StatementModel;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleBindings;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class JavaScriptLang implements Lang {

  @Override
  public CodeWriter newWriter() {
    return new JavaScriptWriter(this);
  }

  @Override
  public Script loadScript(ClassLoader loader, String path) throws Exception {
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
    InputStream in = loader.getResourceAsStream(path + ".js");
    if (in == null) {
      throw new Exception("Could not find " + path + ".js");
    }
    String source = new Scanner(in,"UTF-8").useDelimiter("\\A").next();
    return new Script() {
      @Override
      public String getSource() {
        return source;
      }

      @Override
      public void run(Map<String, Object> globals) throws Exception {
        engine.setBindings(new SimpleBindings(globals), ScriptContext.GLOBAL_SCOPE);
        engine.eval(source);
      }
    };
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

  @Override
  public ExpressionModel asyncResultHandler(LambdaExpressionTree.BodyKind bodyKind, TypeInfo.Parameterized resultType, String resultName, CodeModel body) {
    return new LambdaExpressionModel(bodyKind, Arrays.asList(resultType.getArgs().get(0), TypeInfo.create(Throwable.class)), Arrays.asList(resultName, resultName + "_err"), body);
  }

  @Override
  public StatementModel variableDecl(TypeInfo type, String name, ExpressionModel initializer) {
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
    return StatementModel.conditional((renderer) -> {
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
}
