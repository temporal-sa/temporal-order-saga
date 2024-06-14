package io.temporal.samples.ordersaga;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import io.temporal.samples.ordersaga.dataclasses.SKUQuantity;

import java.util.List;

@ActivityInterface
public interface OrderActivities {

  @ActivityMethod
  String subtractUsingLegacy(List<SKUQuantity> legacySKUs);

  @ActivityMethod
  void subtractUsingLegacyReverse(List<SKUQuantity> legacySKUs);

  @ActivityMethod
  String subtractUsingNew(List<SKUQuantity> newSKUs);

  @ActivityMethod
  void subtractUsingNewReverse(List<SKUQuantity> newSKUs);
}
