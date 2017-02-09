package io.vertx.codetrans.lang.js;

import com.sun.source.tree.LambdaExpressionTree;
import io.vertx.codegen.Helper;
import io.vertx.codegen.type.ApiTypeInfo;
import io.vertx.codegen.type.ClassTypeInfo;
import io.vertx.codegen.type.ParameterizedTypeInfo;
import io.vertx.codegen.type.TypeInfo;
import io.vertx.codegen.type.TypeReflectionFactory;
import io.vertx.codetrans.CodeBuilder;
import io.vertx.codetrans.CodeModel;
import io.vertx.codetrans.CodeWriter;
import io.vertx.codetrans.MethodModel;
import io.vertx.codetrans.RenderMode;
import io.vertx.codetrans.RunnableCompilationUnit;
import io.vertx.codetrans.expression.ApiTypeModel;
import io.vertx.codetrans.expression.EnumFieldExpressionModel;
import io.vertx.codetrans.expression.ExpressionModel;
import io.vertx.codetrans.expression.LambdaExpressionModel;
import io.vertx.codetrans.expression.StringLiteralModel;
import io.vertx.codetrans.expression.VariableScope;
import io.vertx.codetrans.statement.StatementModel;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
class JavaScriptCodeBuilder implements CodeBuilder {

  LinkedHashSet<ClassTypeInfo> modules = new LinkedHashSet<>();

  @Override
  public CodeWriter newWriter() {
    return new JavaScriptWriter(this);
  }

  @Override
  public String render(RunnableCompilationUnit unit, RenderMode renderMode) {
    CodeWriter writer = newWriter();
    for (ClassTypeInfo module : modules) {
      writer.append("var ").append(module.getSimpleName()).append(" = require(\"").
        append(module.getModuleName()).append("-js/").append(Helper.convertCamelCaseToUnderscores(module.getSimpleName())).append("\");\n");
    }
    if (renderMode != RenderMode.SNIPPET) {
      for (Map.Entry<String, StatementModel> field : unit.getFields().entrySet()) {
        field.getValue().render(writer);
        writer.append(";\n");
      }
      for (Map.Entry<String, MethodModel> member : unit.getMethods().entrySet()) {
        writer.append("var ").append(member.getKey()).append(" = function(");
        for (Iterator<String> it = member.getValue().getParameterNames().iterator(); it.hasNext(); ) {
          String paramName = it.next();
          writer.append(paramName);
          if (it.hasNext()) {
            writer.append(", ");
          }
        }
        writer.append(") {\n");
        writer.indent();
        member.getValue().render(writer);
        writer.unindent();
        writer.append("};\n");
      }
    }
    unit.getMain().render(writer);
    return writer.getBuffer().toString();
  }

  @Override
  public ApiTypeModel apiType(ApiTypeInfo type) {
    modules.add(type);
    return CodeBuilder.super.apiType(type);
  }

  @Override
  public ExpressionModel toDataObjectValue(EnumFieldExpressionModel enumField) {
    return new StringLiteralModel(this, enumField.identifier);
  }

  @Override
  public ExpressionModel asyncResultHandler(LambdaExpressionTree.BodyKind bodyKind, ParameterizedTypeInfo resultType, String resultName, CodeModel body, CodeModel succeededBody, CodeModel failedBody) {
    return new LambdaExpressionModel(this, bodyKind, Arrays.asList(resultType.getArgs().get(0), TypeReflectionFactory.create(Throwable.class)), Arrays.asList(resultName, resultName + "_err"), body);
  }

  @Override
  public StatementModel variableDecl(VariableScope scope, TypeInfo type, String name, ExpressionModel initializer) {
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
  public StatementModel sequenceForLoop(String variableName, ExpressionModel fromValue, ExpressionModel toValue, StatementModel body) {
    return StatementModel.conditional((renderer) -> {
      renderer.append("for (var ").append(variableName).append(" = ");
      fromValue.render(renderer);
      renderer.append(";").append(variableName).append(" < ");
      toValue.render(renderer);
      renderer.append(";").append(variableName).append("++) {\n");
      renderer.indent();
      body.render(renderer);
      renderer.unindent();
      renderer.append("}");
    });
  }
}
