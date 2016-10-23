package collection;

import io.vertx.codetrans.annotations.CodeTranslate;
import io.vertx.support.CollectionFactory;
import io.vertx.codetrans.CollectionTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ListApi {

  @CodeTranslate
  public void add() throws Exception {
    List<String> list = new ArrayList<>();
    list.add("foo");
    CollectionTest.o = list;
  }

  @CodeTranslate
  public void get() throws Exception {
    List<String> list = new ArrayList<>();
    list.add("foo");
    CollectionTest.o = list.get(0);
  }

  @CodeTranslate
  public void newArrayList() throws Exception {
    List<String> list = new ArrayList<>();
    CollectionTest.o = list;
  }

  @CodeTranslate
  public void size() throws Exception {
    List<String> list = new ArrayList<>();
    list.add("foo");
    CollectionTest.o = list.size();
  }

  @CodeTranslate
  public void asList() throws Exception {
    CollectionTest.o = Arrays.asList("foo", "bar", "juu");
  }
}
