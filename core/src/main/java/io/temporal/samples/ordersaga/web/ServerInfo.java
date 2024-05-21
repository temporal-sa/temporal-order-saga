/*
 *  Copyright (c) 2020 Temporal Technologies, Inc. All Rights Reserved
 *
 *  Copyright 2012-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 *  Modifications copyright (C) 2017 Uber Technologies, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"). You may not
 *  use this file except in compliance with the License. A copy of the License is
 *  located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 *  or in the "license" file accompanying this file. This file is distributed on
 *  an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 *  express or implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 */

package io.temporal.samples.ordersaga.web;

import java.util.HashMap;
import java.util.Map;

public class ServerInfo {

  public static String getCertPath() {
    return System.getenv("TEMPORAL_CERT_PATH") != null ? System.getenv("TEMPORAL_CERT_PATH") : "";
  }

  public static String getKeyPath() {
    return System.getenv("TEMPORAL_KEY_PATH") != null ? System.getenv("TEMPORAL_KEY_PATH") : "";
  }

  public static String getNamespace() {
    String namespace = System.getenv("TEMPORAL_NAMESPACE");
    return namespace != null && !namespace.isEmpty() ? namespace : "default";
  }

  public static String getAddress() {
    String address = System.getenv("TEMPORAL_ADDRESS");
    return address != null && !address.isEmpty() ? address : "localhost:7233";
  }

  public static String getTaskqueue() {
    String taskqueue = System.getenv("TEMPORAL_ORDERSAGA_TASKQUEUE");
    return taskqueue != null && !taskqueue.isEmpty() ? taskqueue : "OrderWorkflowSagaTaskQueue";
  }

  public static String getWebServerURL() {
    String webServerURL = System.getenv("TEMPORAL_JAVA_WEB_SERVER_URL");
    return webServerURL != null && !webServerURL.isEmpty() ? webServerURL : "http://localhost:7070";
  }

  public static Map<String, String> getServerInfo() {
    Map<String, String> info = new HashMap<>();
    info.put("certPath", getCertPath());
    info.put("keyPath", getKeyPath());
    info.put("namespace", getNamespace());
    info.put("address", getAddress());
    info.put("taskQueue", getTaskqueue());
    return info;
  }
}
