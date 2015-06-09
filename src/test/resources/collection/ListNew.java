package collection;

import io.vertx.codetrans.annotations.CodeTranslate;
import io.vertx.support.CollectionFactory;
import io.vertx.codetrans.CollectionTest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ListNew {

  @CodeTranslate
  public void newArrayList() throws Exception {
    List list = new ArrayList<>();
    CollectionTest.o = list;
  }
}
