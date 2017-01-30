package io.vertx.codetrans.lang.ruby;

import com.sun.source.tree.LambdaExpressionTree;
import io.vertx.codegen.Case;
import io.vertx.codegen.type.TypeInfo;
import io.vertx.codegen.type.ApiTypeInfo;
import io.vertx.codegen.type.ClassTypeInfo;
import io.vertx.codegen.type.EnumTypeInfo;
import io.vertx.codegen.type.ParameterizedTypeInfo;
import io.vertx.codegen.type.TypeReflectionFactory;
import io.vertx.codetrans.expression.ApiTypeModel;
import io.vertx.codetrans.CodeBuilder;
import io.vertx.codetrans.CodeModel;
import io.vertx.codetrans.CodeWriter;
import io.vertx.codetrans.expression.EnumExpressionModel;
import io.vertx.codetrans.expression.ExpressionModel;
import io.vertx.codetrans.expression.VariableScope;
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

  LinkedHashSet<ClassTypeInfo> imports = new LinkedHashSet<>();
  LinkedHashSet<String> requires = new LinkedHashSet<>();

  @Override
  public CodeWriter newWriter() {
    return new RubyWriter(this);
  }

  @Override
  public String render(RunnableCompilationUnit unit, boolean standalone) {
    CodeWriter writer = newWriter();
    for (ClassTypeInfo type : imports) {
      requires.add(type.getModuleName() + "/" + Case.SNAKE.format(Case.CAMEL.parse(type.getSimpleName())));
    }
    for (String require : requires) {
      writer.append("require '").append(require).append("'\n");
    }
    for (Map.Entry<String, StatementModel> field : unit.getFields().entrySet()) {
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
  public EnumExpressionModel enumType(EnumTypeInfo type) {
    return CodeBuilder.super.enumType(type);
  }

  @Override
  public ApiTypeModel apiType(ApiTypeInfo type) {
    imports.add(type);
    return CodeBuilder.super.apiType(type);
  }

  @Override
  public ExpressionModel asyncResultHandler(LambdaExpressionTree.BodyKind bodyKind, ParameterizedTypeInfo resultType, String resultName, CodeModel body, CodeModel succeededBody, CodeModel failedBody) {
    return new LambdaExpressionModel(
        this,
        bodyKind,
        Arrays.asList(TypeReflectionFactory.create(Throwable.class), resultType.getArgs().get(0)),
        Arrays.asList(resultName + "_err", resultName),
        body);
  }

  @Override
  public StatementModel variableDecl(VariableScope scope, TypeInfo type, String name, ExpressionModel initializer) {
    return StatementModel.render(renderer -> {
      switch (scope) {
        case FIELD:
          renderer.append('@');
          renderer.append(name);
          break;
        case GLOBAL:
          throw new UnsupportedOperationException("Does not make sense");
        default:
          if (initializer != null) {
            renderer.append(name);
          }
          break;
      }
      if (initializer != null ) {
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
  public StatementModel sequenceForLoop(String variableName, ExpressionModel fromValue, ExpressionModel toValue, StatementModel body) {
    return StatementModel.render(writer -> {
      writer.append('(');
      fromValue.render(writer);
      writer.append("...");
      toValue.render(writer);
      writer.append(").each do |").append(variableName).append("|\n");
      writer.indent();
      body.render(writer);
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

