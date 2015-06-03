package io.vertx.codetrans;

import com.sun.source.tree.LambdaExpressionTree;
import io.vertx.codegen.Case;
import io.vertx.codegen.ClassKind;
import io.vertx.codegen.TypeInfo;
import org.jruby.embed.LocalContextScope;
import org.jruby.embed.ScriptingContainer;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class RubyLang implements Lang {

  static class RubyWriter extends CodeWriter {
    LinkedHashSet<TypeInfo.Class> imports = new LinkedHashSet<>();
    LinkedHashSet<String> requires = new LinkedHashSet<>();
    RubyWriter(Lang lang) {
      super(lang);
    }
  }

  @Override
  public Script loadScript(ClassLoader loader, String path) throws Exception {
    String filename = path + ".rb";
    InputStream in = loader.getResourceAsStream(filename);
    if (in == null) {
      throw new Exception("Could not find " + filename);
    }
    String source = new Scanner(in,"UTF-8").useDelimiter("\\A").next();
    ScriptingContainer container = new ScriptingContainer(LocalContextScope.SINGLETHREAD);
    return new Script() {
      @Override
      public String getSource() {
        return source;
      }

      @Override
      public void run(Map<String, Object> globals) {
        for (Map.Entry<String, Object> global : globals.entrySet()) {
          container.put("$" + global.getKey(), global.getValue());
        }
        container.runScriptlet(source);
      }
    };
  }

  public void renderEquals(ExpressionModel expression, ExpressionModel arg, CodeWriter writer) {
    expression.render(writer);
    writer.append(".==(");
    arg.render(writer);
    writer.append(")");
  }

  @Override
  public void renderFragment(String fragment, CodeWriter writer) {
    FragmentParser renderer = new FragmentParser() {
      @Override
      public void onNewline() {
        writer.append('\n');
      }
      @Override
      public void onComment(char c) {
        writer.append(c);
      }
      @Override
      public void onBeginComment(boolean multiline) {
        writer.append(multiline ? "=begin" : "#");
      }
      @Override
      public void onEndComment(boolean multiline) {
        if (multiline) {
          writer.append("=end");
        }
      }
    };
    renderer.parse(fragment);
  }

  @Override
  public void renderBlock(BlockModel block, CodeWriter writer) {
    if (writer instanceof RubyWriter) {
      Lang.super.renderBlock(block, writer);
    } else {
      RubyWriter langRenderer = new RubyWriter(this);
      Lang.super.renderBlock(block, langRenderer);
      LinkedHashSet<String> requires = new LinkedHashSet<>(langRenderer.requires);
      for (TypeInfo.Class type : langRenderer.imports) {
        requires.add(type.getModuleName() + "/" + Case.SNAKE.format(Case.CAMEL.parse(type.getSimpleName())));
      }
      for (String require : requires) {
        writer.append("require '").append(require).append("'\n");
      }
      writer.append(langRenderer.getBuffer());
    }
  }

  @Override
  public void renderConditionals(List<ConditionalBlockModel> conditionals, StatementModel otherwise, CodeWriter writer) {
    for (int i = 0;i < conditionals.size();i++) {
      ConditionalBlockModel conditional = conditionals.get(i);
      writer.append(i == 0 ? "if " : "elsif ");
      conditional.condition.render(writer);
      writer.append("\n");
      writer.indent();
      conditional.body.render(writer);
      writer.unindent();
    }
    if (otherwise != null) {
      writer.append("else\n");
      writer.indent();
      otherwise.render(writer);
      writer.unindent();
      writer.append("end");
    } else {
      writer.append("end");
    }
  }

  @Override
  public void renderPrefixIncrement(ExpressionModel expression, CodeWriter writer) {
    expression.render(writer);
    writer.append("+=1");
  }

  @Override
  public void renderPostfixIncrement(ExpressionModel expression, CodeWriter writer) {
    expression.render(writer);
    writer.append("+=1");
  }

  @Override
  public void renderPostfixDecrement(ExpressionModel expression, CodeWriter writer) {
    expression.render(writer);
    writer.append("-=1");
  }

  @Override
  public void renderPrefixDecrement(ExpressionModel expression, CodeWriter writer) {
    expression.render(writer);
    writer.append("-=1");
  }

  @Override
  public void renderNullLiteral(CodeWriter writer) {
    writer.append("nil");
  }

  @Override
  public void renderBinary(BinaryExpressionModel expression, CodeWriter writer) {
    if (Helper.isString(expression)) {
      Helper.renderInterpolatedString(expression, writer, "#{", "}");
    } else {
      Lang.super.renderBinary(expression, writer);
    }
  }

  @Override
  public void renderStatement(StatementModel statement, CodeWriter writer) {
    statement.render(writer);
    writer.append("\n");
  }

  @Override
  public void renderMethodReference(ExpressionModel expression, String methodName, CodeWriter writer) {
    expression.render(writer);
    writer.append(".method(:").append(Case.SNAKE.format(Case.CAMEL.parse(methodName))).append(")");
  }

  @Override
  public void renderMethodInvocation(ExpressionModel expression, TypeInfo receiverType, String methodName, TypeInfo returnType, List<TypeInfo> parameterTypes, List<ExpressionModel> argumentModels, List<TypeInfo> argumentTypes, CodeWriter writer) {
    int size = parameterTypes.size();
    int index = size - 1;

    // Api patching
    if (receiverType.getKind() == ClassKind.STRING) {
      if (methodName.equals("startsWith")) {
        methodName = "startWith";
      }
    }

    LambdaExpressionModel lambda = null;

    for (int i = 0;i < size;i++) {
      if (Helper.isHandler(parameterTypes.get(index))) {
        if (i == size - 1) {
          ExpressionModel lastExpr = argumentModels.get(index);
          if (lastExpr instanceof LambdaExpressionModel) {
            lambda = (LambdaExpressionModel) lastExpr;
            parameterTypes = parameterTypes.subList(0, size - 1);
            argumentModels = argumentModels.subList(0, size - 1);
            argumentTypes = argumentTypes.subList(0, size - 1);
          } else {
            if (Helper.isInstanceOfHandler(argumentTypes.get(i))) {
              argumentModels = new ArrayList<>(argumentModels);
              argumentModels.set(index, ExpressionModel.render(writer2 -> {
                writer.append("&");
                lastExpr.render(writer);
                writer.append(".method(:handle)");
              }));
            } else {
              argumentModels = new ArrayList<>(argumentModels);
              argumentModels.set(index, ExpressionModel.render(writer2 -> {
                writer.append("&");
                lastExpr.render(writer);
              }));
            }
          }
        } else {
          // Do nothing for now
        }
      }
    }

    methodName = Case.SNAKE.format(Case.CAMEL.parse(methodName));
    if (returnType.getName().equals("boolean") || returnType.getName().equals("java.lang.Boolean")) {
      if (methodName.startsWith("is_")) {
        methodName = methodName.substring(3);
      }
      methodName += "?";
    }

    expression.render(writer);
    writer.append('.');
    writer.append(methodName);
    renderArguments(argumentModels, writer);

    //
    if (lambda != null) {
      writer.append(" ");
      renderBlock(lambda.getBodyKind(), lambda.getParameterTypes(), lambda.getParameterNames(), lambda.getBody(), writer);
    }
  }

  private void renderArguments(List<ExpressionModel> arguments, CodeWriter writer) {
    writer.append('(');
    for (int i = 0; i < arguments.size(); i++) {
      if (i > 0) {
        writer.append(", ");
      }
      arguments.get(i).render(writer);
    }
    writer.append(')');
  }

  @Override
  public void renderFloatLiteral(String value, CodeWriter writer) {
    writer.renderChars(value);
  }

  @Override
  public void renderDoubleLiteral(String value, CodeWriter writer) {
    writer.renderChars(value);
  }

  @Override
  public void renderLongLiteral(String value, CodeWriter writer) {
    writer.renderChars(value);
  }

  @Override
  public void renderMapGet(ExpressionModel map, ExpressionModel arg, CodeWriter writer) {
    map.render(writer);
    writer.append('[');
    arg.render(writer);
    writer.append(']');
  }

  @Override
  public void renderMapForEach(ExpressionModel map, String keyName, TypeInfo keyType, String valueName, TypeInfo valueType, LambdaExpressionTree.BodyKind bodyKind, CodeModel block, CodeWriter writer) {
    map.render(writer);
    writer.append(".each_pair ");
    renderBlock(bodyKind, Arrays.asList(keyType, valueType), Arrays.asList(keyName, valueName), block, writer);
  }

  @Override
  public String getExtension() {
    return "rb";
  }

  @Override
  public void renderJsonObject(JsonObjectLiteralModel jsonObject, CodeWriter writer) {
    renderJsonObject(jsonObject.getMembers(), writer, true);
  }

  private void renderJsonObject(Iterable<Member> members, CodeWriter writer, boolean unquote) {
    writer.append("{\n");
    writer.indent();
    for (Iterator<Member> iterator = members.iterator();iterator.hasNext();) {
      Member member = iterator.next();
      String name = member.name.render(writer.getLang());
      if (unquote) {
        name = io.vertx.codetrans.Helper.unwrapQuotedString(name);
      }
      writer.append("'").append(name).append("' => ");
      if (member instanceof Member.Single) {
        ((Member.Single) member).value.render(writer);
      } else {
        renderJsonArray(((Member.Array) member).values, writer);
      }
      if (iterator.hasNext()) {
        writer.append(',');
      }
      writer.append('\n');
    }
    writer.unindent().append("}");
  }

  private void renderJsonArray(List<ExpressionModel> values, CodeWriter writer) {
    writer.append("[\n").indent();
    for (int i = 0;i < values.size();i++) {
      values.get(i).render(writer);
      if (i < values.size() - 1) {
        writer.append(',');
      }
      writer.append('\n');
    }
    writer.unindent().append(']');
  }

  @Override
  public void renderJsonArray(JsonArrayLiteralModel jsonArray, CodeWriter writer) {
    renderJsonArray(jsonArray.getValues(), writer);
  }

  @Override
  public void renderDataObject(DataObjectLiteralModel model, CodeWriter writer) {
    renderJsonObject(model.getMembers(), writer, false);
  }

  @Override
  public void renderJsonObjectAssign(ExpressionModel expression, ExpressionModel name, ExpressionModel value, CodeWriter writer) {
    expression.render(writer);
    writer.append("[");
    name.render(writer);
    writer.append("] = ");
    value.render(writer);
  }

  @Override
  public void renderDataObjectAssign(ExpressionModel expression, ExpressionModel name, ExpressionModel value, CodeWriter writer) {
    renderJsonObjectAssign(expression, ExpressionModel.render(writer2 -> {
      writer2.append("'");
      name.render(writer2);
      writer2.append("'");
    }), value, writer);
  }

  @Override
  public void renderJsonObjectToString(ExpressionModel expression, CodeWriter writer) {
    RubyWriter rubyWriter = (RubyWriter) writer;
    rubyWriter.requires.add("json");
    writer.append("JSON.generate(");
    expression.render(writer);
    writer.append(")");
  }

  @Override
  public void renderJsonArrayToString(ExpressionModel expression, CodeWriter writer) {
    RubyWriter rubyWriter = (RubyWriter) writer;
    rubyWriter.requires.add("json");
    writer.append("JSON.generate(");
    expression.render(writer);
    writer.append(")");
  }

  @Override
  public void renderJsonObjectMemberSelect(ExpressionModel expression, ExpressionModel name, CodeWriter writer) {
    expression.render(writer);
    writer.append("[");
    name.render(writer);
    writer.append("]");
  }

  @Override
  public void renderDataObjectMemberSelect(ExpressionModel expression, ExpressionModel name, CodeWriter writer) {
    renderJsonObjectMemberSelect(expression, ExpressionModel.render(writer2 -> {
      writer2.append("'");
      name.render(writer2);
      writer2.append("'");
    }), writer);
  }

  /**
   * Renders a ruby block with curly brace syntax.
   */
  private void renderBlock(LambdaExpressionTree.BodyKind bodyKind, List<TypeInfo> parameterTypes, List<String> parameterNames, CodeModel body, CodeWriter writer) {
    writer.append("{");
    if (parameterNames.size() > 0) {
      writer.append(" |");
      for (int i = 0; i < parameterNames.size(); i++) {
        if (i > 0) {
          writer.append(",");
        }
        writer.append(parameterNames.get(i));
      }
      writer.append("|");
    }
    writer.append("\n");
    writer.indent();
    body.render(writer);
    if (bodyKind == LambdaExpressionTree.BodyKind.EXPRESSION) {
      writer.append("\n");
    }
    writer.unindent();
    writer.append("}");
  }

  @Override
  public void renderLambda(LambdaExpressionTree.BodyKind bodyKind, List<TypeInfo> parameterTypes, List<String> parameterNames, CodeModel body, CodeWriter writer) {
    writer.append("lambda ");
    renderBlock(bodyKind, parameterTypes, parameterNames, body, writer);
  }

  @Override
  public void renderEnumConstant(TypeInfo.Class.Enum type, String constant, CodeWriter writer) {
    writer.append(':').append(constant);
  }

  @Override
  public void renderThrow(String throwableType, ExpressionModel reason, CodeWriter writer) {
    if (reason == null) {
      writer.append("raise ").append("\"an error occured\"");
    } else {
      writer.append("raise ");
      reason.render(writer);
    }
  }

  @Override
  public ExpressionModel classExpression(TypeInfo.Class type) {
    return ExpressionModel.render(
        "Java::" + Case.CAMEL.format(Case.QUALIFIED.parse(type.getPackageName())) + "::" + type.getSimpleName()
    );
  }

  @Override
  public ExpressionModel asyncResult(String identifier) {
    return ExpressionModel.forMethodInvocation((member, args) -> {
      switch (member) {
        case "succeeded":
          return ExpressionModel.render(identifier + "_err == nil");
        case "result":
          return ExpressionModel.render(identifier);
        case "cause":
          return ExpressionModel.render(identifier + "_err");
        case "failed":
          return ExpressionModel.render(identifier + "_err != nil");
        default:
          throw new UnsupportedOperationException("Not implemented");
      }
    });
  }

  @Override
  public ExpressionModel asyncResultHandler(LambdaExpressionTree.BodyKind bodyKind, TypeInfo.Parameterized resultType, String resultName, CodeModel body) {
    return new LambdaExpressionModel(bodyKind, Arrays.asList(resultType.getArgs().get(0), TypeInfo.create(Throwable.class)), Arrays.asList(resultName, resultName + "_err"), body);
  }

  @Override
  public ExpressionModel staticFactory(TypeInfo.Class receiverType, String methodName, TypeInfo returnType, List<TypeInfo> parameterTypes, List<ExpressionModel> arguments, List<TypeInfo> argumentTypes) {
    return ExpressionModel.render(writer -> {
      RubyWriter jsRenderer = (RubyWriter) writer;
      jsRenderer.imports.add(receiverType);
      String expr = Case.CAMEL.format(Case.KEBAB.parse(receiverType.getModule().getName())) + "::" + receiverType.getSimpleName();
      renderMethodInvocation(ExpressionModel.render(expr), receiverType, methodName, returnType, parameterTypes, arguments, argumentTypes, writer);
    });
  }

  @Override
  public void renderMemberSelect(ExpressionModel expression, String identifier, CodeWriter writer) {
    expression.render(writer);
    writer.append("::").append(identifier);
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
  public ExpressionModel console(ExpressionModel expression) {
    return ExpressionModel.render(renderer -> {
      renderer.append("puts ");
      expression.render(renderer);
    });
  }

  @Override
  public ExpressionModel variable(TypeInfo type, boolean local, String name) {
    if (!local) {
      name = "$" + name;
    }
    return Lang.super.variable(type, true, name);
  }

  @Override
  public void renderNew(ExpressionModel expression, TypeInfo type, List<ExpressionModel> argumentModels, CodeWriter writer) {
    writer.append("");
    expression.render(writer);
    writer.append(".new");
    renderArguments(argumentModels, writer);
  }
}

