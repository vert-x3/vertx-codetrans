package io.vertx.codetrans.lang.ruby;

import com.sun.source.tree.LambdaExpressionTree;
import io.vertx.codegen.Case;
import io.vertx.codegen.type.ApiTypeInfo;
import io.vertx.codegen.type.ClassKind;
import io.vertx.codegen.type.ClassTypeInfo;
import io.vertx.codegen.type.EnumTypeInfo;
import io.vertx.codegen.type.TypeInfo;
import io.vertx.codetrans.CodeModel;
import io.vertx.codetrans.CodeWriter;
import io.vertx.codetrans.FragmentParser;
import io.vertx.codetrans.Helper;
import io.vertx.codetrans.MethodSignature;
import io.vertx.codetrans.TypeArg;
import io.vertx.codetrans.expression.*;
import io.vertx.codetrans.statement.ConditionalBlockModel;
import io.vertx.codetrans.statement.StatementModel;

import javax.lang.model.element.TypeElement;
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

  public void renderEquals(ExpressionModel left, ExpressionModel right) {
    left.render(this);
    append(".==(");
    right.render(this);
    append(")");
  }

  @Override
  public void renderStringLiteral(List parts) {
    append('"');
    for (Object part : parts) {
      if (part instanceof ExpressionModel) {
        append("#{");
        ExpressionModel ex = (ExpressionModel) part;
        ex.render(this);
        append("}");
      } else {
        renderChars(part.toString());
      }
    }
    append('"');
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

  @Override
  public void renderThis() {
    append("self");
  }

  @Override
  public void renderApiType(ApiTypeInfo apiType) {
    append(Case.CAMEL.format(Case.KEBAB.parse(apiType.getModule().getName())) + "::" + apiType.getSimpleName());
  }

  @Override
  public void renderJavaType(ClassTypeInfo javaType) {
    append("Java::" + Case.CAMEL.format(Case.QUALIFIED.parse(javaType.getPackageName())) + "::" + javaType.getSimpleName());
  }

  @Override
  public void renderAsyncResultSucceeded(TypeInfo resultType, String name) {
    append(name + "_err == nil");
  }

  @Override
  public void renderAsyncResultFailed(TypeInfo resultType, String name) {
    append(name + "_err != nil");
  }

  @Override
  public void renderAsyncResultCause(TypeInfo resultType, String name) {
    append(name + "_err");
  }

  @Override
  public void renderAsyncResultValue(TypeInfo resultType, String name) {
    append(name);
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
  public void renderStatement(StatementModel statement) {
    statement.render(this);
    append("\n");
  }

  @Override
  public void renderTryCatch(StatementModel tryBlock, StatementModel catchBlock) {
    append("begin\n");
    indent();
    tryBlock.render(this);
    unindent();
    append("rescue\n");
    indent();
    catchBlock.render(this);
    unindent();
    append("end\n");
  }

  @Override
  public void renderMethodReference(ExpressionModel expression, MethodSignature signature) {
    if (!(expression instanceof ThisModel)) {
      expression.render(this);
      append(".");
    }

    append("method(:").append(Case.SNAKE.format(Case.CAMEL.parse(signature.getName()))).append(")");
  }

  @Override
  public void renderMethodInvocation(ExpressionModel expression, TypeInfo receiverType, MethodSignature method, TypeInfo returnType, List<TypeArg> typeArguments, List<ExpressionModel> argumentModels, List<TypeInfo> argumentTypes) {
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

    methodName = javaToRubyMethodName(methodName, returnType);

    if (!(expression instanceof ThisModel)) {
      expression.render(this);
      append('.');
    }
    append(methodName);
    renderArguments(argumentModels, this);

    //
    if (lambda != null) {
      append(" ");
      renderBlock(lambda.getBodyKind(), lambda.getParameterTypes(), lambda.getParameterNames(), lambda.getBody(), this);
    }
  }

  String javaToRubyMethodName(String javaName, TypeInfo returnType) {
    String result = Case.SNAKE.format(Case.CAMEL.parse(javaName));
    if (returnType.getName().equals(boolean.class.getName()) || returnType.getName().equals(Boolean.class.getName())) {
      if (result.startsWith("is_")) {
        result = result.substring(3);
      }
      result += "?";
    }
    return result;
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
  public void renderListAdd(ExpressionModel list, ExpressionModel value) {
    list.render(this);
    append(".push(");
    value.render(this);
    append(")");
  }

  @Override
  public void renderListSize(ExpressionModel list) {
    list.render(this);
    append(".length");
  }

  @Override
  public void renderListGet(ExpressionModel list, ExpressionModel index) {
    list.render(this);
    append("[");
    index.render(this);
    append("]");
  }

  @Override
  public void renderListLiteral(List<ExpressionModel> arguments) {
    append("[");
    for (Iterator<ExpressionModel> it = arguments.iterator();it.hasNext();) {
      it.next().render(this);
      if (it.hasNext()) {
        append(", ");
      }
    }
    append("]");
  }

  @Override
  public void renderMapGet(ExpressionModel map, ExpressionModel key) {
    map.render(this);
    append('[');
    key.render(this);
    append(']');
  }

  @Override
  public void renderMapPut(ExpressionModel map, ExpressionModel key, ExpressionModel value) {
    map.render(this);
    append('[');
    key.render(this);
    append("] = ");
    value.render(this);
  }

  @Override
  public void renderMapForEach(ExpressionModel map, String keyName, TypeInfo keyType, String valueName, TypeInfo valueType, LambdaExpressionTree.BodyKind bodyKind, CodeModel block) {
    map.render(this);
    append(".each_pair ");
    renderBlock(bodyKind, Arrays.asList(keyType, valueType), Arrays.asList(keyName, valueName), block, this);
  }

  @Override
  public void renderJsonObject(JsonObjectLiteralModel jsonObject) {
    renderJsonObject(jsonObject.getMembers(), this);
  }

  private void renderJsonObject(Iterable<Member> members, CodeWriter writer) {
    append("{\n");
    indent();
    for (Iterator<Member> iterator = members.iterator();iterator.hasNext();) {
      Member member = iterator.next();
      String name = member.getName();
      append("'");
      renderChars(name);
      append("' => ");
      if (member instanceof Member.Single) {
        ((Member.Single) member).getValue().render(this);
      } else if (member instanceof Member.Sequence) {
        renderJsonArray(((Member.Sequence) member).getValues(), writer);
      } else {
        renderJsonObject(((Member.Entries) member).entries(), writer);
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
    renderJsonObject(model.getMembers(), this);
  }

  @Override
  public void renderJsonObjectAssign(ExpressionModel expression, String name, ExpressionModel value) {
    expression.render(this);
    append("['");
    append(name);
    append("'] = ");
    value.render(this);
  }

  @Override
  public void renderNewMap() {
    append("Hash.new()");
  }

  @Override
  public void renderNewList() {
    append("Array.new");
  }

  @Override
  public void renderDataObjectAssign(ExpressionModel expression, String name, ExpressionModel value) {
    renderJsonObjectAssign(expression, name, value);
  }

  @Override
  public void renderJsonArrayAdd(ExpressionModel expression, ExpressionModel value) {
    expression.render(this);
    append(".push(");
    value.render(this);
    append(")");
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
  public void renderJsonObjectMemberSelect(ExpressionModel expression, Class<?> type, String name) {
    expression.render(this);
    append("['");
    append(name);
    append("']");
  }

  @Override
  public void renderToDataObject(JsonObjectModel model, ClassTypeInfo type) {
    model.render(this);
  }

  @Override
  public void renderDataObjectMemberSelect(ExpressionModel expression, String name) {
    renderJsonObjectMemberSelect(expression, Object.class, name);
  }

  @Override
  public void renderJsonObjectSize(ExpressionModel expression) {
    expression.render(this);
    append(".length");
  }

  @Override
  public void renderJsonArraySize(ExpressionModel expression) {
    expression.render(this);
    append(".length");
  }

  @Override
  public void renderIdentifier(String name, VariableScope scope) {
    switch (scope) {
      case GLOBAL:
        name = "$" + name;
        break;
      case FIELD:
        name = "@" + name;
        break;
    }
    super.renderIdentifier(name, scope);
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
  public void renderEnumConstant(EnumTypeInfo type, String constant) {
    append(':').append(constant);
  }

  @Override
  public void renderThrow(String throwableType, ExpressionModel reason) {
    if (reason == null) {
      append("raise ").append("\"an error occurred\"");
    } else {
      append("raise ");
      reason.render(this);
    }
  }

  @Override
  public void renderSystemOutPrintln(ExpressionModel expression) {
    append("puts ");
    expression.render(this);
  }

  @Override
  public void renderSystemErrPrintln(ExpressionModel expression) {
    append("STDERR.puts ");
    expression.render(this);
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

  @Override
  public void renderInstanceOf(ExpressionModel expression, TypeElement type) {
    expression.render(this);
    append(".class.name == '");
    append("Java::");
    String qn = type.getQualifiedName().toString();
    int idx = qn.lastIndexOf('.');
    String pkg = qn.substring(0, idx);
    append(Case.QUALIFIED.to(Case.CAMEL, pkg));
    append("::");
    append(type.getSimpleName());
    append("'");
  }
}
