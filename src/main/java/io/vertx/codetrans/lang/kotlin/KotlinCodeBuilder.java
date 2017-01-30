package io.vertx.codetrans.lang.kotlin;

import com.sun.source.tree.LambdaExpressionTree;
import io.vertx.codegen.type.*;
import io.vertx.codetrans.*;
import io.vertx.codetrans.expression.*;
import io.vertx.codetrans.statement.StatementModel;

import java.util.*;

/**
 * @author Sergey Mashkov
 */
public class KotlinCodeBuilder implements CodeBuilder {
  private TreeSet<String> imports = new TreeSet<>();

  KotlinCodeBuilder() {
    imports.add("io.vertx.kotlin.common.json.*");
  }

  @Override
  public KotlinCodeWriter newWriter() {
    return new KotlinCodeWriter(this);
  }

  @Override
  public String render(RunnableCompilationUnit unit, boolean standalone) {
    KotlinCodeWriter writer = newWriter();

    if (standalone) {
      String className = unit.getMain().getClassName();
      writer.append("package ").append(className).append("\n\n");
    }

    for (String i : imports) {
      writer.append("import ").append(i).append("\n");
    }
    writer.append("\n");

    if (standalone) {
      writer.append("object ").append(unit.getMain().getSignature().getName()).append(" {\n");
      writer.indent();
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

    if (standalone) {
      writer.append("fun ").append(unit.getMain().getSignature().getName()).append("() {\n");
      writer.indent();
    }

    unit.getMain().render(writer);

    if (standalone) {
      writer.unindent();
      writer.append("}\n");
      writer.unindent();
      writer.append("}\n");
    }

    return writer.getBuffer().toString();
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
        renderer.append(" = ");
        initializer.render(renderer);
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
    if (importedType instanceof ApiTypeInfo) {
      fqn = importedType.translateName("kotlin");
    }
    imports.add(fqn);
  }
}
