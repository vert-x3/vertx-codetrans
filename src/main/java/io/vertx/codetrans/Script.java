package io.vertx.codetrans;

import java.util.Collections;
import java.util.Map;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public interface Script{

  String getSource();

  default void run() throws Exception {
    run(Collections.emptyMap());
  }

  void run(Map<String, Object> globals) throws Exception;
}
