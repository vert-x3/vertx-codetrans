package io.vertx.codetrans.lang.ruby;

import com.sun.source.tree.LambdaExpressionTree;
import io.vertx.codegen.Case;
import io.vertx.codegen.ClassKind;
import io.vertx.codegen.TypeInfo;
import io.vertx.codetrans.BinaryExpressionModel;
import io.vertx.codetrans.BlockModel;
import io.vertx.codetrans.CodeModel;
import io.vertx.codetrans.CodeWriter;
import io.vertx.codetrans.ConditionalBlockModel;
import io.vertx.codetrans.DataObjectLiteralModel;
import io.vertx.codetrans.ExpressionModel;
import io.vertx.codetrans.FragmentParser;
import io.vertx.codetrans.Helper;
import io.vertx.codetrans.JsonArrayLiteralModel;
import io.vertx.codetrans.JsonObjectLiteralModel;
import io.vertx.codetrans.LambdaExpressionModel;
import io.vertx.codetrans.Member;
import io.vertx.codetrans.MethodRef;
import io.vertx.codetrans.StatementModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
* @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
*/
class RubyWriter extends CodeWriter {

  final RubyCodeBuilder builder;

  RubyWriter(RubyCodeBuilder builder) {
    super(builder);
    this.builder = builder;
  }

  public void renderEquals(ExpressionModel expression, ExpressionModel arg) {
    expression.render(this);
    append(".==(");
    arg.render(this);
    append(")");
  }

  @Override
  public void renderFragment(String fragment) {
    FragmentParser renderer = new FragmentParser() {
      @Override
      public void onNewline() {
        append('\n');
      }
      @Override
      public void onComment(char c) {
        append(c);
      }
      @Override
      public void onBeginComment(boolean multiline) {
        append(multiline ? "=begin" : "#");
      }
      @Override
      public void onEndComment(boolean multiline) {
        if (multiline) {
          append("=end");
        }
      }
    };
    renderer.parse(fragment);
  }

  // Temporary hack
  private int depth = 0;

  @Override
  public void renderBlock(BlockModel block) {
    if (depth++ > 0) {
      super.renderBlock(block);
    } else {
      super.renderBlock(block);
      StringBuilder buffer = getBuffer();
      String tmp = buffer.toString();
      buffer.setLength(0);
      for (TypeInfo.Class type : builder.imports) {
        builder.requires.add(type.getModuleName() + "/" + Case.SNAKE.format(Case.CAMEL.parse(type.getSimpleName())));
      }
      for (String require : builder.requires) {
        append("require '").append(require).append("'\n");
      }
      append(tmp);
    }
    depth--;
  }

  @Override
  public void renderThis() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void renderApiType(TypeInfo.Class.Api apiType) {
    String expr = Case.CAMEL.format(Case.KEBAB.parse(apiType.getModule().getName())) + "::" + apiType.getSimpleName();
    append(expr);
  }

  @Override
  public void renderConditionals(List<ConditionalBlockModel> conditionals, StatementModel otherwise) {
    for (int i = 0;i < conditionals.size();i++) {
      ConditionalBlockModel conditional = conditionals.get(i);
      append(i == 0 ? "if " : "elsif ");
      conditional.getCondition().render(this);
      append("\n");
      indent();
      conditional.getBody().render(this);
      unindent();
    }
    if (otherwise != null) {
      append("else\n");
      indent();
      otherwise.render(this);
      unindent();
      append("end");
    } else {
      append("end");
    }
  }

  @Override
  public void renderPrefixIncrement(ExpressionModel expression, CodeWriter writer) {
    expression.render(this);
    append("+=1");
  }

  @Override
  public void renderPostfixIncrement(ExpressionModel expression) {
    expression.render(this);
    append("+=1");
  }

  @Override
  public void renderPostfixDecrement(ExpressionModel expression) {
    expression.render(this);
    append("-=1");
  }

  @Override
  public void renderPrefixDecrement(ExpressionModel expression) {
    expression.render(this);
    append("-=1");
  }

  @Override
  public void renderNullLiteral() {
    append("nil");
  }

  @Override
  public void renderBinary(BinaryExpressionModel expression) {
    if (Helper.isString(expression)) {
      Helper.renderInterpolatedString(expression, this, "#{", "}");
    } else {
      super.renderBinary(expression);
    }
  }

  @Override
  public void renderStatement(StatementModel statement) {
    statement.render(this);
    append("\n");
  }

  @Override
  public void renderMethodReference(ExpressionModel expression, String methodName) {
    expression.render(this);
    append(".method(:").append(Case.SNAKE.format(Case.CAMEL.parse(methodName))).append(")");
  }

