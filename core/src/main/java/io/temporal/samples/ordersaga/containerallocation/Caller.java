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

package io.temporal.samples.ordersaga.containerallocation;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.samples.ordersaga.TemporalClient;
import io.temporal.samples.ordersaga.dataclasses.SKUQuantity;
import io.temporal.samples.ordersaga.web.ServerInfo;

import javax.net.ssl.SSLException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Caller {

  public static void runWorkflow(int quantity) throws FileNotFoundException, SSLException {
    // generate a random reference number

    // Workflow execution code

    WorkflowClient client = TemporalClient.get();
    final String TASK_QUEUE = ServerInfo.getTaskqueue();

    // get java timestamp
    long javaTime = System.currentTimeMillis();
    long timeSeconds = TimeUnit.MILLISECONDS.toSeconds(javaTime);

    WorkflowOptions options =
        WorkflowOptions.newBuilder()
            .setWorkflowId("Subtract-" + timeSeconds)
            .setTaskQueue(TASK_QUEUE)
            .build();
    ContainerAllocationSAGA workflow = client.newWorkflowStub(ContainerAllocationSAGA.class, options);

    String sku = "sku1001";
    int qty = quantity;

    // start the workflow
    workflow.processOrder("Order-" + timeSeconds, sku, qty);

    System.out.println("Workflow completed");

  }

  @SuppressWarnings("CatchAndPrintStackTrace")
  public static void main(String[] args) throws Exception {
    System.out.println("EXAMPLE: ./gradlew -q execute -PmainClass=io.temporal.samples.ordersaga.containerallocation.Caller -Pquantity=250");

    if (args.length != 1) {
      throw new IllegalArgumentException("Please provide the quantity as a parameter.");
    }

    int quantity;
    try {
      quantity = Integer.parseInt(args[0]);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("The provided quantity is not a valid integer.");
    }

    runWorkflow(quantity);

    System.exit(0);
  }
}
