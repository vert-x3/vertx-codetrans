package io.vertx.codetrans.lang.kotlin;

import com.sun.source.tree.LambdaExpressionTree;
import io.vertx.codegen.type.*;
import io.vertx.codetrans.*;
import io.vertx.codetrans.expression.*;
import io.vertx.codegen.type.ApiTypeInfo;
import io.vertx.codegen.type.ClassTypeInfo;
import io.vertx.codegen.type.EnumTypeInfo;
import io.vertx.codegen.type.TypeInfo;
import io.vertx.codegen.type.VoidTypeInfo;
import io.vertx.codetrans.CodeBuilder;
import io.vertx.codetrans.CodeModel;
import io.vertx.codetrans.CodeWriter;
import io.vertx.codetrans.MethodSignature;
import io.vertx.codetrans.TypeArg;
import io.vertx.codetrans.expression.*;
import io.vertx.codetrans.statement.StatementModel;

import javax.lang.model.element.TypeElement;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Sergey Mashkov
 */
public class KotlinCodeWriter extends CodeWriter {

  private static final Map<String, String> BASIC_TYPES = new HashMap<>();

  static {
    BASIC_TYPES.put(java.lang.Byte.class.getName(), "Byte");
    BASIC_TYPES.put(byte.class.getName(), "Byte");
    BASIC_TYPES.put(java.lang.Short.class.getName(), "Short");
    BASIC_TYPES.put(short.class.getName(), "Short");
    BASIC_TYPES.put(java.lang.Integer.class.getName(), "Int");
    BASIC_TYPES.put(int.class.getName(), "Int");
    BASIC_TYPES.put(java.lang.Long.class.getName(), "Long");
    BASIC_TYPES.put(long.class.getName(), "Long");
    BASIC_TYPES.put(java.lang.Float.class.getName(), "Float");
    BASIC_TYPES.put(float.class.getName(), "Float");
    BASIC_TYPES.put(java.lang.Double.class.getName(), "Double");
    BASIC_TYPES.put(double.class.getName(), "Double");
    BASIC_TYPES.put(java.lang.Character.class.getName(), "Char");
    BASIC_TYPES.put(char.class.getName(), "Char");
    BASIC_TYPES.put(java.lang.Boolean.class.getName(), "Boolean");
    BASIC_TYPES.put(boolean.class.getName(), "Boolean");
  }

  private int jsonLevel = 0;

  public KotlinCodeWriter(CodeBuilder builder) {
    super(builder);
  }

  @Override
  public KotlinCodeBuilder getBuilder() {
    return (KotlinCodeBuilder) super.getBuilder();
  }

  @Override
  public void renderStringLiteral(List<?> parts) {
    append('"');
    for (Object part : parts) {
      if (part instanceof ExpressionModel) {
        append("${");
        ((ExpressionModel) part).render(this);
        append("}");
      } else {
        renderChars(part.toString());
      }
    }
    append('"');
  }

  public void renderChars(String value) {
    for (int i = 0; i < value.length(); i++) {
      char c = value.charAt(i);
      switch (c) {
        case '\b':
          append("\\b");
          break;
        case '\f':
          append("\\u000c");
          break;
        case '\n':
          append("\\n");
          break;
        case '\t':
          append("\\t");
          break;
        case '\r':
          append("\\r");
          break;
        case '"':
          append("\\\"");
          break;
        case '\\':
          append("\\\\");
          break;
        case '$':
          append("\\$");
        default:
          if (c < 32 || c > 126) {
            String s = Integer.toHexString(c).toUpperCase();
            while (s.length() < 4) {
              s = "0" + s;
            }
            append("\\u").append(s);
          } else {
            append(c);
          }
      }
    }
  }

  @Override
  public void renderNewList() {
    append("mutableListOf<Any?>()");
  }

  @Override
  public void renderNewMap() {
    append("mutableMapOf<String, Any?>()");
  }

  @Override
  public void renderThis() {
    append("this");
  }

