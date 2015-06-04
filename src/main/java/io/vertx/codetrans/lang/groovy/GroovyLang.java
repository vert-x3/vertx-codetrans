package io.vertx.codetrans.lang.groovy;

import com.sun.source.tree.LambdaExpressionTree;
import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyCodeSource;
import groovy.lang.Script;
import io.vertx.codegen.TypeInfo;
import io.vertx.codetrans.CodeModel;
import io.vertx.codetrans.CodeWriter;
import io.vertx.codetrans.ExpressionModel;
import io.vertx.codetrans.LambdaExpressionModel;
import io.vertx.codetrans.Lang;
import io.vertx.codetrans.MethodRef;
import io.vertx.codetrans.StatementModel;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class GroovyLang implements Lang {

  @Override
  public CodeWriter newWriter() {
    return new GroovyWriter(this);
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

  @Override
  public String getExtension() {
    return "groovy";
  }

  @Override
  public ExpressionModel classExpression(TypeInfo.Class type) {
    return ExpressionModel.render(type.getName());
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
  public ExpressionModel staticFactory(TypeInfo.Class receiverType, MethodRef method, TypeInfo returnType, List<ExpressionModel> arguments, List<TypeInfo> argumentTypes) {
    String methodName = method.getName();
    return ExpressionModel.render(writer -> {
      GroovyWriter jsRenderer = (GroovyWriter) writer;
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

  @Override
  public ExpressionModel console(ExpressionModel expression) {
    return ExpressionModel.render(renderer -> {
      renderer.append("println(");
      expression.render(renderer);
      renderer.append(")");
    });
  }
}
