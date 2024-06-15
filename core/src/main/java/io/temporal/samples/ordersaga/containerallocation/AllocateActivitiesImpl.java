package io.temporal.samples.ordersaga.containerallocation;

import java.util.Arrays;
import java.util.List;

import io.temporal.failure.ApplicationFailure;

public class AllocateActivitiesImpl implements AllocateActivities {

    // divide list of SKUs into two lists based on the proportion
    // will be used to split the traffic into two systems
    @Override
    public List<String> getContainerRecommendation(String sku, int qty) {
        // Hardcoded demo function
        return Arrays.asList("container1", "container2", "container3", "container4");
    }

    @Override
    public int allocate(String container, String sku, int qty) {
        // Subtract 100 from qty
        int newQty = qty;
        if (newQty < 100) {
            newQty = 0; // allocate all remaining
        } else {
            newQty = qty - 100;
        }
        System.out.println("Allocated " + (qty - newQty) + " of SKU " + sku + " to container " + container);
        return newQty;
    }

    @Override
    public int allocateReverse(String container, String sku, int qty) {
        // Add 100 to qty
        int newQty = qty + 100;
        System.out.println("Reversed allocation of 100 for SKU " + sku + " to container " + container + ". New quantity: " + newQty);
        return newQty;
    }
}
