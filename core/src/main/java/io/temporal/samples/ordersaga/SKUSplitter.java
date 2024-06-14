package io.temporal.samples.ordersaga;

import io.temporal.samples.ordersaga.dataclasses.SKUQuantity;
import io.temporal.samples.ordersaga.dataclasses.SplitSKUTraffic;

import java.util.ArrayList;
import java.util.List;

public class SKUSplitter {

    public static SplitSKUTraffic splitTrafficBySKU(List<SKUQuantity> listSKUQty, double legacyTrafficProportion) {
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

    public static void main(String[] args) {
        List<SKUQuantity> skus = new ArrayList<>();
        skus.add(new SKUQuantity("sku1001", -100));
        skus.add(new SKUQuantity("sku2002", -50));
        skus.add(new SKUQuantity("sku3003", -200));

        double legacyProportion = 0.35;

        SplitSKUTraffic result = splitTrafficBySKU(skus, legacyProportion);
        System.out.println(result);
    }
}
