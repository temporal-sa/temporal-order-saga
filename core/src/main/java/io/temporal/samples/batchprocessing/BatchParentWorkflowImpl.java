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

import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Async;
import io.temporal.workflow.Promise;
import io.temporal.workflow.Workflow;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BatchParentWorkflowImpl implements BatchParentWorkflow {

  private final ActivityOptions options =
      ActivityOptions.newBuilder().setStartToCloseTimeout(Duration.ofSeconds(60)).build();
  private static final Logger log = LoggerFactory.getLogger(BatchParentWorkflowImpl.class);
  private final BatchActivities activities =
      Workflow.newActivityStub(BatchActivities.class, options);

  // process x records in a single batch (parallel activity executions)
  private static final int BATCH_SIZE = 50;

  // run up to x child workflows in parallel
  private static final int WINDOW_SIZE = 4;

  // continue as new every x workflow executions (to keep event history size small)
  private static final int CONTINUE_AS_NEW_THRESHOLD = 500;

  @Override
  public void processRecords(BatchParentWorkflowParams params) {
    List<Promise<Void>> activeWorkflows = new ArrayList<>();
    int processedWorkflows = 0;
    int currentOffset = params.getOffset();

    while (true) {
      // Get a batch of records to process
      List<String> batch = activities.createSingleBatch(BATCH_SIZE,
              params.getNumWords(),
              currentOffset);

      // If no more records to process, exit the loop
      if (batch.isEmpty()) {
        break; // Exit the loop if the batch is empty (no more records to process)
      }

      // Ensures that no more than WINDOW_SIZE child workflows are running in parallel
      if (activeWorkflows.size() >= WINDOW_SIZE) {
        Workflow.await(() -> activeWorkflows.stream().anyMatch(Promise::isCompleted));
        activeWorkflows.removeIf(Promise::isCompleted);
      }

      // Process the batch in a child workflow
      BatchChildWorkflow childWorkflow = Workflow.newChildWorkflowStub(BatchChildWorkflow.class);
      Promise<Void> workflowExecution = Async.procedure(childWorkflow::processBatch, batch);
      activeWorkflows.add(workflowExecution);
      processedWorkflows++;

      currentOffset += BATCH_SIZE;

      // Avoid filling up the event history by continuing as new every CONTINUE_AS_NEW_THRESHOLD
      if (processedWorkflows >= CONTINUE_AS_NEW_THRESHOLD) {
        // Wait for all active child workflows to complete
        Workflow.await(() -> activeWorkflows.stream().allMatch(Promise::isCompleted));

        log.info("Processed {} workflows, continuing as new", processedWorkflows);

        // Continue as new from currentOffset (records already processed)
        params.setOffset(currentOffset);
        Workflow.continueAsNew(params);
        return;
      }

    }

    Workflow.await(() -> activeWorkflows.stream().allMatch(Promise::isCompleted));
  }
}
