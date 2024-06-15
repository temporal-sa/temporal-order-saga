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

import io.temporal.failure.ApplicationFailure;

public class OrderActivitiesImpl implements OrderActivities {

    @Override
    public void processPayment(String orderId) {
        System.out.println("Processing payment for order: " + orderId);
        sleep(5); // simulate API call processing time
        // Implement the actual payment processing logic here
    }

    @Override
    public void reserveInventory(String orderId) {
        System.out.println("Reserving inventory for order: " + orderId);
        sleep(5); // simulate API call processing time
        // Implement the actual inventory reservation logic here

        // Simulate inventory reservation failure
        //    Exception e = new RuntimeException("Inventory reservation failed");
        //    throw ApplicationFailure.newNonRetryableFailure(e.getMessage(), e.getClass().getName());
    }

    @Override
    public void deliverOrder(String orderId) {
        System.out.println("Delivering order: " + orderId);
        sleep(5); // simulate API call processing time
        // Implement the actual order delivery logic here

        // Simulate delivery failure
            Exception e = new RuntimeException("Delivery failed");
            throw ApplicationFailure.newNonRetryableFailure(e.getMessage(), e.getClass().getName());
    }

    @Override
    public void refundPayment(String orderId) {
        sleep(5); // simulate API call processing time
        System.out.println("Refunding payment for order: " + orderId);
        // Implement the actual payment refund logic here
    }

    @Override
    public void restockInventory(String orderId) {
        sleep(5); // simulate API call processing time
        System.out.println("Restocking inventory for order: " + orderId);
        // Implement the actual inventory restocking logic here
    }

    @Override
    public void cancelDelivery(String orderId) {
        sleep(5); // simulate API call processing time
        System.out.println("Reverting order: " + orderId);
        // Implement the actual order reversion logic here
    }

    private void sleep(int seconds) {
        try {
            // a random number between 800 and 1200
            // to simulate variance in API call time
            long sleepTime = (long) (Math.random() * 400) + 800;

            Thread.sleep(seconds * sleepTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}