package io.vertx.codetrans;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class StatementModel extends CodeModel {

  public static StatementModel ifThenElse(ExpressionModel condition, StatementModel thenBody, StatementModel elseBody) {
    return StatementModel.render((renderer) -> {
      renderer.getLang().renderIfThenElse(condition, thenBody, elseBody, renderer);
    });
  }

  public static StatementModel block(List<StatementModel> statements) {
    return StatementModel.render(renderer -> renderer.getLang().renderBlock(statements, renderer));
  }

  public static StatementModel render(Consumer<CodeWriter> c) {
    return new StatementModel() {
      @Override
      public void render(CodeWriter writer) {
        c.accept(writer);
      }
    };
  }

  public static StatementModel render(String s) {
    return new StatementModel() {
      @Override
      public void render(CodeWriter writer) {
        writer.append(s);
      }
    };
  }
}
