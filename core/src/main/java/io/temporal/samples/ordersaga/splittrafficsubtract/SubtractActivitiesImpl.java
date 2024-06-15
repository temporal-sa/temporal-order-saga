package io.temporal.samples.ordersaga.splittrafficsubtract;

import io.temporal.samples.ordersaga.dataclasses.SKUQuantity;
import io.temporal.samples.ordersaga.dataclasses.SplitSKUTraffic;

import java.util.ArrayList;
import java.util.List;

public class SubtractActivitiesImpl implements SubtractActivities {

    // divide list of SKUs into two lists based on the proportion
    // will be used to split the traffic into two systems
    @Override
    public SplitSKUTraffic splitTrafficBySKU(List<SKUQuantity> listSKUQty, double legacyTrafficProportion) {
        List<SKUQuantity> legacyList = new ArrayList<>();
        List<SKUQuantity> newList = new ArrayList<>();

        int totalSKUs = listSKUQty.size();
        int legacySKUs = (int) Math.floor(totalSKUs * legacyTrafficProportion);

        for (int i = 0; i < listSKUQty.size(); i++) {
            if (i < legacySKUs) {
                legacyList.add(listSKUQty.get(i));
            } else {
                newList.add(listSKUQty.get(i));
            }
        }

        return new SplitSKUTraffic(legacyList, newList);
    }

    @Override
    public String subtractUsingLegacy(List<SKUQuantity> legacySKUs) {
        // Mock implementation: return a string indicating the activity was performed
        String result = "Legacy subtract completed for SKUs: " + legacySKUs;
        System.out.println(result);

        // Uncomment to test saga compensation
        // This error is set as non-retryable, so the activity will not be retried
//        throw ApplicationFailure.newNonRetryableFailureWithCause(
//                "Subtract failed in legacy system",
//                "Subtract failed in legacy system",
//                new RuntimeException("Exception in subtractUsingLegacy"));

        return result;
    }

    @Override
    public String subtractUsingNew(List<SKUQuantity> newSKUs) {
        // Mock implementation: return a string indicating the activity was performed
        String result = "New subtract completed for SKUs: " + newSKUs;
        System.out.println(result);

        // Uncomment to test saga compensation
        // This error is set as non-retryable, so the activity will not be retried
//        throw ApplicationFailure.newNonRetryableFailureWithCause(
//                "Subtract failed in new system",
//                "Subtract failed in new system",
//                new RuntimeException("Exception in subtractUsingLegacy"));

        return result;
    }

    @Override
    public void subtractUsingLegacyReverse(List<SKUQuantity> legacySKUs) {
        // Mock implementation: return a string indicating the activity was performed
        String result = "Legacy reverse subtract completed for SKUs: " + legacySKUs;
        System.out.println(result);

    }

    @Override
    public void subtractUsingNewReverse(List<SKUQuantity> newSKUs) {
        // Mock implementation: return a string indicating the activity was performed
        String result = "New reverse subtract completed for SKUs: " + newSKUs;
        System.out.println(result);

    }
}
