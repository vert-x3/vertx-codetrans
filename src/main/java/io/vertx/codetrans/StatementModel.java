package io.vertx.codetrans;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class StatementModel extends CodeModel {

  public static StatementModel conditionals(List<ConditionalBlockModel> conditionals, StatementModel otherwise) {
    return StatementModel.render((writer) -> {
      writer.getLang().renderConditionals(conditionals, otherwise, writer);
    });
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
