package io.vertx.codetrans;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class StatementModel extends CodeModel {

  public static StatementModel conditionals(List<ConditionalBlockModel> conditionals, StatementModel otherwise) {
    return ConditionalStructureModel.render((writer) -> {
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

  /**
   * A class marking conditional structures.
   * In some language conditional structure requires specific actions (such as in JavaScript where then ending
   * {@code ;} is not required).
   */
  public static class ConditionalStructureModel extends StatementModel {
    public static ConditionalStructureModel render(Consumer<CodeWriter> c) {
      return new ConditionalStructureModel() {
        @Override
        public void render(CodeWriter writer) {
          c.accept(writer);
        }
      };
    }

    public static StatementModel render(String s) {
      return new ConditionalStructureModel() {
        @Override
        public void render(CodeWriter writer) {
          writer.append(s);
        }
      };
    }

  }
}
