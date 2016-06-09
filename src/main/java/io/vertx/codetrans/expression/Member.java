package io.vertx.codetrans.expression;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
* @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
*/
public abstract class Member {

  final String name;

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

    protected Member append(ExpressionModel value) {
      this.value = value;
      return this;
    }
  }

  public static class Sequence extends Member {

    List<ExpressionModel> values = new ArrayList<>();

    public Sequence(String name) {
      super(name);
    }

    public List<ExpressionModel> getValues() {
      return values;
    }

    protected Member append(ExpressionModel value) {
      this.values.add(value);
      return this;
    }

    protected Member append(List<ExpressionModel> value) {
      this.values.addAll(value);
      return this;
    }
  }

  public static class Entries extends Member {

    Map<String, List<ExpressionModel>> entries = new LinkedHashMap<>();

    public Entries(String name) {
      super(name);
    }

    protected Member append(String key, ExpressionModel value) {
      entries.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
      return this;
    }

    public Iterable<Member> entries() {
      List<Member> members = new ArrayList<>();
      this.entries.forEach((k, v) -> {
        if (v.size() == 1) {
          members.add(new Single(k).append(v.get(0)));
        } else {
          members.add(new Sequence(k).append(v));
        }
      });
      return members;
    }
  }
}
