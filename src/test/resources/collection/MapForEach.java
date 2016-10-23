package collection;

import io.vertx.codetrans.annotations.CodeTranslate;
import io.vertx.support.CollectionFactory;
import io.vertx.codetrans.CollectionTest;

import java.util.Map;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class MapForEach {

  @CodeTranslate
  public void forEach() throws Exception {
    Map<String, String> map = CollectionFactory.createMap();
    map.forEach((key, value) -> {
      CollectionTest.o = key + " -> " + value;
    });
  }
}
