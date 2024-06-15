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

import io.temporal.activity.ActivityOptions;
import io.temporal.failure.ApplicationFailure;
import io.temporal.workflow.Saga;
import io.temporal.workflow.Workflow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

public class ContainerAllocationSAGAImpl implements ContainerAllocationSAGA {

    private static final Logger logger = LoggerFactory.getLogger(ContainerAllocationSAGAImpl.class);

    private final ActivityOptions options = ActivityOptions.newBuilder()
            .setStartToCloseTimeout(Duration.ofSeconds(2))
            .build();

    private final AllocateActivities activities = Workflow.newActivityStub(AllocateActivities.class, options);

    @Override
    public void processOrder(String orderId, String sku, int qty) {
        Saga.Options options = new Saga.Options.Builder()
                .setParallelCompensation(false)
                .build();
        Saga saga = new Saga(options);

        List<String> containers = activities.getContainerRecommendation(sku, qty);
        int quantityLeft = qty;

        logger.info("SKU: {}, Quantity to allocate: {}", sku, qty);
        logger.info("Containers: {}", containers);

        for (String container : containers) {
            logger.info("Allocating to container: {}", container);

            // Allocate and update quantity left
            quantityLeft = activities.allocate(container, sku, quantityLeft);

            // Add compensation for the allocation
            int finalQuantityLeft = quantityLeft; // Needed because of lambda
            saga.addCompensation(() -> activities.allocateReverse(container, sku, finalQuantityLeft));

            logger.info("Quantity left after allocation: {}", quantityLeft);

            if(quantityLeft == 0) {
                break; // All quantity allocated
            }
        }

        if (quantityLeft > 0) {
            logger.error("Failed to allocate all quantity for SKU: {}. Quantity remaining: {}", sku, quantityLeft);
            logger.error("Compensating...");
            saga.compensate(); // compensate all allocations
            throw ApplicationFailure.newFailure(
                    "Failed to allocate all quantity for SKU: " + sku + ". Quantity remaining:" + quantityLeft,
                    "AllocationFailed");
        }
    }
}