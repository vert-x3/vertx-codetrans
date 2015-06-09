package io.vertx.codetrans.lang.groovy;

import com.sun.source.tree.LambdaExpressionTree;
import io.vertx.codegen.TypeInfo;
import io.vertx.codetrans.expression.ApiTypeModel;
import io.vertx.codetrans.CodeModel;
import io.vertx.codetrans.expression.EnumExpressionModel;
import io.vertx.codetrans.expression.ExpressionModel;
import io.vertx.codetrans.expression.IdentifierScope;
import io.vertx.codetrans.expression.LambdaExpressionModel;
import io.vertx.codetrans.CodeBuilder;
import io.vertx.codetrans.MethodModel;
import io.vertx.codetrans.RunnableCompilationUnit;
import io.vertx.codetrans.statement.StatementModel;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
class GroovyCodeBuilder implements CodeBuilder {

  LinkedHashSet<TypeInfo.Class> imports = new LinkedHashSet<>();

  @Override
  public GroovyWriter newWriter() {
    return new GroovyWriter(this);
  }

  @Override
  public String render(RunnableCompilationUnit unit) {
    GroovyWriter writer = newWriter();
    if (unit.getFields().size() > 0) {
      writer.append("import groovy.transform.Field\n");
    }
    for (TypeInfo.Class importedType : imports) {
      String fqn = importedType.getName();
      if (importedType instanceof TypeInfo.Class.Api) {
        fqn = importedType.translateName("groovy");
      }
      writer.append("import ").append(fqn).append('\n');
    }
    for (Map.Entry<String, StatementModel> field : unit.getFields().entrySet()) {
      writer.append("@Field ");
      field.getValue().render(writer);
      writer.append("\n");
    }
    for (Map.Entry<String, MethodModel> method : unit.getMethods().entrySet()) {
      writer.append("def ").append(method.getKey()).append("(");
      for (Iterator<String> it = method.getValue().getParameterNames().iterator();it.hasNext();) {
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
    unit.getMain().render(writer);
    return writer.getBuffer().toString();
  }

  @Override
  public EnumExpressionModel enumType(TypeInfo.Class.Enum type) {
    imports.add(type);
    return CodeBuilder.super.enumType(type);
  }

  @Override
  public ApiTypeModel apiType(TypeInfo.Class.Api type) {
    imports.add(type);
    return CodeBuilder.super.apiType(type);
  }

  @Override
  public ExpressionModel asyncResultHandler(LambdaExpressionTree.BodyKind bodyKind, TypeInfo.Parameterized resultType, String resultName, CodeModel body) {
    return new LambdaExpressionModel(this, bodyKind, Collections.singletonList(resultType), Collections.singletonList(resultName), body);
  }

  @Override
  public StatementModel variableDecl(IdentifierScope scope, TypeInfo type, String name, ExpressionModel initializer) {
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
}
