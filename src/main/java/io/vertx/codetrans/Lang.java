package io.vertx.codetrans;

import com.sun.source.tree.LambdaExpressionTree;
import io.vertx.codegen.TypeInfo;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public interface Lang {

  Script loadScript(ClassLoader loader, String path) throws Exception;

  String getExtension();

  CodeWriter newWriter();

  default ExpressionModel nullLiteral() {
    return new LiteralModel.Null(this);
  }

  default ExpressionModel stringLiteral(String value) {
    return new LiteralModel.String(this, value);
  }

  default ExpressionModel booleanLiteral(String value) {
    return new LiteralModel.Boolean(this, value);
  }

  default ExpressionModel integerLiteral(String value) {
    return new LiteralModel.Integer(this, value);
  }

  default ExpressionModel longLiteral(String value) {
    return new LiteralModel.Long(this, value);
  }

  default ExpressionModel characterLiteral(char value) {
    return new LiteralModel.Character(this, value);
  }

  default ExpressionModel floatLiteral(String value) {
    return new LiteralModel.Float(this, value);
  }

  default ExpressionModel doubleLiteral(String value) {
    return new LiteralModel.Double(this, value);
  }

  default ExpressionModel combine(ExpressionModel left, String op, ExpressionModel right) {
    return new BinaryExpressionModel(this, left, op, right);
  }

  ExpressionModel classExpression(TypeInfo.Class type);

  ExpressionModel asyncResult(String identifier);

  ExpressionModel asyncResultHandler(LambdaExpressionTree.BodyKind bodyKind, TypeInfo.Parameterized resultType, String resultName, CodeModel body);

  default ApiTypeModel apiType(TypeInfo.Class.Api type) {
    return new ApiTypeModel(this, type);
  }

  default EnumExpressionModel enumType(TypeInfo.Class.Enum type) {
    return new EnumExpressionModel(this, type);
  }

  default ExpressionModel variable(TypeInfo type, boolean local, String name) {
    return render(name).as(type);
  }

  StatementModel variableDecl(TypeInfo type, String name, ExpressionModel initializer);

  StatementModel enhancedForLoop(String variableName, ExpressionModel expression, StatementModel body);

  StatementModel forLoop(StatementModel initializer, ExpressionModel condition, ExpressionModel update, StatementModel body);

  default ExpressionModel jsonArrayEncoder(ExpressionModel expression) {
    return render(writer -> {
      writer.renderJsonArrayToString(expression);
    });
  }

  default ExpressionModel jsonObjectEncoder(ExpressionModel expression) {
    return render(writer -> {
      writer.renderJsonObjectToString(expression);
    });
  }

  //

  ExpressionModel console(ExpressionModel expression);

  default ExpressionModel thisModel() {
    return new ThisModel(this);
  }

  default ExpressionModel forNew(Function<List<ExpressionModel>, ExpressionModel> f) {
    Lang lang = this;
    return new ExpressionModel(lang) {
      @Override
      public ExpressionModel onNew(TypeInfo type, List<ExpressionModel> arguments) {
        return f.apply(arguments);
      }
    };
  }

  default ExpressionModel forFieldSelect(String expected, Supplier<ExpressionModel> f) {
    Lang lang = this;
    return new ExpressionModel(lang) {
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

  default ExpressionModel forParenthesized(ExpressionModel expression) {
    return render((renderer) -> {
      renderer.renderParenthesized(expression);
    });
  }

  default ExpressionModel forConditionalExpression(ExpressionModel condition, ExpressionModel trueExpression, ExpressionModel falseExpression) {
    return render((renderer) -> {
      renderer.renderConditionalExpression(condition, trueExpression, falseExpression);
    });
  }

  default ExpressionModel forAssign(ExpressionModel variable, ExpressionModel expression) {
    return render((renderer) -> {
      renderer.renderAssign(variable, expression);
    });
  }

  default ExpressionModel forMethodInvocation(String methodName, Function<List<ExpressionModel>, ExpressionModel> f) {
    String s = methodName;
    Lang lang = this;
    return new ExpressionModel(lang) {
      @Override
      public ExpressionModel onMethodInvocation(TypeInfo receiverType, MethodRef method, TypeInfo returnType, List<ExpressionModel> argumentModels, List<TypeInfo> argumenTypes) {
        if (s.equals(method.getName())) {
          return f.apply(argumentModels);
        } else {
          return super.onMethodInvocation(receiverType, method, returnType, argumentModels, argumenTypes);
        }
      }
    };
  }

  default ExpressionModel forMethodInvocation(BiFunction<String, List<ExpressionModel>, ExpressionModel> f) {
    Lang lang = this;
    return new ExpressionModel(lang) {
      @Override
      public ExpressionModel onMethodInvocation(TypeInfo receiverType, MethodRef method, TypeInfo returnType, List<ExpressionModel> argumentModels, List<TypeInfo> argumenTypes) {
        return f.apply(method.getName(), argumentModels);
      }
    };
  }

  default ExpressionModel render(Consumer<CodeWriter> c) {
    Lang lang = this;
    return new ExpressionModel(lang) {
      @Override
      public void render(CodeWriter writer) {
        c.accept(writer);
      }
    };
  }

  default ExpressionModel render(Supplier<String> f) {
    Lang lang = this;
    return new ExpressionModel(lang) {
      @Override
      public void render(CodeWriter writer) {
        writer.append(f.get());
      }
    };
  }

  default ExpressionModel render(String s) {
    Lang lang = this;
    return new ExpressionModel(lang) {
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
