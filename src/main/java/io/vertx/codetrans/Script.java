package io.vertx.codetrans;

import java.util.concurrent.Callable;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public interface Script extends Callable<Void> {

  String getSource();
}
