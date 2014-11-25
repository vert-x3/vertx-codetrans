package io.vertx.examples;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ExpressionModel extends CodeModel {

  public ExpressionModel onMemberSelect(String identifier) {
    if (identifier.equals("equals")) {
      return ExpressionModel.forMethodInvocation(args -> {
        if (args.size() == 1) {
          return ExpressionModel.render(renderer -> {
            renderer.getLang().renderEquals(ExpressionModel.this, args.get(0), renderer);
          });
        } else {
          throw new UnsupportedOperationException("Not yet implemented"); // Equals overloading
        }
      });
    } else {
      return ExpressionModel.render((renderer) -> {
        renderer.getLang().renderMemberSelect(ExpressionModel.this, identifier, renderer);
      });
    }
  }

  public ExpressionModel onNew(List<ExpressionModel> arguments) {
    throw new UnsupportedOperationException("Not implemented with arguments " + arguments);
  }

  public ExpressionModel onMethodInvocation(List<ExpressionModel> arguments) {
    return ExpressionModel.render((renderer) -> {
      renderer.getLang().renderMethodInvocation(ExpressionModel.this, arguments, renderer);
    });
  }

  public ExpressionModel onPostFixIncrement() {
    return ExpressionModel.render((renderer) -> {
      renderer.getLang().renderPostFixIncrement(ExpressionModel.this, renderer);
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

  public static ExpressionModel forMemberSelect(String expected, Supplier<ExpressionModel> f) {
    return new ExpressionModel() {
      @Override
      public ExpressionModel onMemberSelect(String identifier) {
        if (expected.equals(identifier)) {
          return f.get();
        } else {
          throw new UnsupportedOperationException();
        }
      }
    };
  }

  public static ExpressionModel forMemberSelect(Function<String, ExpressionModel> f) {
    return new ExpressionModel() {
      @Override
      public ExpressionModel onMemberSelect(String identifier) {
        return f.apply(identifier);
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

  public static ExpressionModel forMethodInvocation(Function<List<ExpressionModel>, ExpressionModel> f) {
    return new ExpressionModel() {
      @Override
      public ExpressionModel onMethodInvocation(List<ExpressionModel> arguments) {
        return f.apply(arguments);
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
      public void render(CodeWriter writer) {
        writer.append(s);
      }
    };
  }
}
