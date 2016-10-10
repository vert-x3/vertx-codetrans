package io.vertx.codetrans.lang.scala;

import com.sun.source.tree.LambdaExpressionTree;
import io.vertx.codegen.type.*;
import io.vertx.codetrans.*;
import io.vertx.codetrans.expression.*;
import io.vertx.codetrans.statement.StatementModel;

import java.util.*;
import java.util.stream.IntStream;

/**
 * @author <a href="mailto:jochen.mader@codecentric.de">Jochen Mader</a
 */
public class ScalaCodeBuilder implements CodeBuilder {

  private Set<ClassTypeInfo> imports = new HashSet<>();
  private List<String> asyncResults = new ArrayList<>();

  @Override
  public CodeWriter newWriter() {
    return new ScalaCodeWriter(this);
  }

  @Override
  public ApiTypeModel apiType(ApiTypeInfo type) {
    imports.add(type);
    return CodeBuilder.super.apiType(type);
  }

  @Override
  public EnumExpressionModel enumType(EnumTypeInfo type) {
    imports.add(type);
    return CodeBuilder.super.enumType(type);
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
  public ExpressionModel asyncResultHandler(LambdaExpressionTree.BodyKind bodyKind, ParameterizedTypeInfo parameterizedTypeInfo, String s, CodeModel codeModel, CodeModel codeModel1, CodeModel codeModel2) {
    return new ExpressionModel(this) {
      public void render(CodeWriter writer) {
        asyncResults.add(s);
        writer.append("\n");
        writer.indent();
        writer.append("case Success(result) => ");
        writer.indent();

        if (codeModel1 != null) {
          writer.append("{\n");
          codeModel1.render(writer);
          writer.unindent();
          writer.append("}\n");
        } else {
          writer.append("println(\"Success\")\n");
          writer.unindent();
        }

        writer.append("case Failure(cause) => ");
        writer.indent();
        if (codeModel2 != null) {
          writer.append("{\n");
          writer.append("println(s\"$cause\")");
//          codeModel2.render(writer)
          writer.unindent();
          writer.append("}\n");
        } else {
          writer.append("println(\"Failure\")\n");
          writer.unindent();
        }
        writer.unindent();
      }
    };
  }

  @Override
  public StatementModel enhancedForLoop(String variableName, ExpressionModel expression, StatementModel body) {
    return new StatementModel() {
      public void render(CodeWriter renderer) {
        expression.render(renderer);
        renderer.append(".foreach(");
        renderer.append(variableName);
        renderer.append(" => {");
        renderer.append("\n");
        renderer.indent();
        body.render(renderer);
        renderer.unindent();
        renderer.append("}\n");
      }
    };
  }

  @Override
  public StatementModel forLoop(StatementModel initializer, ExpressionModel condition, ExpressionModel update, StatementModel body) {
    return new StatementModel() {
      public void render(CodeWriter renderer) {
        BinaryExpressionModel condExp = (BinaryExpressionModel) condition;
        initializer.render(renderer);
        renderer.append("\nwhile(");
        condition.render(renderer);
        renderer.append("){\n");
        renderer.indent();
        condExp.getLeft().render(renderer);
        renderer.append(" += ");
        update.render(renderer);
        renderer.append("\n");
        body.render(renderer);
        renderer.unindent();
        renderer.append("}\n");
      }
    };
  }

  @Override
  public StatementModel sequenceForLoop(String variableName, ExpressionModel fromValue, ExpressionModel toValue, StatementModel body) {
    return new StatementModel() {
      public void render(CodeWriter renderer) {
        renderer.append("for ( " + variableName + " <- ");
        fromValue.render(renderer);
        renderer.append(" to ");
        toValue.render(renderer);
        renderer.append(") {\n");
        renderer.indent();
        body.render(renderer);
        renderer.unindent();
        renderer.append("}\n");
      }
    };
  }

  @Override
  public ApiModel api(ExpressionModel expr) {
    return new ScalaApiModel(this, expr);
  }

  @Override
  public String render(RunnableCompilationUnit unit) {
    CodeWriter writer = newWriter();

    for (ClassTypeInfo importedType : imports) {
      String fqn = importedType.getName();
      //only translate actual API-types
      if (importedType instanceof ApiTypeInfo) fqn = importedType.translateName("scala");
      writer.append("import ").append(fqn).append('\n');
    }

    for (StatementModel value : unit.getFields().values()) {
      writer.append("val ");
      value.render(writer);
      writer.append("\n");
    }
    for (Map.Entry<String, MethodModel> method : unit.getMethods().entrySet()) {
      writer.append("def ").append(method.getKey()).append("(");

      IntStream.range(0, method.getValue().getParameterNames().size()).forEach(i -> {
        if (i > 0) writer.append(", ");
        writer.append(method.getValue().getParameterNames().get(i));
      });

      writer.append(") {\n");
      writer.indent();
      method.getValue().render(writer);
      writer.unindent();
      writer.append("}\n");
    }
    unit.getMain().render(writer);

    return asyncResults.stream().reduce(writer.getBuffer().toString(), (identity, value) -> identity.replace(value + ".result()", "result"));
  }

}
