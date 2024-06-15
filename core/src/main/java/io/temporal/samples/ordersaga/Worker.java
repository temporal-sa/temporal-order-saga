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

package io.temporal.samples.ordersaga;

import io.temporal.samples.ordersaga.containerallocation.AllocateActivitiesImpl;
import io.temporal.samples.ordersaga.containerallocation.ContainerAllocationSAGAImpl;
import io.temporal.samples.ordersaga.splittrafficsubtract.SubtractActivitiesImpl;
import io.temporal.samples.ordersaga.splittrafficsubtract.SplitTrafficSubtractSAGAImpl;
import io.temporal.samples.ordersaga.web.ServerInfo;
import io.temporal.worker.WorkerFactory;
import io.temporal.worker.WorkerOptions;

public class Worker {

  @SuppressWarnings("CatchAndPrintStackTrace")
  public static void main(String[] args) throws Exception {

    final String TASK_QUEUE = ServerInfo.getTaskqueue();

    // set activities per second across *all* workers
    // prevents resource exhausted errors
    WorkerOptions options =
        WorkerOptions.newBuilder().build();

    // worker factory that can be used to create workers for specific task queues
    WorkerFactory factory = WorkerFactory.newInstance(TemporalClient.get());
    io.temporal.worker.Worker worker = factory.newWorker(TASK_QUEUE, options);
    worker.registerWorkflowImplementationTypes(SplitTrafficSubtractSAGAImpl.class, ContainerAllocationSAGAImpl.class);
    worker.registerActivitiesImplementations(new SubtractActivitiesImpl(), new AllocateActivitiesImpl());

    // Start all workers created by this factory.
    factory.start();
    System.out.println("Worker started for task queue: " + TASK_QUEUE);
  }
}
