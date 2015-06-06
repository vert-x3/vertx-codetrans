package io.vertx.codetrans;

import com.sun.source.tree.LambdaExpressionTree;
import io.vertx.codegen.TypeInfo;

import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public abstract class CodeWriter implements Appendable {

  protected final CodeBuilder builder;
  private int indent = 0;
  private boolean first = true;
  private StringBuilder buffer = new StringBuilder();

  public CodeWriter(CodeBuilder builder) {
    this.builder = builder;
  }

  public CodeBuilder getBuilder() {
    return builder;
  }

  public CodeWriter indent() {
    indent += 2;
    return this;
  }

  public CodeWriter unindent() {
    if (indent < 2) {
      throw new IllegalStateException();
    }
    indent -= 2;
    return this;
  }

  public StringBuilder getBuffer() {
    return buffer;
  }

  @Override
  public CodeWriter append(CharSequence csq) {
    return append(csq, 0, csq.length());
  }

  @Override
  public CodeWriter append(CharSequence csq, int start, int end) {
    while (start < end) {
      append(csq.charAt(start++));
    }
    return this;
  }

  @Override
  public CodeWriter append(char c) {
    if (c == '\n') {
      first = true;
    } else if (first) {
      first = false;
      for (int i = 0;i < indent;i++) {
        buffer.append(' ');
      }
    }
    buffer.append(c);
    return this;
  }

  public void renderChars(String value) {
    for (int i = 0;i < value.length();i++) {
      char c = value.charAt(i);
      switch (c) {
        case '\b':
          append("\\b");
          break;
        case '\f':
          append("\\f");
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

  public void renderConditionals(List<ConditionalBlockModel> conditionals, StatementModel otherwise) {
    for (int i = 0;i < conditionals.size();i++) {
      ConditionalBlockModel conditional = conditionals.get(i);
      append(i == 0 ? "if " : " else if ");
      conditional.condition.render(this);
      append(" {\n");
      indent();
      conditional.body.render(this);
      unindent();
      append("}");
    }
    if (otherwise != null) {
      append(" else {\n");
      indent();
      otherwise.render(this);
      unindent();
      append("}");
    }
  }

  public void renderParenthesized(ExpressionModel expression) {
    append('(');
    expression.render(this);
    append(')');
  }

  public void renderEquals(ExpressionModel expression, ExpressionModel arg) {
    expression.render(this);
    append(" == ");
    arg.render(this);
  }

  public void renderConditionalExpression(ExpressionModel condition, ExpressionModel trueExpression, ExpressionModel falseExpression) {
    condition.render(this);
    append(" ? ");
    trueExpression.render(this);
    append(" : ");
    falseExpression.render(this);
  }

  public void renderAssign(ExpressionModel variable, ExpressionModel expression) {
    variable.render(this);
    append(" = ");
    expression.render(this);
  }

  public void renderIdentifier(String name, IdentifierKind kind) {
    append(name);
  }

  public abstract void renderStatement(StatementModel statement);

  public void renderBlock(BlockModel block) {
    block.render(this);
  }

  public void renderMemberSelect(ExpressionModel expression, String identifier) {
    expression.render(this);
    append('.').append(identifier);
  }

  public abstract void renderMethodReference(ExpressionModel expression, String methodName);

  public abstract void renderNew(ExpressionModel expression, TypeInfo type, List<ExpressionModel> argumentModels);

  public void renderMethodInvocation(ExpressionModel expression,
                                     TypeInfo receiverType,
                                     MethodSignature method,
                                     TypeInfo returnType,
                                     List<ExpressionModel> argumentModels,
                                     List<TypeInfo> argumentTypes) {
    expression.render(this); // ?
    append('.'); // ?
    append(method.getName());
    append('(');
    for (int i = 0; i < argumentModels.size(); i++) {
      if (i > 0) {
        append(", ");
      }
      argumentModels.get(i).render(this);
    }
    append(')');
  }

  public void renderBinary(BinaryExpressionModel expression) {
    expression.left.render(this);
    append(" ").append(expression.op).append(" ");
    expression.right.render(this);
  }

  public void renderNullLiteral() {
    append("null");
  }

  public void renderStringLiteral(String value) {
    append('"');
    renderChars(value);
    append('"');
  }

  public void renderCharLiteral(char value) {
    append('\'');
    renderChars(Character.toString(value));
    append('\'');
  }

  public void renderFloatLiteral(String value) {
    renderChars(value);
  }

  public void renderDoubleLiteral(String value) {
    renderChars(value);
  }

  public void renderBooleanLiteral(String value) {
    append(value);
  }

  public void renderLongLiteral(String value) {
    renderChars(value);
  }

  public void renderIntegerLiteral(String value) {
    append(value);
  }

  public void renderPostfixIncrement(ExpressionModel expression) {
    expression.render(this);
    append("++");
  }

  public void renderPrefixIncrement(ExpressionModel expression, CodeWriter writer) {
    writer.append("++");
    expression.render(writer);
  }

  public void renderPostfixDecrement(ExpressionModel expression) {
    expression.render(this);
    append("--");
  }

  public void renderPrefixDecrement(ExpressionModel expression) {
    append("--");
    expression.render(this);
  }

  public void renderLogicalComplement(ExpressionModel expression) {
    append("!");
    expression.render(this);
  }

  public void renderUnaryMinus(ExpressionModel expression) {
    append("-");
    expression.render(this);
  }

  public void renderUnaryPlus(ExpressionModel expression) {
    append("+");
    expression.render(this);
  }

  public abstract void renderMapGet(ExpressionModel map, ExpressionModel key);

  public abstract void renderMapPut(ExpressionModel map, ExpressionModel key, ExpressionModel value);

  public abstract void renderMapForEach(ExpressionModel map,
                                        String keyName, TypeInfo keyType,
                                        String valueName, TypeInfo valueType,
                                        LambdaExpressionTree.BodyKind bodyKind, CodeModel block);

  public abstract void renderJsonObject(JsonObjectLiteralModel jsonObject);

  public abstract void renderJsonArray(JsonArrayLiteralModel jsonArray);

  public abstract void renderDataObject(DataObjectLiteralModel model);

  public abstract void renderJsonObjectAssign(ExpressionModel expression, String name, ExpressionModel value);

  public abstract void renderDataObjectAssign(ExpressionModel expression, String name, ExpressionModel value);

  public abstract void renderJsonObjectToString(ExpressionModel expression);

  public abstract void renderJsonArrayToString(ExpressionModel expression);

  public void renderJsonArrayAdd(ExpressionModel expression,ExpressionModel value) {
    throw new UnsupportedOperationException("todo");
  }

  public abstract void renderJsonObjectMemberSelect(ExpressionModel expression, String name);

  public abstract void renderDataObjectMemberSelect(ExpressionModel expression, String name);

  public void renderJsonArrayGet(ExpressionModel expression, ExpressionModel index) {
    expression.render(this);
    append('[');
    index.render(this);
    append(']');
  }

  public abstract void renderNewMap();

  public abstract void renderAsyncResultSucceeded(String name);

  public abstract void renderAsyncResultFailed(String name);

  public abstract void renderAsyncResultCause(String name);

  public abstract void renderAsyncResultValue(String name);

  public abstract void renderLambda(LambdaExpressionTree.BodyKind bodyKind, List<TypeInfo> parameterTypes, List<String> parameterNames, CodeModel body);

  public abstract void renderEnumConstant(TypeInfo.Class.Enum type, String constant);

  public abstract void renderThrow(String throwableType, ExpressionModel reason);

  public abstract void renderThis();

  public abstract void renderApiType(TypeInfo.Class.Api apiType);

  public abstract void renderJavaType(TypeInfo.Class apiType);

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
        append(multiline ? "/*" : "//");
      }
      @Override
      public void onEndComment(boolean multiline) {
        if (multiline) {
          append("*/");
        }
      }
    };
    renderer.parse(fragment);
  }
}
