package io.vertx.codetrans.lang.scala;

import com.sun.source.tree.LambdaExpressionTree;
import io.vertx.codegen.type.ApiTypeInfo;
import io.vertx.codegen.type.ClassTypeInfo;
import io.vertx.codegen.type.EnumTypeInfo;
import io.vertx.codegen.type.TypeInfo;
import io.vertx.codetrans.CodeBuilder;
import io.vertx.codetrans.CodeModel;
import io.vertx.codetrans.CodeWriter;
import io.vertx.codetrans.MethodSignature;
import io.vertx.codetrans.expression.*;
import io.vertx.codetrans.statement.StatementModel;

import javax.lang.model.element.TypeElement;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class ScalaCodeWriter extends CodeWriter {


  public ScalaCodeWriter(CodeBuilder builder) {
    super(builder);
  }

  private String capitalize(String string) {
    return string.substring(0,1).toUpperCase()+string.substring(1, string.length());
  }

  @Override
  public void renderNewMap() {
    append("Map()");
  }

  @Override
  public void renderStringLiteral(List<?> parts) {
    if(parts.stream().anyMatch(a -> a instanceof ExpressionModel))
      append("s\"");
    else append("\"");
    parts.stream().forEach(part -> {
    if (part instanceof ExpressionModel) {
      append("${");
      ((ExpressionModel)part).render(this);
      append("}");
    } else {
      renderChars(part.toString());
    }
    });
    append("\"");
  }

  @Override
  public void renderNew(ExpressionModel expression, TypeInfo type, List<ExpressionModel> argumentModels) {
    expression.render(this);
    append('(');
    IntStream.range(0, argumentModels.size()).forEach(i -> {
      if(i > 0) append(", ");
      argumentModels.get(i).render(this);
    });
    append(')');
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
  public void renderAsyncResultValue(TypeInfo resultType, String name){
    append("todo-renderAsyncResultValue");
  }

  @Override
  public void renderAsyncResultSucceeded(TypeInfo resultType, String name){
    append("todo-renderAsyncResultSucceeded");
  }

  @Override
  public void renderAsyncResultFailed(TypeInfo resultType, String name){
    append("todo-renderAsyncResultFailed");
  }

  @Override
  public void renderAsyncResultCause(TypeInfo resultType, String name){
    append("todo-renderAsyncResultCause");
  }


  @Override
  public void renderListGet(ExpressionModel list, ExpressionModel index){
    list.render(this);
    append("(");
    index.render(this);
    append(")");
  }

  @Override
  public void renderMethodReference(ExpressionModel expressionModel, MethodSignature methodSignature){
    expressionModel.render(this);
    append(methodSignature.getName()).append(" _");
  }

  @Override
  public void renderApiType(ApiTypeInfo apiType){
    append(apiType.getSimpleName());
  }

  @Override
  public void renderListLiteral(List<ExpressionModel> arguments){
    append("List(");
    IntStream.range(0, arguments.size()).forEach(i -> {
      if(i > 0) append(", ");
      arguments.get(i).render(this);
    });
    append(')');
  }

  @Override
  public void renderJsonObjectMemberSelect(ExpressionModel expression, String name){
    expression.render(this);
    append('.');
    append(name);
  }

  @Override
  public void renderDataObjectMemberSelect(ExpressionModel expression, String name){
    append("todo-renderDataObjectMemberSelect");
  }

  @Override
  public void renderEnumConstant(EnumTypeInfo type, String constant){
    append(type.getSimpleName()).append('.').append(constant);
  }

  @Override
  public void renderJsonObjectAssign(ExpressionModel expression, String name, ExpressionModel value){
    expression.render(this);
    append(".put(");
    append(name);
    append(", ");
    value.render(this);
    append(")");
  }

  @Override
  public void renderListSize(ExpressionModel list){
    list.render(this);
    append(".size");
  }

  @Override
  public void renderLambda(LambdaExpressionTree.BodyKind bodyKind, List<TypeInfo> parameterTypes, List<String> parameterNames, CodeModel body){
    append("(");
    IntStream.range(0, parameterNames.size()).forEach(i -> {
      if(i > 0) append(", ");
      append(parameterNames.get(i)+": "+parameterTypes.get(i).translateName("scala"));
    });
    append(") => {\n");
    indent();
    body.render(this);
    if (bodyKind == LambdaExpressionTree.BodyKind.EXPRESSION) append("\n");
    unindent();
    append("}");
  }

  @Override
  public void renderMapGet(ExpressionModel map, ExpressionModel key){
    map.render(this);
    append('(');
    key.render(this);
    append(')');
  }

  @Override
  public void renderNewList(){
    append("List()");
  }

  @Override
  public void renderJavaType(ClassTypeInfo apiType){
    append(apiType.getName());
  }

  @Override
  public void renderMapPut(ExpressionModel map, ExpressionModel key, ExpressionModel value){
    map.render(this);
    append(" + (");
    key.render(this);
    append(" -> ");
    value.render(this);
    append(")");
  }

  @Override
  public void renderThrow(String throwableType, ExpressionModel reason){
    if (reason == null) append("throw new ").append(throwableType).append("()");
    else {
      append("throw new ").append(throwableType).append("(");
      reason.render(this);
      append(")");
    }
  }

  @Override
  public void renderMapForEach(ExpressionModel map, String keyName, TypeInfo keyType, String valueName, TypeInfo valueType, LambdaExpressionTree.BodyKind bodyKind, CodeModel block){
    map.render(this);
    append(".foreach(");
    renderLambda(bodyKind, Arrays.asList(keyType, valueType), Arrays.asList(keyName, valueName), block);
    append(")");
  }

  @Override
  public void renderJsonObject(JsonObjectLiteralModel jsonObject){
    append("todo-renderJsonObject");
  }

  @Override
  public void renderTryCatch(StatementModel tryBlock, StatementModel catchBlock){
    append("try {\n");
    indent();
    tryBlock.render(this);
    unindent();
    append("} catch {\n");
    indent();
    append("e:Exception => ");
    catchBlock.render(this);
    unindent();
    append("}\n");
  }

  @Override
  public void renderJsonObjectToString(ExpressionModel expression){
    append("todo-renderJsonObjectToString");
  }

  @Override
  public void renderJsonArray(JsonArrayLiteralModel jsonArray){
    append("todo-renderJsonArray");
  }

  public void renderDataObject(DataObjectLiteralModel model){
    append(model.getType().getSimpleName()+"()");
    boolean dataModelHasMembers = model.getMembers().iterator().hasNext();
    if(dataModelHasMembers) {
      append("\n");
      indent();
    }
    model.getMembers().forEach(member -> {
            append(".set"+capitalize(member.getName())+"(");
    if (member instanceof Member.Single) ((Member.Single)member).getValue().render(this);
    else if (member instanceof Member.Sequence) {
      append("Set(");
      IntStream.range(0, ((Member.Sequence) member).getValues().size()).forEach(i -> {
        if(i > 0)
          append(", ");
        ((Member.Sequence) member).getValues().get(i).render(this);
      });
      append(")");
    }
    else if (member instanceof Member.Entries) append("todo-renderDataObject-entries");
    append(")\n");
    });
    if(dataModelHasMembers) {
      unindent();
    }
  }

  @Override
  public void renderListAdd(ExpressionModel list, ExpressionModel value){
    list.render(this);
    append(" += ");
    value.render(this);
  }

  @Override
  public void renderStatement(StatementModel statement){
    statement.render(this);
    append("\n");
  }

  @Override
  public void renderThis(){
    append("this");
  }

  @Override
  public void renderJsonArrayToString(ExpressionModel expression){
    append("todo-renderJsonArrayToString");
  }

  @Override
  public void renderDataObjectAssign(ExpressionModel expression, String name, ExpressionModel value){
    expression.render(this);
    append(".set"+capitalize(name)+"(");
    value.render(this);
    append(")");
  }

  @Override
  public void renderInstanceOf(ExpressionModel expression, TypeElement type){
    expression.render(this);
    append(".isInstanceOf[");
    append(type.getSimpleName());
    append("]");
  }

  @Override
  public void renderPrefixDecrement(ExpressionModel expression){
    renderPostfixDecrement(expression);
  }

  @Override
  public void renderPrefixIncrement(ExpressionModel expression, CodeWriter writer){
    renderPostfixDecrement(expression);
  }

  @Override
  public void renderPostfixIncrement(ExpressionModel expression){
    expression.render(this);
    append(" += 1");
  }

  @Override
  public void renderPostfixDecrement(ExpressionModel expression){
    expression.render(this);
    append(" -= 1");
  }

  @Override
  public void renderMethodInvocation(ExpressionModel expression, TypeInfo receiverType, MethodSignature method, TypeInfo returnType, List<ExpressionModel> argumentModels, List<TypeInfo> argumentTypes){
    String lbracket = (method.getName() == "onComplete") ? "{" : "(";
    String rbracket = (method.getName() == "onComplete") ? "}" : ")";
    if(method.getName() != "onComplete")
      expression.render(this);
    append('.');
    append(method.getName());
    append(lbracket);

    IntStream.range(0, argumentModels.size()).forEach(i -> {
      if (i > 0) append(", ");
      argumentModels.get(i).render(this);
    });

    append(rbracket);
  }

}
