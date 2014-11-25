package io.vertx.examples;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Helper {

  static String unwrapQuotedString(String s) {
    int len = s.length();
    if (len > 2 && s.charAt(0) == '"' && s.charAt(len - 1) == '"') {
      return s.substring(1, len - 1);
    } else {
      throw new IllegalArgumentException("Illegal quoted string " + s);
    }
  }

  static <E> List<E> append(List<E> list, E last) {
    ArrayList<E> copy = new ArrayList<>(list);
    copy.add(last);
    return copy;
  }
}
