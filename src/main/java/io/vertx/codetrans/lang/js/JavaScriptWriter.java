package io.vertx.codetrans.lang.js;

import com.sun.source.tree.LambdaExpressionTree;
import io.vertx.codegen.Helper;
import io.vertx.codegen.TypeInfo;
import io.vertx.codetrans.BinaryExpressionModel;
import io.vertx.codetrans.BlockModel;
import io.vertx.codetrans.CodeModel;
import io.vertx.codetrans.CodeWriter;
import io.vertx.codetrans.DataObjectLiteralModel;
import io.vertx.codetrans.ExpressionModel;
import io.vertx.codetrans.JsonArrayLiteralModel;
import io.vertx.codetrans.JsonObjectLiteralModel;
import io.vertx.codetrans.Lang;
import io.vertx.codetrans.Member;
import io.vertx.codetrans.MethodRef;
import io.vertx.codetrans.StatementModel;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
* @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
*/
class JavaScriptWriter extends CodeWriter {

  final JavaScriptLang lang;

  JavaScriptWriter(JavaScriptLang lang) {
    super(lang);
    this.lang = lang;
  }

  @Override
  public void renderBinary(BinaryExpressionModel expression) {
    String op = expression.getOp();
    switch (op) {
      case "==":
        op = "===";
        break;
      case "!=":
        op = "!==";
        break;
    }
    super.renderBinary(new BinaryExpressionModel(lang, expression.getLeft(), op, expression.getRight()));
  }

  @Override
  public void renderStatement(StatementModel statement) {
    statement.render(this);
    // In javascript, conditional structure should not have an ending ;. This generates an empty instruction.
    if (statement instanceof StatementModel.Expression) {
      append(";");
    }
    append("\n");
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
      for (TypeInfo.Class module : lang.modules) {
        append("var ").append(module.getSimpleName()).append(" = require(\"").
            append(module.getModuleName()).append("-js/").append(Helper.convertCamelCaseToUnderscores(module.getSimpleName())).append("\");\n");
      }
      append(tmp);
    }
    depth--;
  }

  @Override
  public void renderThis() {
    throw new UnsupportedOperationException();
  }

  public void renderDataObject(DataObjectLiteralModel model) {
    renderJsonObject(model.getMembers(), false);
  }

  public void renderJsonObject(JsonObjectLiteralModel jsonObject) {
    renderJsonObject(jsonObject.getMembers(), true);
  }

  public void renderJsonArray(JsonArrayLiteralModel jsonArray) {
    renderJsonArray(jsonArray.getValues());
  }

  private void renderJsonObject(Iterable<Member> members, boolean unquote) {
    append("{\n");
    indent();
    for (Iterator<Member> iterator = members.iterator();iterator.hasNext();) {
      Member member = iterator.next();
      String name = member.getName().render(getLang());
      if (unquote) {
        name = io.vertx.codetrans.Helper.unwrapQuotedString(name);
      }
      append("\"").append(name).append("\" : ");
      if (member instanceof Member.Single) {
        ((Member.Single) member).getValue().render(this);
      } else {
        renderJsonArray(((Member.Array) member).getValues());
      }
      if (iterator.hasNext()) {
        append(',');
      }
      append('\n');
    }
    unindent().append("}");
  }

  private void renderJsonArray(List<ExpressionModel> values) {
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
  public void renderJsonObjectAssign(ExpressionModel expression, ExpressionModel name, ExpressionModel value) {
    expression.render(this);
    append('.');
    name.render(this);
    append(" = ");
    value.render(this);
  }

  @Override
  public void renderDataObjectAssign(ExpressionModel expression, ExpressionModel name, ExpressionModel value) {
    renderJsonObjectAssign(expression, name, value);
  }

  @Override
  public void renderJsonObjectMemberSelect(ExpressionModel expression, ExpressionModel name) {
    expression.render(this);
    append('.');
    name.render(this);
  }

  @Override
  public void renderJsonObjectToString(ExpressionModel expression) {
    append("JSON.stringify(");
    expression.render(this);
    append(")");
  }

  @Override
  public void renderJsonArrayToString(ExpressionModel expression) {
    append("JSON.stringify(");
    expression.render(this);
    append(")");
  }

  @Override
  public void renderDataObjectMemberSelect(ExpressionModel expression, ExpressionModel name) {
    renderJsonObjectMemberSelect(expression, name);
  }

  @Override
  public void renderLambda(LambdaExpressionTree.BodyKind bodyKind, List<TypeInfo> parameterTypes, List<String> parameterNames, CodeModel body) {
    append("function (");
    for (int i = 0; i < parameterNames.size(); i++) {
      if (i > 0) {
        append(", ");
      }
      append(parameterNames.get(i));
    }
    append(") {\n");
    indent();
    body.render(this);
    if (bodyKind == LambdaExpressionTree.BodyKind.EXPRESSION) {
      append(";\n");
    }
    unindent();
    append("}");
  }

  @Override
  public void renderEnumConstant(TypeInfo.Class.Enum type, String constant) {
    append('\'').append(constant).append('\'');
  }

  @Override
  public void renderThrow(String throwableType, ExpressionModel reason) {
    if (reason == null) {
      append("throw ").append("\"an error occured\"");
    } else {
      append("throw ");
      reason.render(this);
    }
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
    append(".forEach(");
    renderLambda(bodyKind, Arrays.asList(valueType, keyType), Arrays.asList(valueName, keyName), block);
    append(")");
  }

  @Override
  public void renderMethodReference(ExpressionModel expression, String methodName) {
    expression.render(this);
    append('.').append(methodName);
  }

  @Override
  public void renderApiType(TypeInfo.Class.Api apiType) {
    append(apiType.getSimpleName());
  }

  @Override
  public void renderMethodInvocation(ExpressionModel expression, TypeInfo receiverType, MethodRef method, TypeInfo returnType, List<ExpressionModel> argumentModels, List<TypeInfo> argumentTypes) {
    List<TypeInfo> parameterTypes = method.getParameterTypes();
    for (int i = 0;i < parameterTypes.size();i++) {
      TypeInfo parameterType = parameterTypes.get(i);
      TypeInfo argumentType = argumentTypes.get(i);
      if (io.vertx.codetrans.Helper.isHandler(parameterType) && io.vertx.codetrans.Helper.isInstanceOfHandler(argumentType)) {
        ExpressionModel expressionModel = argumentModels.get(i);
        argumentModels.set(i, lang.render(cw -> {
          expressionModel.render(cw);
          cw.append(".handle");
        }));
      }
    }
    super.renderMethodInvocation(expression, receiverType, method, returnType, argumentModels, argumentTypes);
  }

  @Override
  public void renderNew(ExpressionModel expression, TypeInfo type, List<ExpressionModel> argumentModels) {
    append("new (");
    expression.render(this);
    append(")");
    append('(');
    for (int i = 0; i < argumentModels.size(); i++) {
      if (i > 0) {
        append(", ");
      }
      argumentModels.get(i).render(this);
    }
    append(')');
  }
}
