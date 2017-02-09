package io.vertx.codetrans.lang.kotlin;

import com.sun.source.tree.LambdaExpressionTree;
import io.vertx.codegen.type.ApiTypeInfo;
import io.vertx.codegen.type.ClassTypeInfo;
import io.vertx.codegen.type.EnumTypeInfo;
import io.vertx.codegen.type.ParameterizedTypeInfo;
import io.vertx.codegen.type.TypeInfo;
import io.vertx.codegen.type.VoidTypeInfo;
import io.vertx.codetrans.CodeBuilder;
import io.vertx.codetrans.CodeModel;
import io.vertx.codetrans.CodeWriter;
import io.vertx.codetrans.MethodModel;
import io.vertx.codetrans.RenderMode;
import io.vertx.codetrans.RunnableCompilationUnit;
import io.vertx.codetrans.expression.ApiTypeModel;
import io.vertx.codetrans.expression.DataObjectClassModel;
import io.vertx.codetrans.expression.EnumExpressionModel;
import io.vertx.codetrans.expression.ExpressionModel;
import io.vertx.codetrans.expression.JsonArrayClassModel;
import io.vertx.codetrans.expression.JsonObjectClassModel;
import io.vertx.codetrans.expression.LambdaExpressionModel;
import io.vertx.codetrans.expression.NullLiteralModel;
import io.vertx.codetrans.expression.VariableScope;
import io.vertx.codetrans.statement.StatementModel;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

/**
 * @author Sergey Mashkov
 */
public class KotlinCodeBuilder implements CodeBuilder {
  private TreeSet<String> imports = new TreeSet<>();

  KotlinCodeBuilder() {
  }

  @Override
  public KotlinCodeWriter newWriter() {
    return new KotlinCodeWriter(this);
  }

  @Override
  public String render(RunnableCompilationUnit unit, RenderMode renderMode) {
    KotlinCodeWriter writer = newWriter();

    if (renderMode != RenderMode.SNIPPET) {
      String foo;
      String className = unit.getMain().getClassName();
      String pkg;
      if (unit.isVerticle()) {
        int index = className.lastIndexOf('.');
        pkg = className.substring(0, index);
        foo = className.substring(index + 1);
      } else {
        pkg = className;
        foo = unit.getMain().getSignature().getName();
      }
      writer.append("package ").append(pkg).append("\n\n");

      for (String i : imports) {
        writer.append("import ").append(i).append("\n");
      }
      writer.append("\n");


      switch (renderMode) {
        case TEST:
          writer.append("object ").append(foo).append(" {\n");
          writer.indent();
          break;
        case EXAMPLE:
          writer.append("class ").append(foo).append(" : io.vertx.core.AbstractVerticle() ").append(" {\n");
          writer.indent();
          break;
      }

      for (Map.Entry<String, StatementModel> field : unit.getFields().entrySet()) {
        field.getValue().render(writer);
        writer.append("\n");
      }

      for (Map.Entry<String, MethodModel> method : unit.getMethods().entrySet()) {
        writer.append("fun ").append(method.getKey()).append("(");
        List<TypeInfo> types = method.getValue().getSignature().getParameterTypes();
        List<String> names = method.getValue().getParameterNames();

        int count = Math.min(types.size(), names.size());

        for (int i = 0; i < count; ++i) {
          String name = names.get(i);
          TypeInfo type = types.get(i);

          if (i > 0) {
            writer.append(", ");
          }

          writer.append(name).append(": ");
          renderType(type, writer);
        }

        writer.append(") ");

        TypeInfo returnType = method.getValue().getSignature().getReturnType();
        if (returnType != VoidTypeInfo.INSTANCE) {
          writer.append(": ");
          renderType(returnType, writer);
        }

        writer.append("{\n");
        writer.indent();
        method.getValue().render(writer);
        writer.unindent();
        writer.append("}\n");
      }

      switch (renderMode) {
        case TEST:
          writer.append("fun ").append(unit.getMain().getSignature().getName()).append("() {\n");
          writer.indent();
          break;
        case EXAMPLE:
          writer.append("override fun ").append("start").append("() {\n");
          writer.indent();
          break;
      }
    }

    unit.getMain().render(writer);

    if (renderMode != RenderMode.SNIPPET) {
        writer.unindent();
        writer.append("}\n");
        writer.unindent();
        writer.append("}\n");
    }

    return writer.getBuffer().toString();
  }

