package io.vertx.support;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@DataObject
public class KeyStoreOptions {

  private String path;
  private String password;

  public KeyStoreOptions() {
  }

  public KeyStoreOptions(JsonObject json) {
    path = json.getString("path");
    password = json.getString("password");
  }

  public String getPath() {
    return path;
  }

  public KeyStoreOptions setPath(String path) {
    this.path = path;
    return this;
  }

  public String getPassword() {
    return password;
  }

  public KeyStoreOptions setPassword(String password) {
    this.password = password;
    return this;
  }
}
