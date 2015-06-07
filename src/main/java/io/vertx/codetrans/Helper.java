package io.vertx.codetrans;

import io.vertx.codegen.TypeInfo;
import io.vertx.core.Handler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Helper {

  public static <E> List<E> append(List<E> list, E last) {
    ArrayList<E> copy = new ArrayList<>(list);
    copy.add(last);
    return copy;
  }

  public static boolean isHandler(TypeInfo type) {
    if (type instanceof TypeInfo.Parameterized) {
      TypeInfo.Parameterized parameterizedType = (TypeInfo.Parameterized) type;
      return parameterizedType.getRaw().getName().equals(Handler.class.getName());
    }
    return false;
  }

  public static boolean isInstanceOfHandler(TypeInfo type) {
    if (type instanceof TypeInfo.Class.Api) {
      TypeInfo.Class.Api apiType = (TypeInfo.Class.Api) type;
      return apiType.isHandler();
    }
    return false;
  }
}
