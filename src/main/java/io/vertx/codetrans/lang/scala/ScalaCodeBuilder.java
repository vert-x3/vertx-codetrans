package io.vertx.codetrans.lang.scala;

import com.sun.source.tree.LambdaExpressionTree;
import io.vertx.codegen.type.ApiTypeInfo;
import io.vertx.codegen.type.EnumTypeInfo;
import io.vertx.codegen.type.ParameterizedTypeInfo;
import io.vertx.codegen.type.TypeInfo;
import io.vertx.codetrans.CodeBuilder;
import io.vertx.codetrans.CodeModel;
import io.vertx.codetrans.CodeWriter;
import io.vertx.codetrans.MethodModel;
import io.vertx.codetrans.RenderMode;
import io.vertx.codetrans.RunnableCompilationUnit;
import io.vertx.codetrans.expression.ApiModel;
import io.vertx.codetrans.expression.ApiTypeModel;
import io.vertx.codetrans.expression.EnumExpressionModel;
import io.vertx.codetrans.expression.ExpressionModel;
import io.vertx.codetrans.expression.VariableScope;
import io.vertx.codetrans.statement.StatementModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * @author <a href="mailto:jochen.mader@codecentric.de">Jochen Mader</a
 */
public class ScalaCodeBuilder implements CodeBuilder {

  private Set<String> imports = new HashSet<>();
  private List<String> asyncResults = new ArrayList<>();

  @Override
  public CodeWriter newWriter() {
    return new ScalaCodeWriter(this);
  }

  @Override
  public ApiTypeModel apiType(ApiTypeInfo type) {
    imports.add(type.translateName("scala"));
    return CodeBuilder.super.apiType(type);
  }

  @Override
  public EnumExpressionModel enumType(EnumTypeInfo type) {
    imports.add(type.getName());
    return CodeBuilder.super.enumType(type);
  }

  @Override
  public StatementModel variableDecl(VariableScope scope, TypeInfo type, String name, ExpressionModel initializer) {
    return StatementModel.render(renderer -> {
      renderer.append("var ").append(name);
      if (initializer != null) {
        renderer.append(" = ");
        initializer.render(renderer);
      } else {
        renderer.append(" = null.asInstanceOf[").append(type.getName()).append(']');
      }
    });
  }

  @Override
  public ExpressionModel asyncResultHandler(LambdaExpressionTree.BodyKind bodyKind, ParameterizedTypeInfo parameterizedTypeInfo, String s, CodeModel codeModel, CodeModel codeModel1, CodeModel codeModel2) {
    imports.add("scala.util.Failure");
    imports.add("scala.util.Success");
    return new ExpressionModel(this) {
      public void render(CodeWriter writer) {
        asyncResults.add(s);
        writer.append("{\n");
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
          writer.append("\n}\n");
        } else {
          writer.append("println(\"Failure\")\n");
          writer.unindent();
        }

        writer.unindent();
        writer.append("}");
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
        renderer.append("})\n");
      }
    };
  }

  @Override
  public StatementModel forLoop(StatementModel initializer, ExpressionModel condition, ExpressionModel update, StatementModel body) {
    return new StatementModel() {
      public void render(CodeWriter renderer) {
        initializer.render(renderer);
        renderer.append("\nwhile(");
        condition.render(renderer);
        renderer.append("){\n");
        renderer.indent();
        body.render(renderer);
        renderer.append("\n");
        update.render(renderer);
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
        renderer.append(" until ");
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
  public String render(RunnableCompilationUnit unit, RenderMode renderMode) {
    CodeWriter writer = newWriter();

    if (renderMode != RenderMode.SNIPPET) {
      for (String importedType : imports) {
        writer.append("import ").append(importedType).append('\n');
      }

      for (StatementModel value : unit.getFields().values()) {
        value.render(writer);
        writer.append("\n");
      }
      for (Map.Entry<String, MethodModel> method : unit.getMethods().entrySet()) {
        writer.append("def ").append(method.getKey()).append("(");

        IntStream.range(0, method.getValue().getParameterNames().size()).forEach(i -> {
          if (i > 0) writer.append(", ");
          writer.append(method.getValue().getParameterNames().get(i));
          writer.append(":");
          writer.append(method.getValue().getSignature().getParameterTypes().get(i).getName());
        });
        writer.append(") = {\n");
        writer.indent();
        method.getValue().render(writer);
        writer.unindent();
        writer.append("}\n");
      }
    }

    unit.getMain().render(writer);

    String ret = writer.getBuffer().toString();
    //remove return statements
    ret = ret.replace("return ","");
    ret = convertLeftoverUsageOfAsyncResultMethods(ret);
    ret = removeThisIfStringRepresentsAScript(ret);

    if (renderMode == RenderMode.TEST) {
      String className = unit.getMain().getClassName();
      ret = "package " + className + "\n" +
        "class " + unit.getMain().getSignature().getName() + " extends (() => Any) {\n" +
        "  def apply() = {\n" +
        ret +
        "  }\n" +
        "}\n";
    }

    return ret;
  }

  private String removeThisIfStringRepresentsAScript(String ret) {
    if (!ret.contains("class")){
      return ret.replace("this.","");
    }
    return ret;
  }

  private String convertLeftoverUsageOfAsyncResultMethods(String ret) {
    return asyncResults.stream()
            .reduce(ret, (identity, value) -> identity.replace(value + ".result()", "result")
                    .replace(value + ".succeeded()", "true"));
  }

}
