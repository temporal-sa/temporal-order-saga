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

import io.temporal.activity.ActivityOptions;
import io.temporal.samples.ordersaga.dataclasses.SKUQuantity;
import io.temporal.samples.ordersaga.dataclasses.SplitSKUTraffic;
import io.temporal.workflow.Async;
import io.temporal.workflow.Promise;
import io.temporal.workflow.Workflow;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.temporal.workflow.Saga;

public class SplitTrafficSubtractSAGAImpl implements SplitTrafficSubtractSAGA {

    private static final Logger logger = LoggerFactory.getLogger(SplitTrafficSubtractSAGAImpl.class);

    private final ActivityOptions options = ActivityOptions.newBuilder()
            .setStartToCloseTimeout(Duration.ofSeconds(2))
            .build();

    private final SubtractActivities activities = Workflow.newActivityStub(SubtractActivities.class, options);

    @Override
    public List<String> processOrder(String orderId, List<SKUQuantity> skus, double legacyProportion) {
        Saga.Options options = new Saga.Options.Builder()
                .setParallelCompensation(true)
                .build();
        Saga saga = new Saga(options);

        try {

            SplitSKUTraffic splitSKUTraffic = activities.splitTrafficBySKU(skus, legacyProportion);
            logger.info("Split traffic into legacy and new SKUs: {}", splitSKUTraffic);

            // Asynchronous activities
            Promise<String> subtractUsingLegacyPromise = Async.function(activities::subtractUsingLegacy, splitSKUTraffic.getLegacySKUs());
            saga.addCompensation(() -> activities.subtractUsingLegacyReverse(splitSKUTraffic.getLegacySKUs()));

            Promise<String> subtractUsingNewPromise = Async.function(activities::subtractUsingNew, splitSKUTraffic.getNewSKUs());
            saga.addCompensation(() -> activities.subtractUsingNewReverse(splitSKUTraffic.getNewSKUs()));

            // Wait for both activities to complete
            Promise.allOf(subtractUsingLegacyPromise, subtractUsingNewPromise).get();

            // Handle the results if necessary
            String legacyResult = subtractUsingLegacyPromise.get();
            String newResult = subtractUsingNewPromise.get();

            System.out.println("Result from subtractUsingLegacy: " + legacyResult);
            System.out.println("Result from subtractUsingNew: " + newResult);

            // Combine and return the results
            List<String> results = new ArrayList<>();
            results.add(legacyResult);
            results.add(newResult);
            return results;

        } catch (Exception e) {
            saga.compensate();
            throw Workflow.wrap(e);
        }
    }
}