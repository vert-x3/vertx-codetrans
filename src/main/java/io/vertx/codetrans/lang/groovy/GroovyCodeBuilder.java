package io.vertx.codetrans.lang.groovy;

import com.sun.source.tree.LambdaExpressionTree;
import io.vertx.codegen.TypeInfo;
import io.vertx.codetrans.ApiTypeModel;
import io.vertx.codetrans.CodeModel;
import io.vertx.codetrans.CodeWriter;
import io.vertx.codetrans.EnumExpressionModel;
import io.vertx.codetrans.ExpressionModel;
import io.vertx.codetrans.LambdaExpressionModel;
import io.vertx.codetrans.CodeBuilder;
import io.vertx.codetrans.MethodModel;
import io.vertx.codetrans.MethodRef;
import io.vertx.codetrans.RunnableCompilationUnit;
import io.vertx.codetrans.StatementModel;

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
    for (TypeInfo.Class importedType : imports) {
      String fqn = importedType.getName();
      if (importedType instanceof TypeInfo.Class.Api) {
        fqn = importedType.translateName("groovy");
      }
      writer.append("import ").append(fqn).append('\n');
    }
    for (Map.Entry<String, MethodModel> member : unit.getMembers().entrySet()) {
      writer.append("def ").append(member.getKey()).append("(");
      for (Iterator<String> it = member.getValue().getParameterNames().iterator();it.hasNext();) {
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
  public ExpressionModel javaType(TypeInfo.Class type) {
    return render(type.getName());
  }

  @Override
  public ExpressionModel asyncResult(String identifier) {
    return render(renderer -> renderer.append(identifier));
  }

  @Override
  public ExpressionModel asyncResultHandler(LambdaExpressionTree.BodyKind bodyKind, TypeInfo.Parameterized resultType, String resultName, CodeModel body) {
    return new LambdaExpressionModel(this, bodyKind, Collections.singletonList(resultType), Collections.singletonList(resultName), body);
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
    return render(renderer -> {
      renderer.append("println(");
      expression.render(renderer);
      renderer.append(")");
    });
  }
}
