package io.vertx.examples;

import java.util.ArrayList;
import java.util.List;

/**
* @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
*/
public abstract class Member {

  final ExpressionModel name;

  protected abstract Member append(ExpressionModel builder);

  public Member(ExpressionModel name) {
    this.name = name;
  }

  public static class Single extends Member {

    ExpressionModel value;

    public Single(ExpressionModel name) {
      super(name);
    }

    @Override
    protected Member append(ExpressionModel value) {
      this.value = value;
      return this;
    }
  }

  public static class Array extends Member {

    List<ExpressionModel> values = new ArrayList<>();

    public Array(ExpressionModel name) {
      super(name);
    }

    @Override
    protected Member append(ExpressionModel value) {
      this.values.add(value);
      return this;
    }
  }
}
