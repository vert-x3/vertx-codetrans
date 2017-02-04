package io.vertx.kotlin.support

import io.vertx.core.http.HttpVersion
import io.vertx.support.ServerOptions

fun ServerOptions(
  host: String? = null,
  port: Int? = null,
  keyStore: io.vertx.support.KeyStoreOptions? = null,
  enabledCipherSuites: Iterable<String>? = null,
  protocolVersion: HttpVersion? = null,
  headers: Map<String, String>? = null): ServerOptions = io.vertx.support.ServerOptions().apply {
  if (enabledCipherSuites != null) {
    for (item in enabledCipherSuites) {
      this.addEnabledCipherSuite(item)
    }
  }
  if (host != null) {
    this.setHost(host)
  }
  if (keyStore != null) {
    this.setKeyStore(keyStore)
  }
  if (port != null) {
    this.setPort(port)
  }
  if (protocolVersion != null) {
    this.setProtocolVersion(protocolVersion);
  }
  if (headers != null) {
    for (item in headers) {
      this.addHeader(item.key, item.value)
    }
  }
}

