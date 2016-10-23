package collection;

import io.vertx.codetrans.annotations.CodeTranslate;
import io.vertx.support.CollectionFactory;
import io.vertx.codetrans.CollectionTest;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class MapNew {

  @CodeTranslate
  public void newMap() throws Exception {
    Map hashMap = new HashMap<>();
    CollectionTest.map = hashMap;
  }
}
