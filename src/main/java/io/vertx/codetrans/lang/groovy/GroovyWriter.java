package io.vertx.codetrans.lang.groovy;

import com.sun.source.tree.LambdaExpressionTree;
import io.vertx.codegen.type.ApiTypeInfo;
import io.vertx.codegen.type.ClassTypeInfo;
import io.vertx.codegen.type.EnumTypeInfo;
import io.vertx.codegen.type.TypeInfo;
import io.vertx.codetrans.CodeModel;
import io.vertx.codetrans.CodeWriter;
import io.vertx.codetrans.expression.DataObjectLiteralModel;
import io.vertx.codetrans.expression.ExpressionModel;
import io.vertx.codetrans.expression.JsonArrayLiteralModel;
import io.vertx.codetrans.expression.JsonObjectLiteralModel;
import io.vertx.codetrans.expression.Member;
import io.vertx.codetrans.statement.StatementModel;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
* @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
*/
class GroovyWriter extends CodeWriter {

  final GroovyCodeBuilder builder;

  GroovyWriter(GroovyCodeBuilder builder) {
    super(builder);
    this.builder = builder;
  }

  @Override
  public void renderStringLiteral(List<?> parts) {
    append('"');
    for (Object part : parts) {
      if (part instanceof ExpressionModel) {
        append("${");
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
  public void renderStatement(StatementModel statement) {
    statement.render(this);
    append("\n");
  }

  @Override
  public void renderTryCatch(StatementModel tryBlock, StatementModel catchBlock) {
    append("try {\n");
    indent();
    tryBlock.render(this);
    unindent();
    append("} catch(Exception e) {\n");
    indent();
    catchBlock.render(this);
    unindent();
    append("}\n");
  }

  @Override
  public void renderLongLiteral(String value) {
    renderChars(value);
    append('L');
  }

  @Override
  public void renderFloatLiteral(String value) {
    renderChars(value);
    append('f');
  }

  @Override
  public void renderDoubleLiteral(String value) {
    renderChars(value);
    append('d');
  }

  @Override
  public void renderThis() {
    append("this");
  }

  @Override
  public void renderLambda(LambdaExpressionTree.BodyKind bodyKind, List<TypeInfo> parameterTypes, List<String> parameterNames, CodeModel body) {
    append("{");
    for (int i = 0; i < parameterNames.size(); i++) {
      if (i == 0) {
        append(" ");
      } else {
        append(", ");
      }
      append(parameterNames.get(i));
    }
    append(" ->\n");
    indent();
    body.render(this);
    if (bodyKind == LambdaExpressionTree.BodyKind.EXPRESSION) {
      append("\n");
    }
    unindent();
    append("}");
  }

  @Override
  public void renderApiType(ApiTypeInfo apiType) {
    append(apiType.getSimpleName());
  }

  @Override
  public void renderJavaType(ClassTypeInfo javaType) {
    append(javaType.getName());
  }

  @Override
  public void renderAsyncResultSucceeded(TypeInfo resultType, String name) {
    append(name).append(".succeeded()");
  }

  @Override
  public void renderAsyncResultFailed(TypeInfo resultType, String name) {
    append(name).append(".failed()");
  }

  @Override
  public void renderAsyncResultCause(TypeInfo resultType, String name) {
    append(name).append(".cause()");
  }

  @Override
  public void renderAsyncResultValue(TypeInfo resultType, String name) {
    append(name).append(".result()");
  }

  @Override
  public void renderEnumConstant(EnumTypeInfo type, String constant) {
    append(type.getSimpleName()).append('.').append(constant);
  }

  @Override
  public void renderThrow(String throwableType, ExpressionModel reason) {
    if (reason == null) {
      append("throw new ").append(throwableType).append("()");
    } else {
      append("throw new ").append(throwableType).append("(");
      reason.render(this);
      append(")");
    }
  }

  @Override
  public void renderSystemOutPrintln(ExpressionModel expression) {
    append("println(");
    expression.render(this);
    append(")");
  }

  @Override
  public void renderSystemErrPrintln(ExpressionModel expression) {
    append("System.err.println(");
    expression.render(this);
    append(")");
  }

  @Override
  public void renderNewMap() {
    append("[:]");
  }

  @Override
  public void renderNewList() {
    append("[]");
  }

  public void renderDataObject(DataObjectLiteralModel model) {
    renderJsonObject(model.getMembers());
  }

  public void renderJsonObject(JsonObjectLiteralModel jsonObject) {
    renderJsonObject(jsonObject.getMembers());
  }

  public void renderJsonArray(JsonArrayLiteralModel jsonArray) {
    renderJsonArray(jsonArray.getValues());
  }

  private boolean isValidMapLiteralKey(String s) {
    int len = s.length();
    if (len == 0) {
      return false;
    }
    if (!Character.isJavaIdentifierStart(s.charAt(0))) {
      return false;
    }
    for (int i = 1;i < len;i++) {
      if (!Character.isJavaIdentifierPart(s.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  private void renderJsonObject(Iterable<Member> members) {
    Iterator<Member> iterator = members.iterator();
    if (iterator.hasNext()) {
      append("[\n").indent();
      while (iterator.hasNext()) {
        Member member = iterator.next();
        String name = member.getName();
        if (isValidMapLiteralKey(name)) {
          append(name);
        } else {
          append("'");
          renderChars(name);
          append("'");
        }
        append(":");
        if (member instanceof Member.Single) {
          ((Member.Single) member).getValue().render(this);
        } else if (member instanceof Member.Sequence) {
          renderJsonArray(((Member.Sequence) member).getValues());
        } else if (member instanceof Member.Entries) {
          renderJsonObject(((Member.Entries) member).entries());
        }
        if (iterator.hasNext()) {
          append(',');
        }
        append('\n');
      }
      unindent().append("]");
    } else {
      append("[:]");
    }
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
  public void renderJsonObjectAssign(ExpressionModel expression, String name, ExpressionModel value) {
    expression.render(this);
    append('.');
    append(name);
    append(" = ");
    value.render(this);
  }

  @Override
  public void renderJsonArrayAdd(ExpressionModel expression, ExpressionModel value) {
    expression.render(this);
    append(".add(");
    value.render(this);
    append(")");
  }

  @Override
  public void renderDataObjectAssign(ExpressionModel expression, String name, ExpressionModel value) {
    renderJsonObjectAssign(expression, name, value);
  }

  @Override
  public void renderJsonObjectMemberSelect(ExpressionModel expression, String name) {
    expression.render(this);
    append('.');
    append(name);
  }

  @Override
  public void renderJsonObjectToString(ExpressionModel expression) {
    append("groovy.json.JsonOutput.toJson(");
    expression.render(this);
    append(")");
  }

  @Override
  public void renderJsonArrayToString(ExpressionModel expression) {
    append("groovy.json.JsonOutput.toJson(");
    expression.render(this);
    append(")");
  }

  @Override
  public void renderDataObjectMemberSelect(ExpressionModel expression, String name) {
    renderJsonObjectMemberSelect(expression, name);
  }

  @Override
  public void renderListAdd(ExpressionModel list, ExpressionModel value) {
    list.render(this);
    append(".add(");
    value.render(this);
    append(")");
  }

  @Override
  public void renderListSize(ExpressionModel list) {
    list.render(this);
    append(".size()");
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
    append(".each ");
    renderLambda(bodyKind, Arrays.asList(keyType, valueType), Arrays.asList(keyName, valueName), block);
  }

  @Override
  public void renderMethodReference(ExpressionModel expression, String methodName) {
    expression.render(this);
    append(".&").append(methodName);
  }

  @Override
  public void renderNew(ExpressionModel expression, TypeInfo type, List<ExpressionModel> argumentModels) {
    append("new ");
    expression.render(this);
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
