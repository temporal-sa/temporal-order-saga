package io.temporal.samples.ordersaga;

import io.temporal.samples.ordersaga.dataclasses.SKUQuantity;

import java.util.List;

public class OrderActivitiesImpl implements OrderActivities {

    @Override
    public String subtractUsingLegacy(List<SKUQuantity> legacySKUs) {
        // Mock implementation: return a string indicating the activity was performed
        String result = "Legacy subtract completed for SKUs: " + legacySKUs;
        System.out.println(result);

        // Uncomment to test saga compensation
        // throw new RuntimeException("Exception in subtractUsingLegacy");

        return result;
    }

    @Override
    public String subtractUsingNew(List<SKUQuantity> newSKUs) {
        // Mock implementation: return a string indicating the activity was performed
        String result = "New subtract completed for SKUs: " + newSKUs;
        System.out.println(result);

        // Uncomment to test saga compensation
        // throw new RuntimeException("Exception in subtractUsingNew");

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
