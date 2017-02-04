package io.vertx.support;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.http.HttpVersion;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

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

  public ServerOptions() {
  }

  public ServerOptions(JsonObject json) {
    host = json.getString("host");
    port = json.getInteger("port");
    keyStore = json.containsKey("keyStore") ? new KeyStoreOptions(json.getJsonObject("keyStore")) : null;
    protocolVersion = json.containsKey("protocolVersion") ? HttpVersion.valueOf(json.getString("protocolVersion")) : null;
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
}
