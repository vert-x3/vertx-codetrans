package io.vertx.codetrans.lang.ruby;

import com.sun.source.tree.LambdaExpressionTree;
import io.vertx.codegen.Case;
import io.vertx.codegen.TypeInfo;
import io.vertx.codetrans.expression.ApiTypeModel;
import io.vertx.codetrans.CodeBuilder;
import io.vertx.codetrans.CodeModel;
import io.vertx.codetrans.CodeWriter;
import io.vertx.codetrans.expression.EnumExpressionModel;
import io.vertx.codetrans.expression.ExpressionModel;
import io.vertx.codetrans.expression.LambdaExpressionModel;
import io.vertx.codetrans.MethodModel;
import io.vertx.codetrans.RunnableCompilationUnit;
import io.vertx.codetrans.statement.StatementModel;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
class RubyCodeBuilder implements CodeBuilder {

  LinkedHashSet<TypeInfo.Class> imports = new LinkedHashSet<>();
  LinkedHashSet<String> requires = new LinkedHashSet<>();

  @Override
  public CodeWriter newWriter() {
    return new RubyWriter(this);
  }

  @Override
  public String render(RunnableCompilationUnit unit) {
    CodeWriter writer = newWriter();
    for (TypeInfo.Class type : imports) {
      requires.add(type.getModuleName() + "/" + Case.SNAKE.format(Case.CAMEL.parse(type.getSimpleName())));
    }
    for (String require : requires) {
      writer.append("require '").append(require).append("'\n");
    }
    for (Map.Entry<String, StatementModel> field : unit.getFields().entrySet()) {
      writer.append("@");
      field.getValue().render(writer);
      writer.append("\n");
    }
    for (Map.Entry<String, MethodModel> member : unit.getMethods().entrySet()) {
      String methodName = Case.SNAKE.format(Case.CAMEL.parse(member.getKey()));
      writer.append("def ").append(methodName).append("(");
      for (Iterator<String> it = member.getValue().getParameterNames().iterator();it.hasNext();) {
        String paramName = it.next();
        writer.append(paramName);
        if (it.hasNext()) {
          writer.append(", ");
        }
      }
      writer.append(")\n");
      writer.indent();
      member.getValue().render(writer);
      writer.unindent();
      writer.append("end\n");
    }
    unit.getMain().render(writer);
    return writer.getBuffer().toString();
  }

  @Override
  public EnumExpressionModel enumType(TypeInfo.Class.Enum type) {
    return CodeBuilder.super.enumType(type);
  }

  @Override
  public ApiTypeModel apiType(TypeInfo.Class.Api type) {
    imports.add(type);
    return CodeBuilder.super.apiType(type);
  }

  @Override
  public ExpressionModel asyncResultHandler(LambdaExpressionTree.BodyKind bodyKind, TypeInfo.Parameterized resultType, String resultName, CodeModel body) {
    return new LambdaExpressionModel(this, bodyKind, Arrays.asList(resultType.getArgs().get(0), TypeInfo.create(Throwable.class)), Arrays.asList(resultName, resultName + "_err"), body);
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
  public ExpressionModel jsonArrayEncoder(ExpressionModel expression) {
    requires.add("json");
    return CodeBuilder.super.jsonArrayEncoder(expression);
  }

  @Override
  public ExpressionModel jsonObjectEncoder(ExpressionModel expression) {
    requires.add("json");
    return CodeBuilder.super.jsonObjectEncoder(expression);
  }
}