  @Override
  public void renderMethodReference(ExpressionModel expression, MethodSignature signature) {
    append("{ ");
    List<ExpressionModel> arguments = new ArrayList<>();

    if (!signature.getParameterTypes().isEmpty()) {
      for (int i = 0, m = signature.getParameterTypes().size(); i < m; ++i) {
        String name;
        if (m == 1) {
          name = "it";
        } else {
          name = "p" + Integer.toString(i);
        }

        arguments.add(new IdentifierModel(builder, name, VariableScope.VARIABLE));
      }

      if (arguments.size() > 1) {
        for (int i = 0, m = arguments.size(); i < m; ++i) {
          if (i > 0) {
            append(", ");
          }

          append(((IdentifierModel) arguments.get(i)).name);
        }

        append(" -> ");
      }
    }

    renderMethodInvocation(expression, VoidTypeInfo.INSTANCE, signature, VoidTypeInfo.INSTANCE, Collections.emptyList(), arguments, Collections.emptyList());
    append(" }");
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
  }

  @Override
  public void renderBinary(BinaryExpressionModel expression) {
    expression.getLeft().render(this);
    append(" ");

    switch (expression.getOp()) {
      case "&":
        append("and");
        break;
      case "|":
        append("or");
        break;
      case "^":
        append("xor");
        break;
      default:
        append(expression.getOp());
    }

    append(" ");
    expression.getRight().render(this);
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
    append("} catch(e: Exception) {\n");
    indent();
    catchBlock.render(this);
    unindent();
    append("}\n");
  }

