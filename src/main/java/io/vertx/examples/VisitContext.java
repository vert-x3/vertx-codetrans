package io.vertx.examples;

import com.sun.tools.javac.code.Symbol;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class VisitContext {

  private final Map<Symbol, ExpressionModel> aliases;

  public VisitContext() {
    aliases = Collections.emptyMap();
  }

  private VisitContext(Map<Symbol, ExpressionModel> aliases) {
    this.aliases = aliases;
  }

  public VisitContext putAlias(Symbol symbol, ExpressionModel builder) {
    HashMap<Symbol, ExpressionModel> clone = new HashMap<>(aliases);
    clone.put(symbol, builder);
    return new VisitContext(clone);
  }

  public ExpressionModel getAlias(Symbol symbol) {
    return aliases.get(symbol);
  }

}