  @Override
  public void renderMethodInvocation(ExpressionModel expression, TypeInfo receiverType, MethodRef method, TypeInfo returnType, List<ExpressionModel> argumentModels, List<TypeInfo> argumentTypes) {
    List<TypeInfo> parameterTypes = method.getParameterTypes();
    String methodName = method.getName();
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
              argumentModels.set(index, builder.render(writer2 -> {
                append("&");
                lastExpr.render(this);
                append(".method(:handle)");
              }));
            } else {
              argumentModels = new ArrayList<>(argumentModels);
              argumentModels.set(index, builder.render(writer2 -> {
                append("&");
                lastExpr.render(this);
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

    expression.render(this);
    append('.');
    append(methodName);
    renderArguments(argumentModels, this);

    //
    if (lambda != null) {
      append(" ");
      renderBlock(lambda.getBodyKind(), lambda.getParameterTypes(), lambda.getParameterNames(), lambda.getBody(), this);
    }
  }

  private void renderArguments(List<ExpressionModel> arguments, CodeWriter writer) {
    append('(');
    for (int i = 0; i < arguments.size(); i++) {
      if (i > 0) {
        append(", ");
      }
      arguments.get(i).render(this);
    }
    append(')');
  }

  @Override
  public void renderMapGet(ExpressionModel map, ExpressionModel arg) {
    map.render(this);
    append('[');
    arg.render(this);
    append(']');
  }

  @Override
  public void renderMapForEach(ExpressionModel map, String keyName, TypeInfo keyType, String valueName, TypeInfo valueType, LambdaExpressionTree.BodyKind bodyKind, CodeModel block) {
    map.render(this);
    append(".each_pair ");
    renderBlock(bodyKind, Arrays.asList(keyType, valueType), Arrays.asList(keyName, valueName), block, this);
  }

  @Override
  public void renderJsonObject(JsonObjectLiteralModel jsonObject) {
    renderJsonObject(jsonObject.getMembers(), this, true);
  }

  private void renderJsonObject(Iterable<Member> members, CodeWriter writer, boolean unquote) {
    append("{\n");
    indent();
    for (Iterator<Member> iterator = members.iterator();iterator.hasNext();) {
      Member member = iterator.next();
      String name = member.getName().render(getBuilder());
      if (unquote) {
        name = io.vertx.codetrans.Helper.unwrapQuotedString(name);
      }
      append("'").append(name).append("' => ");
      if (member instanceof Member.Single) {
        ((Member.Single) member).getValue().render(this);
      } else {
        renderJsonArray(((Member.Array) member).getValues(), writer);
      }
      if (iterator.hasNext()) {
        append(',');
      }
      append('\n');
    }
    unindent().append("}");
  }

  private void renderJsonArray(List<ExpressionModel> values, CodeWriter writer) {
    append("[\n").indent();
    for (int i = 0;i < values.size();i++) {
      values.get(i).render(this);
      if (i < values.size() - 1) {
        append(',');
      }
      append('\n');
    }
    unindent().append(']');
  }

  @Override
  public void renderJsonArray(JsonArrayLiteralModel jsonArray) {
    renderJsonArray(jsonArray.getValues(), this);
  }

  @Override
  public void renderDataObject(DataObjectLiteralModel model) {
    renderJsonObject(model.getMembers(), this, false);
  }

  @Override
  public void renderJsonObjectAssign(ExpressionModel expression, ExpressionModel name, ExpressionModel value) {
    expression.render(this);
    append("[");
    name.render(this);
    append("] = ");
    value.render(this);
  }

  @Override
  public void renderDataObjectAssign(ExpressionModel expression, ExpressionModel name, ExpressionModel value) {
    renderJsonObjectAssign(expression, builder.render(writer2 -> {
      writer2.append("'");
      name.render(writer2);
      writer2.append("'");
    }), value);
  }

  @Override
  public void renderJsonObjectToString(ExpressionModel expression) {
    append("JSON.generate(");
    expression.render(this);
    append(")");
  }

  @Override
  public void renderJsonArrayToString(ExpressionModel expression) {
    append("JSON.generate(");
    expression.render(this);
    append(")");
  }

  @Override
  public void renderJsonObjectMemberSelect(ExpressionModel expression, ExpressionModel name) {
    expression.render(this);
    append("[");
    name.render(this);
    append("]");
  }

  @Override
  public void renderDataObjectMemberSelect(ExpressionModel expression, ExpressionModel name) {
    renderJsonObjectMemberSelect(expression, builder.render(writer2 -> {
      writer2.append("'");
      name.render(writer2);
      writer2.append("'");
    }));
  }

  /**
   * Renders a ruby block with curly brace syntax.
   */
  private void renderBlock(LambdaExpressionTree.BodyKind bodyKind, List<TypeInfo> parameterTypes, List<String> parameterNames, CodeModel body, CodeWriter writer) {
    append("{");
    if (parameterNames.size() > 0) {
      append(" |");
      for (int i = 0; i < parameterNames.size(); i++) {
        if (i > 0) {
          append(",");
        }
        append(parameterNames.get(i));
      }
      append("|");
    }
    append("\n");
    indent();
    body.render(this);
    if (bodyKind == LambdaExpressionTree.BodyKind.EXPRESSION) {
      append("\n");
    }
    unindent();
    append("}");
  }

  @Override
  public void renderLambda(LambdaExpressionTree.BodyKind bodyKind, List<TypeInfo> parameterTypes, List<String> parameterNames, CodeModel body) {
    append("lambda ");
    renderBlock(bodyKind, parameterTypes, parameterNames, body, this);
  }

  @Override
  public void renderEnumConstant(TypeInfo.Class.Enum type, String constant) {
    append(':').append(constant);
  }

  @Override
  public void renderThrow(String throwableType, ExpressionModel reason) {
    if (reason == null) {
      append("raise ").append("\"an error occured\"");
    } else {
      append("raise ");
      reason.render(this);
    }
  }

  @Override
  public void renderMemberSelect(ExpressionModel expression, String identifier) {
    expression.render(this);
    append("::").append(identifier);
  }

  @Override
  public void renderNew(ExpressionModel expression, TypeInfo type, List<ExpressionModel> argumentModels) {
    append("");
    expression.render(this);
    append(".new");
    renderArguments(argumentModels, this);
  }
}
