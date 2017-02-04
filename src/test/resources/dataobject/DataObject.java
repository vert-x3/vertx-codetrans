package dataobject;

import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.codetrans.annotations.CodeTranslate;
import io.vertx.codetrans.DataObjectTest;
import io.vertx.core.http.HttpVersion;
import io.vertx.support.KeyStoreOptions;
import io.vertx.support.ServerOptions;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class DataObject {

  @CodeTranslate
  public void empty() throws Exception {
    DataObjectTest.o = new ServerOptions();
  }

  @CodeTranslate
  public void nested() throws Exception {
    DataObjectTest.o = new ServerOptions().setKeyStore(new KeyStoreOptions().setPath("/mystore.jks").setPassword("secret"));
  }

  @CodeTranslate
  public void addToList() throws Exception {
    DataObjectTest.o = new ServerOptions().addEnabledCipherSuite("foo").addEnabledCipherSuite("bar");
  }

  @CodeTranslate
  public void addToMap() throws Exception {
    DataObjectTest.o = new DeliveryOptions().addHeader("foo", "foo_value").addHeader("bar", "bar_value");
  }

  @CodeTranslate
  public void setFromConstructor() throws Exception {
    DataObjectTest.o = new ServerOptions().setPort(8080).setHost("localhost");
  }

  @CodeTranslate
  public void setFromIdentifier() throws Exception {
    ServerOptions obj = new ServerOptions();
    obj.setPort(8080);
    obj.setHost("localhost");
    DataObjectTest.o = obj;
  }

  @CodeTranslate
  public void getFromIdentifier() throws Exception {
    ServerOptions obj = new ServerOptions().setHost("localhost");
    DataObjectTest.o = obj.getHost();
  }

  @CodeTranslate
  public void enumValueFromConstructor() throws Exception {
    ServerOptions obj = new ServerOptions().setProtocolVersion(HttpVersion.HTTP_2);
    DataObjectTest.o = obj;
  }

  @CodeTranslate
  public void enumValueFromIdentifier() throws Exception {
    ServerOptions obj = new ServerOptions();
    obj.setProtocolVersion(HttpVersion.HTTP_2);
    DataObjectTest.o = obj;
  }
}
