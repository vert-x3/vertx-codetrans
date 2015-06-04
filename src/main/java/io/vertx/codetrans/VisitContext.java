package io.vertx.codetrans;

import com.sun.tools.javac.code.Symbol;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class VisitContext {

  private final Map<Symbol, ExpressionModel> aliases;
  private final Set<MethodRef> refedMethods;

  public VisitContext() {
    aliases = Collections.emptyMap();
    refedMethods = new LinkedHashSet<>();
  }

  private VisitContext(Map<Symbol, ExpressionModel> aliases, Set<MethodRef> refedMethods) {
    this.aliases = aliases;
    this.refedMethods = refedMethods;
  }

  public VisitContext putAlias(Symbol symbol, ExpressionModel builder) {
    HashMap<Symbol, ExpressionModel> clone = new HashMap<>(aliases);
    clone.put(symbol, builder);
    return new VisitContext(clone, refedMethods);
  }

  public ExpressionModel getAlias(Symbol symbol) {
    return aliases.get(symbol);
  }

  public Set<MethodRef> getRefedMethods() {
    return refedMethods;
  }
}