  @Override
  public void renderThrow(String throwableType, ExpressionModel reason) {
    append("throw ");
    append(throwableType);
    append("(");

    if (reason != null) {
      reason.render(this);
    }

    append(")");
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
  public void renderLambda(LambdaExpressionTree.BodyKind bodyKind, List<TypeInfo> parameterTypes, List<String> parameterNames, CodeModel body) {
    append("{");
    if (!parameterNames.isEmpty()) {
      for (int i = 0; i < parameterNames.size(); i++) {
        if (i == 0) {
          append(" ");
        } else {
          append(", ");
        }
        append(parameterNames.get(i));
      }
      append(" ->\n");
    } else {
      append("\n");
    }
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
    switch (javaType.getKind()) {
      case STRING:
        append("String");
        break;
      case VOID:
        append("Unit");
        break;
      case BOXED_PRIMITIVE:
        renderBasicType(javaType);
        break;
      default:
        append(javaType.getName());
    }
  }

  private static final Set<String> reservedWords = new HashSet<>(Arrays.asList("object", "class"));

  @Override
  public void renderIdentifier(String name, VariableScope scope) {
    if (reservedWords.contains(name)) {
      append("`");
      append(name);
      append("`");
    } else {
      append(name);
    }
  }

  public void renderBasicType(TypeInfo type) {
    append(BASIC_TYPES.getOrDefault(type.getName(), type.getName()));
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
  public void renderListAdd(ExpressionModel list, ExpressionModel value) {
    list.render(this);
    append(".add(");
    value.render(this);
    append(")");
  }

  @Override
  public void renderListSize(ExpressionModel list) {
    list.render(this);
    append(".size");
  }

  @Override
  public void renderListGet(ExpressionModel list, ExpressionModel index) {
    list.render(this);
    append("[");
    index.render(this);
    append("]");
  }

  @Override
  public void renderMapGet(ExpressionModel map, ExpressionModel key) {
    map.render(this);
    append("[");
    key.render(this);
    append("]");
  }

  @Override
  public void renderMapPut(ExpressionModel map, ExpressionModel key, ExpressionModel value) {
    map.render(this);
    append("[");
    key.render(this);
    append("] = ");
    value.render(this);
  }

  @Override
  public void renderMapForEach(ExpressionModel map, String keyName, TypeInfo keyType, String valueName, TypeInfo valueType, LambdaExpressionTree.BodyKind bodyKind, CodeModel block) {
    append("for ((").append(keyName).append(", ").append(valueName).append(") in ");
    map.render(this);
    append(") {\n");
    indent();

    block.render(this);
    if (bodyKind == LambdaExpressionTree.BodyKind.EXPRESSION) {
      append("\n");
    }

    unindent();
    append("}\n");
  }

  @Override
  public void renderNew(ExpressionModel expression, TypeInfo type, List<ExpressionModel> argumentModels) {
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

  @Override
  public void renderInstanceOf(ExpressionModel expression, TypeElement type) {
    expression.render(this);
    append(" is ");
    append(type.getQualifiedName());
  }

  @Override
  public void renderListLiteral(List<ExpressionModel> arguments) {
    append("listOf(");
    for (int i = 0; i < arguments.size(); ++i) {
      if (i > 0) {
        append(", ");
      }

      arguments.get(i).render(this);
    }
    append(")");
  }

  @Override
  public void renderNewArray(String primitiveType, List<ExpressionModel> arguments) {
    if (primitiveType != null) {
      append(primitiveType).append("ArrayOf(");
    } else {
      append("arrayOf(");
    }
    for (int i = 0; i < arguments.size(); ++i) {
      if (i > 0) {
        append(", ");
      }

      arguments.get(i).render(this);
    }
    append(")");
  }

  @Override
  public void renderJsonArrayToString(ExpressionModel expression) {
    expression.render(this);
    append(".toString()");
  }

  @Override
  public void renderJsonObjectToString(ExpressionModel expression) {
    expression.render(this);
    append(".toString()");
  }

  @Override
  public void renderJsonArray(JsonArrayLiteralModel jsonArray) {
    renderJsonArray(jsonArray.getValues());
  }

  @Override
  public void renderJsonArrayAdd(ExpressionModel expression, ExpressionModel value) {
    expression.render(this);

    if (value instanceof NullLiteralModel) {
      append(".addNull()");
    } else {
      append(".add(");
      value.render(this);
      append(")");
    }
  }

  private void jsonEnter() {
    if (jsonLevel == 0) {
      append("json {\n");
      indent();
    }
    jsonLevel++;
  }

  private void jsonLeave() {
    jsonLevel--;
    if (jsonLevel == 0) {
      unindent();
      append("\n}");
    }
  }

  @Override
  public void renderJsonArrayGet(ExpressionModel expression, Class<?> type, ExpressionModel index) {
    expression.render(this);
    append(".");
    if (type == Object.class) {
      append("get<Any?>(");
    } else {
      append("get");
      append(type.getSimpleName());
    }
    append("(");
    index.render(this);
    append(')');
  }

  private void renderJsonArray(List<ExpressionModel> entries) {
    jsonEnter();
    append("array(");

    for (int i = 0; i < entries.size(); ++i) {
      if (i > 0) {
        append(", ");
      }

      entries.get(i).render(this);
    }

    append(")");
    jsonLeave();
  }

  @Override
  public void renderJsonObject(JsonObjectLiteralModel jsonObject) {
    jsonEnter();

    renderMapStructure("obj", jsonObject.getMembers());

    jsonLeave();
  }

  @Override
  public void renderJsonObjectAssign(ExpressionModel expression, String name, ExpressionModel value) {
    ArrayList<ExpressionModel> args = new ArrayList<>();
    args.add(new StringLiteralModel(getBuilder(), name));

    if (value instanceof NullLiteralModel) {
      renderMethodInvocation(expression, VoidTypeInfo.INSTANCE, new MethodSignature("putNull", Collections.emptyList(), false, VoidTypeInfo.INSTANCE), VoidTypeInfo.INSTANCE, Collections.emptyList(), args, Collections.emptyList());
    } else {
      args.add(value);
      renderMethodInvocation(expression, VoidTypeInfo.INSTANCE, new MethodSignature("put", Collections.emptyList(), false, VoidTypeInfo.INSTANCE), VoidTypeInfo.INSTANCE, Collections.emptyList(), args, Collections.emptyList());
    }
  }

  @Override
  public void renderMethodInvocation(ExpressionModel expression, TypeInfo receiverType, MethodSignature method, TypeInfo returnType, List<TypeArg> typeArguments, List<ExpressionModel> argumentModels, List<TypeInfo> argumentTypes) {
    if (!(expression instanceof ThisModel)) {
      expression.render(this);
      append('.');
    }
    renderIdentifier(method.getName(), VariableScope.FIELD);
    if (typeArguments.size() > 0) {
      boolean needed = typeArguments.stream().filter(typeArg -> typeArg == null || !typeArg.resolved).count() > 0;
      if (needed) {
        append('<');
        append(typeArguments.stream().map(ti -> {
          if (ti != null) {
            return ti.value.getSimpleName();
          } else {
            return "Any";
          }
        }).collect(Collectors.joining(", ")));
        append('>');
      }
    }
    append('(');
    for (int i = 0; i < argumentModels.size(); i++) {
      if (i > 0) {
        append(", ");
      }
      argumentModels.get(i).render(this);
    }
    append(')');
  }

  @Override
  public void renderJsonObjectMemberSelect(ExpressionModel expression, Class<?> type, String name) {
    expression.render(this);
    append(".");
    if (type == Object.class) {
      append("get<Any?>");
    } else {
      append("get");
      append(type.getSimpleName());
    }
    append("(");
    renderStringLiteral(name);
    append(")");
  }

  @Override
  public void renderToDataObject(JsonObjectModel model, ClassTypeInfo type) {
    append(type.getSimpleName());
    append("(");
    model.render(this);
    append(")");

  }

  @Override
  public void renderDataObject(DataObjectLiteralModel model) {
    append(model.getType().getSimpleName());
    append("(");
    if (model.getMembers().iterator().hasNext()) {
      append("\n");
      indent();
      int index = 0;
      for (Member m : model.getMembers()) {
        if (index > 0) {
          append(",\n");
        }
        append(m.getName()).append(" = ");
        renderMember(m);
        index++;
      }
      unindent();
    }
    append(")");
  }

  @Override
  public void renderDataObjectToJson(IdentifierModel model) {
    model.render(this);
    append(".toJson()");
  }

  @Override
  public void renderDataObjectAssign(ExpressionModel expression, String name, ExpressionModel value) {
    renderDataObjectMemberSelect(expression, name);
    append(" = ");
    value.render(this);
  }

  @Override
  public void renderDataObjectMemberSelect(ExpressionModel expression, String name) {
    expression.render(this);
    append(".");
    renderIdentifier(name, VariableScope.FIELD);
  }

  @Override
  public void renderJsonObjectSize(ExpressionModel expression) {
    expression.render(this);
    append(".size()");
  }

  @Override
  public void renderJsonArraySize(ExpressionModel expression) {
    expression.render(this);
    append(".size()");
  }

  @Override
  public void renderMemberSelect(ExpressionModel expression, String identifier) {
    expression.render(this);
    append('.');
    renderIdentifier(identifier, VariableScope.FIELD);
  }

  private void renderMapStructure(String builderFunctionName, Iterable<Member> members) {
    List<Member> membersList = new ArrayList<>();
    members.forEach(membersList::add);
    boolean feedLine = membersList.size() > 1;

    append(builderFunctionName);
    append("(");
    if (feedLine) {
      append("\n");
    }
    indent();

    int i = 0;
    for (Member m : membersList) {
      if (i > 0) {
        append(",");
        if (feedLine) {
          append("\n");
        }
      }

      renderStringLiteral(m.getName());
      append(" to ");
      renderMember(m);

      i++;
    }

    unindent();
    if (feedLine) {
      append("\n");
    }
    append(")");
  }

  private void renderMap(Iterable<Member> members) {
    renderMapStructure("mapOf", members);
  }

  private void renderMember(Member m) {
    if (m instanceof Member.Single) {
      ((Member.Single) m).getValue().render(this);
    } else if (m instanceof Member.Sequence) {
      renderListLiteral(((Member.Sequence) m).getValues());
    } else if (m instanceof Member.Entries) {
      renderMap(((Member.Entries) m).entries());
    }
  }
}
