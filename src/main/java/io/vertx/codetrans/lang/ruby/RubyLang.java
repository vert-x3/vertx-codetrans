package io.vertx.codetrans.lang.ruby;

import com.sun.source.tree.LambdaExpressionTree;
import io.vertx.codegen.Case;
import io.vertx.codegen.TypeInfo;
import io.vertx.codetrans.ApiTypeModel;
import io.vertx.codetrans.CodeModel;
import io.vertx.codetrans.CodeWriter;
import io.vertx.codetrans.EnumExpressionModel;
import io.vertx.codetrans.ExpressionModel;
import io.vertx.codetrans.LambdaExpressionModel;
import io.vertx.codetrans.Lang;
import io.vertx.codetrans.Script;
import io.vertx.codetrans.StatementModel;
import org.jruby.embed.LocalContextScope;
import org.jruby.embed.ScriptingContainer;

import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Scanner;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class RubyLang implements Lang {

  LinkedHashSet<TypeInfo.Class> imports = new LinkedHashSet<>();
  LinkedHashSet<String> requires = new LinkedHashSet<>();

  @Override
  public CodeWriter newWriter() {
    return new RubyWriter(this);
  }

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
      public void run(Map<String, Object> globals) {
        for (Map.Entry<String, Object> global : globals.entrySet()) {
          container.put("$" + global.getKey(), global.getValue());
        }
        container.runScriptlet(source);
      }
    };
  }

  @Override
  public String getExtension() {
    return "rb";
  }

  @Override
  public EnumExpressionModel enumType(TypeInfo.Class.Enum type) {
    return Lang.super.enumType(type);
  }

  @Override
  public ApiTypeModel apiType(TypeInfo.Class.Api type) {
    imports.add(type);
    return Lang.super.apiType(type);
  }

  @Override
  public ExpressionModel classExpression(TypeInfo.Class type) {
    return ExpressionModel.render(
        "Java::" + Case.CAMEL.format(Case.QUALIFIED.parse(type.getPackageName())) + "::" + type.getSimpleName()
    );
  }

  @Override
  public ExpressionModel asyncResult(String identifier) {
    return ExpressionModel.forMethodInvocation((member, args) -> {
      switch (member) {
        case "succeeded":
          return ExpressionModel.render(identifier + "_err == nil");
        case "result":
          return ExpressionModel.render(identifier);
        case "cause":
          return ExpressionModel.render(identifier + "_err");
        case "failed":
          return ExpressionModel.render(identifier + "_err != nil");
        default:
          throw new UnsupportedOperationException("Not implemented");
      }
    });
  }

  @Override
  public ExpressionModel asyncResultHandler(LambdaExpressionTree.BodyKind bodyKind, TypeInfo.Parameterized resultType, String resultName, CodeModel body) {
    return new LambdaExpressionModel(bodyKind, Arrays.asList(resultType.getArgs().get(0), TypeInfo.create(Throwable.class)), Arrays.asList(resultName, resultName + "_err"), body);
  }

  @Override
  public StatementModel variableDecl(TypeInfo type, String name, ExpressionModel initializer) {
    return StatementModel.render(renderer -> {
      if (initializer != null) {
        renderer.append(name);
        renderer.append(" = ");
        initializer.render(renderer);
      }
    });
  }

  @Override
  public StatementModel enhancedForLoop(String variableName, ExpressionModel expression, StatementModel body) {
    return StatementModel.render(renderer -> {
      expression.render(renderer);
      renderer.append(".each do |").append(variableName).append("|\n");
      renderer.indent();
      body.render(renderer);
      renderer.unindent();
      renderer.append("end");
    });
  }

  @Override
  public StatementModel forLoop(StatementModel initializer, ExpressionModel condition, ExpressionModel update, StatementModel body) {
    return StatementModel.render(writer -> {
      initializer.render(writer);
      writer.append('\n');
      writer.append("while (");
      condition.render(writer);
      writer.append(")\n");
      writer.indent();
      body.render(writer);
      update.render(writer);
      writer.append('\n');
      writer.unindent();
      writer.append("end");
    });
  }

  @Override
  public ExpressionModel console(ExpressionModel expression) {
    return ExpressionModel.render(renderer -> {
      renderer.append("puts ");
      expression.render(renderer);
    });
  }

  @Override
  public ExpressionModel variable(TypeInfo type, boolean local, String name) {
    if (!local) {
      name = "$" + name;
    }
    return Lang.super.variable(type, true, name);
  }
}

