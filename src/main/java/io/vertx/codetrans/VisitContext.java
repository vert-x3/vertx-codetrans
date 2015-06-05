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

  final CodeBuilder builder;
  private final Map<Symbol, ExpressionModel> aliases;
  private final Set<MethodRef> refedMethods;

  public VisitContext(CodeBuilder builder) {
    this.builder = builder;
    this.aliases = Collections.emptyMap();
    this.refedMethods = new LinkedHashSet<>();
  }

  private VisitContext(CodeBuilder builder, Map<Symbol, ExpressionModel> aliases, Set<MethodRef> refedMethods) {
    this.builder = builder;
    this.aliases = aliases;
    this.refedMethods = refedMethods;
  }

  public VisitContext putAlias(Symbol symbol, ExpressionModel builder) {
    HashMap<Symbol, ExpressionModel> clone = new HashMap<>(aliases);
    clone.put(symbol, builder);
    return new VisitContext(this.builder, clone, refedMethods);
  }

  public ExpressionModel getAlias(Symbol symbol) {
    return aliases.get(symbol);
  }

  public Set<MethodRef> getRefedMethods() {
    return refedMethods;
  }
}