  @Override
  public DataObjectClassModel dataObjectClass(ClassTypeInfo type) {
    addImport(type);
    imports.add(type.translatePackageName("kotlin") + ".*");
    return CodeBuilder.super.dataObjectClass(type);
  }

  @Override
  public EnumExpressionModel enumType(EnumTypeInfo type) {
    addImport(type);
    return CodeBuilder.super.enumType(type);
  }

  @Override
  public ApiTypeModel apiType(ApiTypeInfo type) {
    addImport(type);
    return CodeBuilder.super.apiType(type);
  }

  @Override
  public ExpressionModel asyncResultHandler(LambdaExpressionTree.BodyKind bodyKind, ParameterizedTypeInfo resultType, String resultName, CodeModel body, CodeModel succeededBody, CodeModel failedBody) {
    return new LambdaExpressionModel(this, bodyKind, Collections.singletonList(resultType), Collections.singletonList(resultName), body);
  }

  @Override
  public StatementModel variableDecl(VariableScope scope, TypeInfo type, String name, ExpressionModel initializer) {
    return StatementModel.render(renderer -> {
      switch (scope) {
        case PARAMETER:
          break;
        case VARIABLE:
        case FIELD:
        case GLOBAL:
          renderer.append("var ");
          break;
      }

      renderer.append(name);
      if (initializer != null) {
        if (initializer instanceof NullLiteralModel) {
          renderer.append(": ");
          renderType(type, renderer);
          renderer.append("? = null");
        } else {
          renderer.append(" = ");
          initializer.render(renderer);
        }
      } else {
        renderer.append(": ");
        renderType(type, renderer);
      }
    });
  }

  @Override
  public StatementModel enhancedForLoop(String variableName, ExpressionModel expression, StatementModel body) {
    return StatementModel.render(renderer -> {
      renderer.append("for (").append(variableName).append(" in ");
      expression.render(renderer);
      renderer.append(") {\n");
      renderer.indent();

      body.render(renderer);

      renderer.unindent();
      renderer.append("}");
    });
  }

  @Override
  public StatementModel forLoop(StatementModel initializer, ExpressionModel condition, ExpressionModel update, StatementModel body) {
    return StatementModel.render(renderer -> {
      renderer.renderStatement(initializer);

      renderer.append("while (");
      condition.render(renderer);
      renderer.append(") {\n");
      renderer.indent();

      body.render(renderer);
      renderer.append("\n");
      update.render(renderer);

      renderer.unindent();
      renderer.append("}\n");
    });
  }

  @Override
  public StatementModel sequenceForLoop(String variableName, ExpressionModel fromValue, ExpressionModel toValue, StatementModel body) {
    return StatementModel.render(renderer -> {
      renderer.append("for (");
      renderer.renderIdentifier(variableName, VariableScope.VARIABLE);
      renderer.append(" in ");
      fromValue.render(renderer);
      renderer.append(" until ");
      toValue.render(renderer);
      renderer.append(") {\n");
      renderer.indent();

      body.render(renderer);

      renderer.unindent();
      renderer.append("\n}\n");
    });
  }

  @Override
  public JsonObjectClassModel jsonObjectClassModel() {
    imports.add("io.vertx.kotlin.core.json.*");
    return CodeBuilder.super.jsonObjectClassModel();
  }

  @Override
  public JsonArrayClassModel jsonArrayClassModel() {
    imports.add("io.vertx.kotlin.core.json.*");
    return CodeBuilder.super.jsonArrayClassModel();
  }

  private void renderType(TypeInfo type, CodeWriter renderer) {
    if (type instanceof ApiTypeInfo) {
      renderer.renderApiType((ApiTypeInfo) type);
    } else if (type instanceof ClassTypeInfo) {
      renderer.renderJavaType((ClassTypeInfo) type);
    } else {
      renderer.append(type.getName());
    }
  }

  private void addImport(ClassTypeInfo importedType) {
    String fqn = importedType.getName();
/*
    if (importedType instanceof ApiTypeInfo) {
      fqn = importedType.translateName("kotlin");
    }
*/
    imports.add(fqn);
  }
}
