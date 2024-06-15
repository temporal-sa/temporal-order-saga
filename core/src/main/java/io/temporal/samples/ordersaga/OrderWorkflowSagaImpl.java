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

import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;
import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.temporal.workflow.Saga;
import io.temporal.common.RetryOptions;

public class OrderWorkflowSagaImpl implements OrderWorkflowSaga {

    private static final Logger logger = LoggerFactory.getLogger(OrderWorkflowSagaImpl.class);

    private final ActivityOptions options = ActivityOptions.newBuilder()
            .setStartToCloseTimeout(Duration.ofSeconds(10))
            .build();

  private final OrderActivities activities = Workflow.newActivityStub(OrderActivities.class, options);

  @Override
    public void processOrder(String orderId) {
        Saga saga = new Saga(new Saga.Options.Builder().build());
        try {
          // Step 1: Process payment
          saga.addCompensation(() -> activities.refundPayment(orderId));
          activities.processPayment(orderId);

          // Step 2: Reserve inventory
          saga.addCompensation(() -> activities.restockInventory(orderId));
          activities.reserveInventory(orderId);

          // Step 3: Deliver order
          saga.addCompensation(() -> activities.cancelDelivery(orderId));
          activities.deliverOrder(orderId);

        } catch (Exception e) {
          logger.error("Order processing failed, compensating.", e);
          saga.compensate();
          throw Workflow.wrap(e); // Wraps the exception to make it serializable by Temporal
        }
    }

}
