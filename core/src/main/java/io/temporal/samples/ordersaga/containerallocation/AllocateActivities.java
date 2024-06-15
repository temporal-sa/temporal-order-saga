package io.temporal.samples.ordersaga.containerallocation;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

import java.util.List;

@ActivityInterface
public interface AllocateActivities {

    @ActivityMethod
    List<String> getContainerRecommendation(String sku, int qty);

    @ActivityMethod
    int allocate(String container, String sku, int qty);

    @ActivityMethod
    int allocateReverse(String container, String sku, int qty);
}
