package collection;

import io.vertx.codetrans.annotations.CodeTranslate;
import io.vertx.support.CollectionFactory;
import io.vertx.codetrans.CollectionTest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ListGet {

  @CodeTranslate
  public void get() throws Exception {
    List list = new ArrayList<>();
    list.add("foo");
    CollectionTest.o = list.get(0);
  }
}
