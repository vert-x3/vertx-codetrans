package io.vertx.codetrans.statement;

import io.vertx.codetrans.CodeModel;
import io.vertx.codetrans.CodeWriter;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class StatementModel extends CodeModel {

  /**
   * Creates a {@link StatementModel} for an 'if-then-else' conditional structure.
   * The returned statement is not an {@link Expression}.
   *
   * @param conditionals the conditionals
   * @param otherwise    the 'else' part
   * @return the statement
   */
  public static StatementModel conditionals(List<ConditionalBlockModel> conditionals, StatementModel otherwise) {
    return new StatementModel() {
      @Override
      public void render(CodeWriter writer) {
        writer.renderConditionals(conditionals, otherwise);
      }
    };
  }

  /**
   * Creates a {@link StatementModel} for a conditional structure (for loop, while loop...).
   * The returned statement is not an {@link Expression}.
   *
   * @param c the code of the structure.
   * @return the statement
   */
  public static StatementModel conditional(Consumer<CodeWriter> c) {
    return new StatementModel() {
      @Override
      public void render(CodeWriter writer) {
        c.accept(writer);
      }
    };
  }

  /**
   * Creates an {@link Expression} from the given code.
   *
   * @param c the code
   * @return the statement
   */
  public static StatementModel render(Consumer<CodeWriter> c) {
    return new Expression() {
      @Override
      public void render(CodeWriter writer) {
        c.accept(writer);
      }
    };
  }

  /**
   * Creates an {@link Expression} from the given code.
   *
   * @param s the code
   * @return the statement
   */
  public static StatementModel render(String s) {
    return new Expression() {
      @Override
      public void render(CodeWriter writer) {
        writer.append(s);
      }
    };
  }

  /**
   * Marker class for the _default_ statement model.
   */
  public static class Expression extends StatementModel {

  }

}
