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
  private final Set<String> referencedMethods;

  public VisitContext(CodeBuilder builder) {
    this.builder = builder;
    this.aliases = Collections.emptyMap();
    this.referencedMethods = new LinkedHashSet<>();
  }

  private VisitContext(CodeBuilder builder, Map<Symbol, ExpressionModel> aliases, Set<String> referencedMethods) {
    this.builder = builder;
    this.aliases = aliases;
    this.referencedMethods = referencedMethods;
  }

  public VisitContext putAlias(Symbol symbol, ExpressionModel builder) {
    HashMap<Symbol, ExpressionModel> clone = new HashMap<>(aliases);
    clone.put(symbol, builder);
    return new VisitContext(this.builder, clone, referencedMethods);
  }

  public ExpressionModel getAlias(Symbol symbol) {
    return aliases.get(symbol);
  }

  public Set<String> getReferencedMethods() {
    return referencedMethods;
  }
}
