package io.temporal.samples.ordersaga.dataclasses;

import java.util.List;

public class SplitSKUTraffic {
    private List<SKUQuantity> legacy;
    private List<SKUQuantity> newTraffic;

    public SplitSKUTraffic(List<SKUQuantity> legacy, List<SKUQuantity> newTraffic) {
        this.legacy = legacy;
        this.newTraffic = newTraffic;
    }

    public List<SKUQuantity> getLegacySKUs() {
        return legacy;
    }

    public List<SKUQuantity> getNewSKUs() {
        return newTraffic;
    }

    @Override
    public String toString() {
        return "Legacy: " + legacy + "\nNew: " + newTraffic;
    }
}
