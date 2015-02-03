package collection;

import io.vertx.codetrans.annotations.CodeTranslate;
import io.vertx.support.CollectionFactory;
import io.vertx.codetrans.CollectionTest;

import java.util.Map;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class MapGet {

  @CodeTranslate
  public void start() throws Exception {
    Map map = CollectionFactory.createMap();
    Object result = map.get("foo");
    CollectionTest.o = result;
  }
}
