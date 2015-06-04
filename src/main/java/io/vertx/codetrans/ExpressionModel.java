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
      case JSON_ARRAY:
        return new JsonArrayModel(this);
      case DATA_OBJECT:
        return new DataObjectModel(this);
      case MAP:
        return new MapModel(this);
      default:
        return this;
    }
  }

  public ExpressionModel onMethodInvocation(TypeInfo receiverType, String methodName, TypeInfo returnType, List<TypeInfo> parameterTypes,
                                            List<ExpressionModel> argumentModels, List<TypeInfo> argumenTypes) {
    if (methodName.equals("equals") && argumentModels.size() == 1) {
      return ExpressionModel.render(writer -> {
        writer.renderEquals(ExpressionModel.this, argumentModels.get(0));
      });
    } else {
      return ExpressionModel.render(writer -> {
        writer.renderMethodInvocation(ExpressionModel.this, receiverType, methodName, returnType, parameterTypes,
            argumentModels, argumenTypes);
      });
    }
  }

  public ExpressionModel onField(String identifier) {
    return ExpressionModel.render((renderer) -> {
      renderer.renderMemberSelect(ExpressionModel.this, identifier);
    });
  }

  public ExpressionModel onMethodReference(String methodName) {
    return ExpressionModel.render((renderer) -> {
      renderer.renderMethodReference(ExpressionModel.this, methodName);
    });
  }

  public ExpressionModel onNew(TypeInfo type, List<ExpressionModel> arguments) {
    return ExpressionModel.render((renderer) -> {
      renderer.renderNew(ExpressionModel.this, type, arguments);
    });
  }

  public ExpressionModel onPostFixIncrement() {
    return ExpressionModel.render((renderer) -> {
      renderer.renderPostfixIncrement(ExpressionModel.this);
    });
  }

  public ExpressionModel onPrefixIncrement() {
    return ExpressionModel.render((renderer) -> {
      renderer.renderPrefixIncrement(ExpressionModel.this, renderer);
    });
  }

  public ExpressionModel onPostFixDecrement() {
    return ExpressionModel.render((renderer) -> {
      renderer.renderPostfixDecrement(ExpressionModel.this);
    });
  }

  public ExpressionModel onPrefixDecrement() {
    return ExpressionModel.render((renderer) -> {
      renderer.renderPrefixDecrement(ExpressionModel.this);
    });
  }

  public ExpressionModel onLogicalComplement() {
    return ExpressionModel.render((renderer) -> {
      renderer.renderLogicalComplement(ExpressionModel.this);
    });
  }

  public ExpressionModel unaryMinus() {
    return ExpressionModel.render((renderer) -> {
      renderer.renderUnaryMinus(ExpressionModel.this);
    });
  }

  public ExpressionModel unaryPlus() {
    return ExpressionModel.render((renderer) -> {
      renderer.renderUnaryPlus(ExpressionModel.this);
    });
  }

  public static ExpressionModel forNew(Function<List<ExpressionModel>, ExpressionModel> f) {
    return new ExpressionModel() {
      @Override
      public ExpressionModel onNew(TypeInfo type, List<ExpressionModel> arguments) {
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
      renderer.renderParenthesized(expression);
    });
  }

  public static ExpressionModel forConditionalExpression(ExpressionModel condition, ExpressionModel trueExpression, ExpressionModel falseExpression) {
    return ExpressionModel.render((renderer) -> {
      renderer.renderConditionalExpression(condition, trueExpression, falseExpression);
    });
  }

  public static ExpressionModel forAssign(ExpressionModel variable, ExpressionModel expression) {
    return ExpressionModel.render((renderer) -> {
      renderer.renderAssign(variable, expression);
    });
  }

  public static ExpressionModel forMethodInvocation(String methodName, Function<List<ExpressionModel>, ExpressionModel> f) {
    String s = methodName;
    return new ExpressionModel() {
      @Override
      public ExpressionModel onMethodInvocation(TypeInfo receiverType, String methodName, TypeInfo returnType, List<TypeInfo> parameterTypes, List<ExpressionModel> argumentModels, List<TypeInfo> argumenTypes) {
        if (s.equals(methodName)) {
          return f.apply(argumentModels);
        } else {
          return super.onMethodInvocation(receiverType, methodName, returnType, parameterTypes, argumentModels, argumenTypes);
        }
      }
    };
  }

  public static ExpressionModel forMethodInvocation(BiFunction<String, List<ExpressionModel>, ExpressionModel> f) {
    return new ExpressionModel() {
      @Override
      public ExpressionModel onMethodInvocation(TypeInfo receiverType, String methodName, TypeInfo returnType, List<TypeInfo> parameterTypes, List<ExpressionModel> argumentModels, List<TypeInfo> argumenTypes) {
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
