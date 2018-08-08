package io.vertx.support;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.http.HttpVersion;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.*;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@DataObject
public class ServerOptions {

  private String host = "0.0.0.0";
  private int port = 80;
  private KeyStoreOptions keyStore;
  private List<String> enabledCipherSuites = new ArrayList<>();
  private HttpVersion protocolVersion = HttpVersion.HTTP_1_1;
  private Map<String, String> headers = new HashMap<>();

  public ServerOptions() {
  }

  public ServerOptions(JsonObject json) {
    this.host = json.getString("host");
    this.port = json.getInteger("port");
  }

  public String getHost() {
    return host;
  }

  public ServerOptions setHost(String host) {
    this.host = host;
    return this;
  }

  public int getPort() {
    return port;
  }

  public ServerOptions setPort(int port) {
    this.port = port;
    return this;
  }

  public KeyStoreOptions getKeyStore() {
    return keyStore;
  }

  public ServerOptions setKeyStore(KeyStoreOptions keyStore) {
    this.keyStore = keyStore;
    return this;
  }

  public List<String> getEnabledCipherSuites() {
    return enabledCipherSuites;
  }

  public ServerOptions addEnabledCipherSuite(String cipher) {
    enabledCipherSuites.add(cipher);
    return this;
  }

  public ServerOptions setEnabledCipherSuites(List<String> enabledCipherSuites) {
    this.enabledCipherSuites = enabledCipherSuites;
    return this;
  }

  public HttpVersion getProtocolVersion() {
    return protocolVersion;
  }

  public ServerOptions setProtocolVersion(HttpVersion protocolVersion) {
    this.protocolVersion = protocolVersion;
    return this;
  }

  public ServerOptions addHeader(String name, String value) {
    headers.put(name, value);
    return this;
  }

  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject();
    if (host != null) jsonObject.put("host", host);
    jsonObject.put("port", port);
    if (keyStore != null) jsonObject.put("keyStore", keyStore.toJson());
    if (enabledCipherSuites != null && !enabledCipherSuites.isEmpty())
      jsonObject.put("enabledCipherSuites", enabledCipherSuites.stream().<JsonArray>collect(JsonArray::new, JsonArray::add, JsonArray::addAll));
    if (protocolVersion != null) jsonObject.put("protocolVersion", protocolVersion);
    if (headers != null && !headers.isEmpty())
      jsonObject.put("headers", headers.entrySet().stream().<JsonObject>collect(JsonObject::new, (obj, entry) -> obj.put(entry.getKey(), entry.getValue()), JsonObject::mergeIn));
    return jsonObject;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof ServerOptions) {
      ServerOptions that = (ServerOptions) obj;
      return Objects.equals(host, that.host) && port == that.port;
    }
    return false;
  }
}
