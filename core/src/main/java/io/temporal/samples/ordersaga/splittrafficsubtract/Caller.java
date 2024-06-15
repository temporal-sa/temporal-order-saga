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

package io.temporal.samples.ordersaga.splittrafficsubtract;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.samples.ordersaga.TemporalClient;
import io.temporal.samples.ordersaga.dataclasses.SKUQuantity;
import io.temporal.samples.ordersaga.web.ServerInfo;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLException;

public class Caller {

  public static String runWorkflow() throws FileNotFoundException, SSLException {
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
    SplitTrafficSubtractSAGA workflow = client.newWorkflowStub(SplitTrafficSubtractSAGA.class, options);

    // create a list of SKUs
    List<SKUQuantity> skus = new ArrayList<>();
    skus.add(new SKUQuantity("sku1001", -100));
    skus.add(new SKUQuantity("sku2002", -50));
    skus.add(new SKUQuantity("sku3003", -200));

    double legacyProportion = 0.35;

    // start the workflow
    List<String> result = workflow.processOrder("Order-" + timeSeconds, skus, legacyProportion);

    System.out.println("Workflow completed with result: " + result);

    return result.toString();
  }

  @SuppressWarnings("CatchAndPrintStackTrace")
  public static void main(String[] args) throws Exception {

    System.out.println("EXAMPLE: ./gradlew -q execute -PmainClass=io.temporal.samples.ordersaga.splittrafficsubtract.Caller");

    runWorkflow();

    System.exit(0);
  }
}
