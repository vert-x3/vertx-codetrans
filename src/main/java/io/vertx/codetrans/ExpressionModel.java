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

  public ExpressionModel as(TypeInfo type) {
    switch (type.getKind()) {
      case JSON_OBJECT:
        return new JsonObjectModel(this);
      case DATA_OBJECT:
        return new DataObjectModel(this);
      case MAP:
        return new MapModel(this);
      default:
        return this;
    }
  }

  public ExpressionModel onMethodInvocation(String methodName, List<TypeInfo> parameterTypes,
                                            List<ExpressionModel> argumentModels, List<TypeInfo> argumenTypes) {
    if (methodName.equals("equals") && argumentModels.size() == 1) {
      return ExpressionModel.render(writer -> {
        writer.getLang().renderEquals(ExpressionModel.this, argumentModels.get(0), writer);
      });
    } else {
      return ExpressionModel.render(writer -> {
        writer.getLang().renderMethodInvocation(ExpressionModel.this, methodName, parameterTypes,
            argumentModels, argumenTypes, writer);
      });
    }
  }

  public ExpressionModel onField(String identifier) {
    return ExpressionModel.render((renderer) -> {
      renderer.getLang().renderMemberSelect(ExpressionModel.this, identifier, renderer);
    });
  }

  public ExpressionModel onMethodReference(String methodName) {
    return ExpressionModel.render((renderer) -> {
      renderer.getLang().renderMethodReference(ExpressionModel.this, methodName, renderer);
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
      public ExpressionModel onMethodInvocation(String methodName, List<TypeInfo> parameterTypes, List<ExpressionModel> argumentModels, List<TypeInfo> argumenTypes) {
        if (s.equals(methodName)) {
          return f.apply(argumentModels);
        } else {
          return super.onMethodInvocation(methodName, parameterTypes, argumentModels, argumenTypes);
        }
      }
    };
  }

  public static ExpressionModel forMethodInvocation(BiFunction<String, List<ExpressionModel>, ExpressionModel> f) {
    return new ExpressionModel() {
      @Override
      public ExpressionModel onMethodInvocation(String methodName, List<TypeInfo> parameterTypes, List<ExpressionModel> argumentModels, List<TypeInfo> argumenTypes) {
        return f.apply(methodName, argumentModels);
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
