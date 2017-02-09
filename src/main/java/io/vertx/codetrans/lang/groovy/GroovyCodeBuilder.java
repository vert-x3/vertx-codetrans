package io.vertx.codetrans.lang.groovy;

import com.sun.source.tree.LambdaExpressionTree;
import io.vertx.codegen.type.ApiTypeInfo;
import io.vertx.codegen.type.ClassTypeInfo;
import io.vertx.codegen.type.EnumTypeInfo;
import io.vertx.codegen.type.ParameterizedTypeInfo;
import io.vertx.codegen.type.TypeInfo;
import io.vertx.codetrans.CodeBuilder;
import io.vertx.codetrans.CodeModel;
import io.vertx.codetrans.MethodModel;
import io.vertx.codetrans.RenderMode;
import io.vertx.codetrans.RunnableCompilationUnit;
import io.vertx.codetrans.expression.ApiTypeModel;
import io.vertx.codetrans.expression.EnumExpressionModel;
import io.vertx.codetrans.expression.EnumFieldExpressionModel;
import io.vertx.codetrans.expression.ExpressionModel;
import io.vertx.codetrans.expression.LambdaExpressionModel;
import io.vertx.codetrans.expression.StringLiteralModel;
import io.vertx.codetrans.expression.VariableScope;
import io.vertx.codetrans.statement.StatementModel;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
class GroovyCodeBuilder implements CodeBuilder {

  LinkedHashSet<ClassTypeInfo> imports = new LinkedHashSet<>();

  @Override
  public GroovyWriter newWriter() {
    return new GroovyWriter(this);
  }

  @Override
  public String render(RunnableCompilationUnit unit, RenderMode renderMode) {
    GroovyWriter writer = newWriter();
    if (renderMode != RenderMode.SNIPPET) {
      if (unit.getFields().size() > 0) {
        writer.append("import groovy.transform.Field\n");
      }
      for (ClassTypeInfo importedType : imports) {
        String fqn = importedType.getName();
        writer.append("import ").append(fqn).append('\n');
      }
      for (Map.Entry<String, StatementModel> field : unit.getFields().entrySet()) {
        writer.append("@Field ");
        field.getValue().render(writer);
        writer.append("\n");
      }
      for (Map.Entry<String, MethodModel> method : unit.getMethods().entrySet()) {
        writer.append("def ").append(method.getKey()).append("(");
        for (Iterator<String> it = method.getValue().getParameterNames().iterator(); it.hasNext(); ) {
          String paramName = it.next();
          writer.append(paramName);
          if (it.hasNext()) {
            writer.append(", ");
          }
        }
        writer.append(") {\n");
        writer.indent();
        method.getValue().render(writer);
        writer.unindent();
        writer.append("}\n");
      }
    }
    unit.getMain().render(writer);
    return writer.getBuffer().toString();
  }

  @Override
  public EnumExpressionModel enumType(EnumTypeInfo type) {
    imports.add(type);
    return CodeBuilder.super.enumType(type);
  }

  @Override
  public ApiTypeModel apiType(ApiTypeInfo type) {
    imports.add(type);
    return CodeBuilder.super.apiType(type);
  }

  @Override
  public ExpressionModel toDataObjectValue(EnumFieldExpressionModel enumField) {
    return new StringLiteralModel(this, enumField.identifier);
  }

  @Override
  public ExpressionModel asyncResultHandler(LambdaExpressionTree.BodyKind bodyKind, ParameterizedTypeInfo resultType, String resultName, CodeModel body, CodeModel succeededBody, CodeModel failedBody) {
    return new LambdaExpressionModel(this, bodyKind, Collections.singletonList(resultType), Collections.singletonList(resultName), body);
  }

  @Override
  public StatementModel variableDecl(VariableScope scope, TypeInfo type, String name, ExpressionModel initializer) {
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
  public StatementModel sequenceForLoop(String variableName, ExpressionModel fromValue, ExpressionModel toValue, StatementModel body) {
    return StatementModel.render(writer -> {
      writer.append('(');
      fromValue.render(writer);
      writer.append("..<");
      toValue.render(writer);
      writer.append(").each { ").append(variableName).append(" ->\n");
      writer.indent();
      body.render(writer);
      writer.unindent();
      writer.append("}");
    });
  }
}
