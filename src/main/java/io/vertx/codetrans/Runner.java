/*
 * Copyright 2014 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package io.vertx.codetrans;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Runner {

  private static final String LANG = System.getProperty("lang", "groovy");
  private static Vertx vertx = Vertx.vertx();
  private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

  public static void main(String[] args) throws Exception {
    ArrayList<Method> examples = new ArrayList<>();
    for (Method m : Runner.class.getDeclaredMethods()) {
      int mods = m.getModifiers();
      if (Modifier.isPublic(mods) && Modifier.isStatic(mods) && m.getParameterTypes().length == 0) {
        examples.add(m);
      }
    }
    examples.sort((m1, m2) -> m1.getName().compareTo(m2.getName()));
    System.out.println(
        "##########################\n" +
            "# Vert.x examples runner #\n" +
            "##########################\n");
    while (true) {
      Method example;
      if (args.length > 0 && args[0] != null) {
        example = null;
        for (Method m : examples) {
          if (m.getName().equals(args[0].trim())) {
            example = m;
          }
        }
        args = new String[0]; // So we run other examples
      } else {
        System.out.println("Choose an example:");
        int index = 1;
        for (Method m : examples) {
          System.out.println((index++) + ":" + m.getName());
        }
        System.out.print("> ");
        String s = reader.readLine();
        try {
          index = Integer.parseInt(s) - 1;
          if (index >= 0 && index < examples.size()) {
            example = examples.get(index);
          } else {
            System.out.println("Invalid example <" + s + ">");
            continue;
          }
        } catch (NumberFormatException ignore) {
          System.out.println("Invalid example <" + s + ">");
          continue;
        }
      }
      run(example);
    }
  }

  private static void run(Method m) throws Exception {
    m.invoke(null);
  }

  public static void echo() {
    deployInLang("echo/EchoServer", "echo/EchoClient");
  }

  public static void eventbus_pointtopoint() {
    deployInLang("eventbus_pointtopoint/Receiver", "eventbus_pointtopoint/Sender");
  }

  public static void eventbus_pubsub() {
    deployInLang("eventbus_pubsub/Receiver", "eventbus_pubsub/Sender");
  }

  public static void eventbusbridge() {
    deployInLang("eventbusbridge/BridgeServer");
  }

  public static void fanout() {
    deployInLang("fanout/FanoutServer");
  }

  public static void http() {
    deployInLang("http/Server", "http/Client");
  }

  public static void https() {
    deployInLang("https/Server", "https/Client");
  }

  public static void proxy() {
    deployInLang("proxy/Server", "proxy/Proxy", "proxy/Client");
  }

  public static void route_matcher() {
    deployInLang("route_match/RouteMatchServer");
  }

  public static void sendfile() {
    deployInLang("sendfile/SendFile");
  }

  public static void simpleform() {
    deployInLang("simpleform/SimpleFormServer");
  }

  public static void simpleformupload() {
    deployInLang("simpleformupload/SimpleFormUploadServer");
  }

  public static void sockjs() {
    deployInLang("sockjs/SockJSExample");
  }

  public static void ssl() {
    deployInLang("ssl/Server", "ssl/Client");
  }

  public static void upload() {
    deployInLang("upload/Server", "upload/Client");
  }

  public static void websockets() {
    deployInLang("websockets/WebSocketsServer", "websockets/WebSocketsClient");
  }

  private static void deployInLang(String... verticles) {
    verticles = verticles.clone();
    for (int i = 0;i < verticles.length;i++) {
      verticles[i] = LANG + ':' + verticles[i] + '.' + LANG;
    }
    deploy(verticles);
  }

  public static void deploy(String... verticles) {
    BlockingQueue<AsyncResult<String[]>> queue = new ArrayBlockingQueue<>(1);
    deploy(Arrays.asList(verticles), new String[0], queue::add);
    AsyncResult<String[]> result;
    try {
      result = queue.take();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      return;
    }
    if (result.succeeded()) {
      System.out.println("Press a key after run...");
      try {
        System.in.read();
      } catch (IOException e) {
      }
      CountDownLatch done = new CountDownLatch(1);
      undeploy(result.result(), d -> {
        done.countDown();
      });
      try {
        done.await();
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        return;
      }
    } else {
      System.out.println("Failed to deploy:");
      result.cause().printStackTrace();
    }
  }

  /**
   * Deploy the provided verticles sequentially.
   *
   * @param verticles the verticles to deploy
   * @param previousDepl the previous deployments
   * @param resultHandler the result handler
   */
  private static void deploy(List<String> verticles, String[] previousDepl, Handler<AsyncResult<String[]>> resultHandler) {
    if (verticles.size() > 0) {
      String verticle = verticles.get(0);
      vertx.deployVerticle(verticle, result -> {
        if (result.succeeded()) {
          System.out.println("Deployed: " + verticle + " as " + result.result());
          String[] nextDepl = Arrays.copyOf(previousDepl, previousDepl.length + 1);
          nextDepl[previousDepl.length] = result.result();
          deploy(verticles.subList(1, verticles.size()), nextDepl, resultHandler);
        } else {
          undeploy(previousDepl, done -> {
            resultHandler.handle(Future.failedFuture(result.cause()));
          });
        }
      });
    } else {
      resultHandler.handle(Future.succeededFuture(previousDepl));
    }
  }

  private static void undeploy(String[] deployments, Handler<Void> doneHandler) {
    if (deployments.length == 0) {
      doneHandler.handle(null);
    } else {
      String[] next = Arrays.copyOf(deployments, deployments.length - 1);
      vertx.undeploy(deployments[deployments.length - 1], result -> {
        undeploy(next, doneHandler);
      });
    }
  }
}
