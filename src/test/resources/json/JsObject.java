package json;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.codetrans.annotations.CodeTranslate;
import io.vertx.codetrans.JsonTest;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class JsObject {

  @CodeTranslate
  public void instantiate() throws Exception {
    JsonTest.o = new JsonObject();
  }

  @CodeTranslate
  public void putArray() throws Exception {
    JsonTest.o = new JsonObject().put("nested", new JsonArray().add("foo"));
  }

  @CodeTranslate
  public void putBoolean() throws Exception {
    JsonTest.o = new JsonObject().put("_true", true).put("_false", false);
  }

  @CodeTranslate
  public void putNumber() throws Exception {
    JsonTest.o = new JsonObject().put("port", 8080);
  }

  @CodeTranslate
  public void putObject() throws Exception {
    JsonTest.o = new JsonObject().put("nested", new JsonObject().put("foo", "bar"));
  }

  @CodeTranslate
  public void putString() throws Exception {
    JsonTest.o = new JsonObject().put("foo", "foo_value");
  }
}
