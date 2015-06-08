package collection;

import io.vertx.codetrans.annotations.CodeTranslate;
import io.vertx.support.CollectionFactory;
import io.vertx.codetrans.CollectionTest;

import java.util.Map;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class MapPut {

  @CodeTranslate
  public void put() throws Exception {
    Map map = CollectionFactory.wrapMap(CollectionTest.sharedMap);
    map.put("foo", "foo_value");
  }
}
