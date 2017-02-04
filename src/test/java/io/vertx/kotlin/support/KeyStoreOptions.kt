package io.vertx.kotlin.support

import io.vertx.support.KeyStoreOptions

fun KeyStoreOptions(
  password: String? = null,
  path: String? = null): KeyStoreOptions = io.vertx.support.KeyStoreOptions().apply {
  if (password != null) {
    this.setPassword(password)
  }
  if (path != null) {
    this.setPath(path)
  }
}

