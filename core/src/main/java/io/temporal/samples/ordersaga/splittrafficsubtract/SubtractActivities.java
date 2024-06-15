package io.temporal.samples.ordersaga.splittrafficsubtract;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import io.temporal.samples.ordersaga.dataclasses.SKUQuantity;
import io.temporal.samples.ordersaga.dataclasses.SplitSKUTraffic;

import java.util.List;

@ActivityInterface
public interface SubtractActivities {

  @ActivityMethod
  SplitSKUTraffic splitTrafficBySKU(List<SKUQuantity> listSKUQty,
                                    double legacyTrafficProportion);

  @ActivityMethod
  String subtractUsingLegacy(List<SKUQuantity> legacySKUs);

  @ActivityMethod
  void subtractUsingLegacyReverse(List<SKUQuantity> legacySKUs);

  @ActivityMethod
  String subtractUsingNew(List<SKUQuantity> newSKUs);

  @ActivityMethod
  void subtractUsingNewReverse(List<SKUQuantity> newSKUs);
}
