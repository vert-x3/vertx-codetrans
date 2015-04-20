package json;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.codetrans.annotations.CodeTranslate;
import io.vertx.codetrans.JsonTest;
import io.vertx.support.JsonConverter;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class JsArray {

  @CodeTranslate
  public void instantiate() throws Exception {
    JsonArray arr = new JsonArray();
    JsonTest.o = JsonConverter.toJsonArray(arr);
  }

  @CodeTranslate
  public void addArray() throws Exception {
    JsonArray arr = new JsonArray().add(new JsonArray().add("foo"));
    JsonTest.o = JsonConverter.toJsonArray(arr);
  }

  @CodeTranslate
  public void addBoolean() throws Exception {
    JsonArray arr = new JsonArray().add(true).add(false);
    JsonTest.o = JsonConverter.toJsonArray(arr);
  }

  @CodeTranslate
  public void addNumber() throws Exception {
    JsonArray arr = new JsonArray().add(8080);
    JsonTest.o = JsonConverter.toJsonArray(arr);
  }

  @CodeTranslate
  public void addObject() throws Exception {
    JsonArray arr = new JsonArray().add(new JsonObject().put("foo", "foo_value"));
    JsonTest.o = JsonConverter.toJsonArray(arr);
  }

  @CodeTranslate
  public void addString() throws Exception {
    JsonArray arr = new JsonArray().add("foo");
    JsonTest.o = JsonConverter.toJsonArray(arr);
  }
}
