package io.vertx.codetrans;

import io.vertx.codegen.TypeInfo;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ExpressionModel extends CodeModel {

  public ExpressionModel onMethodInvocation(TypeInfo returnType, String methodName, List<ExpressionModel> arguments) {
    if (methodName.equals("equals") && arguments.size() == 1) {
      return ExpressionModel.render(writer -> {
        writer.getLang().renderEquals(ExpressionModel.this, arguments.get(0), writer);
      });
    } else {
      return ExpressionModel.render(writer -> {
        writer.getLang().renderMethodInvocation(ExpressionModel.this, methodName, arguments, writer);
      });
    }
  }

  public ExpressionModel onField(String identifier) {
    return ExpressionModel.render((renderer) -> {
      renderer.getLang().renderMemberSelect(ExpressionModel.this, identifier, renderer);
    });
  }

  public ExpressionModel onNew(List<ExpressionModel> arguments) {
    throw unsupported(" with arguments " + arguments);
  }

  public ExpressionModel onPostFixIncrement() {
    return ExpressionModel.render((renderer) -> {
      renderer.getLang().renderPostfixIncrement(ExpressionModel.this, renderer);
    });
  }

  public ExpressionModel onPrefixIncrement() {
    return ExpressionModel.render((renderer) -> {
      renderer.getLang().renderPrefixIncrement(ExpressionModel.this, renderer);
    });
  }

  public ExpressionModel onPostFixDecrement() {
    return ExpressionModel.render((renderer) -> {
      renderer.getLang().renderPostfixDecrement(ExpressionModel.this, renderer);
    });
  }

  public ExpressionModel onPrefixDecrement() {
    return ExpressionModel.render((renderer) -> {
      renderer.getLang().renderPrefixDecrement(ExpressionModel.this, renderer);
    });
  }

  public ExpressionModel onLogicalComplement() {
    return ExpressionModel.render((renderer) -> {
      renderer.getLang().renderLogicalComplement(ExpressionModel.this, renderer);
    });
  }

  public ExpressionModel unaryMinus() {
    return ExpressionModel.render((renderer) -> {
      renderer.getLang().renderUnaryMinus(ExpressionModel.this, renderer);
    });
  }

  public ExpressionModel unaryPlus() {
    return ExpressionModel.render((renderer) -> {
      renderer.getLang().renderUnaryPlus(ExpressionModel.this, renderer);
    });
  }

  public static ExpressionModel forNew(Function<List<ExpressionModel>, ExpressionModel> f) {
    return new ExpressionModel() {
      @Override
      public ExpressionModel onNew(List<ExpressionModel> arguments) {
        return f.apply(arguments);
      }
    };
  }

  public static ExpressionModel forFieldSelect(String expected, Supplier<ExpressionModel> f) {
    return new ExpressionModel() {
      @Override
      public ExpressionModel onField(String identifier) {
        if (expected.equals(identifier)) {
          return f.get();
        } else {
          throw unsupported();
        }
      }
    };
  }

  public static ExpressionModel forParenthesized(ExpressionModel expression) {
    return ExpressionModel.render((renderer) -> {
      renderer.getLang().renderParenthesized(expression, renderer);
    });
  }

  public static ExpressionModel forConditionalExpression(ExpressionModel condition, ExpressionModel trueExpression, ExpressionModel falseExpression) {
    return ExpressionModel.render((renderer) -> {
      renderer.getLang().renderConditionalExpression(condition, trueExpression, falseExpression, renderer);
    });
  }

  public static ExpressionModel forAssign(ExpressionModel variable, ExpressionModel expression) {
    return ExpressionModel.render((renderer) -> {
      renderer.getLang().renderAssign(variable, expression, renderer);
    });
  }

  public static ExpressionModel forMethodInvocation(String methodName, Function<List<ExpressionModel>, ExpressionModel> f) {
    String s = methodName;
    return new ExpressionModel() {
      @Override
      public ExpressionModel onMethodInvocation(TypeInfo returnType, String methodName, List<ExpressionModel> arguments) {
        if (s.equals(methodName)) {
          return f.apply(arguments);
        } else {
          return super.onMethodInvocation(returnType, methodName, arguments);
        }
      }
    };
  }

  public static ExpressionModel forMethodInvocation(BiFunction<String, List<ExpressionModel>, ExpressionModel> f) {
    return new ExpressionModel() {
      @Override
      public ExpressionModel onMethodInvocation(TypeInfo returnType, String methodName, List<ExpressionModel> arguments) {
        return f.apply(methodName, arguments);
      }
    };
  }

  public static ExpressionModel render(Consumer<CodeWriter> c) {
    return new ExpressionModel() {
      @Override
      public void render(CodeWriter writer) {
        c.accept(writer);
      }
    };
  }

  public static ExpressionModel render(Supplier<String> f) {
    return new ExpressionModel() {
      @Override
      public void render(CodeWriter writer) {
        writer.append(f.get());
      }
    };
  }

  public static ExpressionModel render(String s) {
    return new ExpressionModel() {
      @Override
      public String render(Lang lang) {
        return s;
      }
      @Override
      public void render(CodeWriter writer) {
        writer.append(s);
      }
    };
  }
}
