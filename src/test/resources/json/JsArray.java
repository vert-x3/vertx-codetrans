package json;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.codetrans.annotations.CodeTranslate;
import io.vertx.codetrans.JsonTest;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class JsArray {

  @CodeTranslate
  public void instantiate() throws Exception {
    JsonTest.o = new JsonArray();
  }

  @CodeTranslate
  public void addArray() throws Exception {
    JsonTest.o = new JsonArray().add(new JsonArray().add("foo"));
  }

  @CodeTranslate
  public void addBoolean() throws Exception {
    JsonTest.o = new JsonArray().add(true).add(false);
  }

  @CodeTranslate
  public void addNumber() throws Exception {
    JsonTest.o = new JsonArray().add(8080);
  }

  @CodeTranslate
  public void addObject() throws Exception {
    JsonTest.o = new JsonArray().add(new JsonObject().put("foo", "foo_value"));
  }

  @CodeTranslate
  public void addString() throws Exception {
    JsonTest.o = new JsonArray().add("foo");
  }
}
