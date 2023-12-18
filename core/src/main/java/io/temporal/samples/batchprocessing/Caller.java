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

package io.temporal.samples.batchprocessing;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.samples.batchprocessing.web.ServerInfo;
import java.io.FileNotFoundException;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLException;

public class Caller {

  public static String runWorkflow(int numWords) throws FileNotFoundException, SSLException {
    // generate a random reference number

    // Workflow execution code

    WorkflowClient client = TemporalClient.get();
    final String TASK_QUEUE = ServerInfo.getTaskqueue();

    // get java timestamp
    long javaTime = System.currentTimeMillis();
    long timeSeconds = TimeUnit.MILLISECONDS.toSeconds(javaTime);

    WorkflowOptions options =
        WorkflowOptions.newBuilder()
            .setWorkflowId("BatchParentWorkflow-" + timeSeconds)
            .setTaskQueue(TASK_QUEUE)
            .build();
    BatchParentWorkflow transferWorkflow =
        client.newWorkflowStub(BatchParentWorkflow.class, options);

    BatchParentWorkflowParams params = new BatchParentWorkflowParams(numWords, 0);

    WorkflowClient.start(transferWorkflow::processRecords, params);
    System.out.printf("\n\nBatch process requested\n");

    return "OK";
  }

  @SuppressWarnings("CatchAndPrintStackTrace")
  public static void main(String[] args) throws Exception {

    System.out.println("EXAMPLE: ./gradlew -q execute -PmainClass=io.temporal.samples.batchprocessing.Caller -Parg=\"10\"");

    int numWords = args.length > 0 ? Integer.parseInt(args[0]) : 0;

    runWorkflow(numWords);

    System.exit(0);
  }
}
