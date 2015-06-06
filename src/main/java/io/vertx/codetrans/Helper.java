package io.vertx.codetrans;

import io.vertx.codegen.TypeInfo;
import io.vertx.codetrans.expression.BinaryExpressionModel;
import io.vertx.codetrans.expression.ExpressionModel;
import io.vertx.codetrans.expression.LiteralModel;
import io.vertx.core.Handler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Helper {

  public static String unwrapQuotedString(String s) {
    int len = s.length();
    if (len > 2 && s.charAt(0) == '"' && s.charAt(len - 1) == '"') {
      return s.substring(1, len - 1);
    } else {
      throw new IllegalArgumentException("Illegal quoted string " + s);
    }
  }

  public static <E> List<E> append(List<E> list, E last) {
    ArrayList<E> copy = new ArrayList<>(list);
    copy.add(last);
    return copy;
  }

  public static boolean isString(ExpressionModel expression) {
    if (expression instanceof LiteralModel.String) {
      return true;
    } else if (expression instanceof BinaryExpressionModel) {
      BinaryExpressionModel binary = (BinaryExpressionModel) expression;
      return binary.getOp().equals("+") && (isString(binary.getLeft()) || isString(binary.getRight()));
    }
    return false;
  }

  private static boolean string;

  /**
   * Render an interpolated string.
   *
   * @param expression the binary string expression
   * @param writer the writer
   */
  public static void renderInterpolatedString(BinaryExpressionModel expression, CodeWriter writer,
                                       String beginInterpolation, String endInterpolation) {
    boolean prev = string;
    if (!string) {
      string = true;
      writer.append('"');
    }

    renderInterpolatedString(expression.getLeft(), writer, beginInterpolation, endInterpolation);
    renderInterpolatedString(expression.getRight(), writer, beginInterpolation, endInterpolation);
    if (!prev) {
      writer.append('"');
    }
    string = prev;
  }

  private static void renderInterpolatedString(ExpressionModel expression, CodeWriter writer,
                                               String beginInterpolation, String endInterpolation) {
    if (expression instanceof LiteralModel.String) {
      LiteralModel.String string = (LiteralModel.String) expression;
      writer.renderChars(string.getValue());
    } else if (Helper.isString(expression)) {
      expression.render(writer);
    } else {
      boolean prev = string;
      if (string) {
        string = false;
        writer.append(beginInterpolation);
      }
      expression.render(writer);
      if (prev) {
        writer.append(endInterpolation);
      }
      string = prev;
    }
  }

  public static boolean isHandler(TypeInfo type) {
    if (type instanceof TypeInfo.Parameterized) {
      TypeInfo.Parameterized parameterizedType = (TypeInfo.Parameterized) type;
      return parameterizedType.getRaw().getName().equals(Handler.class.getName());
    }
    return false;
  }

  public static boolean isInstanceOfHandler(TypeInfo type) {
    if (type instanceof TypeInfo.Class.Api) {
      TypeInfo.Class.Api apiType = (TypeInfo.Class.Api) type;
      return apiType.isHandler();
    }
    return false;
  }
}
