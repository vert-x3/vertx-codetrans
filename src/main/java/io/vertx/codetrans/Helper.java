package io.vertx.codetrans;

import io.vertx.codegen.type.ApiTypeInfo;
import io.vertx.codegen.type.ParameterizedTypeInfo;
import io.vertx.codegen.type.TypeInfo;
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
    if (type instanceof ParameterizedTypeInfo) {
      ParameterizedTypeInfo parameterizedType = (ParameterizedTypeInfo) type;
      return parameterizedType.getRaw().getName().equals(Handler.class.getName());
    }
    return false;
  }

  public static boolean isInstanceOfHandler(TypeInfo type) {
    if (type instanceof ApiTypeInfo) {
      ApiTypeInfo apiType = (ApiTypeInfo) type;
      return apiType.isHandler();
    }
    return false;
  }
}
