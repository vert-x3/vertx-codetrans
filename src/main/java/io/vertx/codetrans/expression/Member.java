package io.vertx.codetrans.expression;

import java.util.ArrayList;
import java.util.List;

/**
* @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
*/
public abstract class Member {

  final String name;

  protected abstract Member append(ExpressionModel builder);

  public Member(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public static class Single extends Member {

    ExpressionModel value;

    public Single(String name) {
      super(name);
    }

    public ExpressionModel getValue() {
      return value;
    }

    @Override
    protected Member append(ExpressionModel value) {
      this.value = value;
      return this;
    }
  }

  public static class Array extends Member {

    List<ExpressionModel> values = new ArrayList<>();

    public Array(String name) {
      super(name);
    }

    public List<ExpressionModel> getValues() {
      return values;
    }

    @Override
    protected Member append(ExpressionModel value) {
      this.values.add(value);
      return this;
    }
  }
}
