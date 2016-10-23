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
  public void getOnVariable() throws Exception {
    Map<String, String> createdMap = CollectionFactory.createMap();
    Object result = createdMap.get("foo");
    CollectionTest.o = result;
  }

  @CodeTranslate
  public void getOnMethodReturn() throws Exception {
    Object result = CollectionFactory.createMap().get("foo");
    CollectionTest.o = result;
  }
}
