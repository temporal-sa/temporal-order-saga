package io.temporal.samples.ordersaga.dataclasses;

import java.util.List;

public class SplitSKUTraffic {
    private List<SKUQuantity> legacySKUs;
    private List<SKUQuantity> newSKUs;

    // Default constructor
    public SplitSKUTraffic() {
    }

    public SplitSKUTraffic(List<SKUQuantity> legacy, List<SKUQuantity> newTraffic) {
        this.legacySKUs = legacy;
        this.newSKUs = newTraffic;
    }

    public List<SKUQuantity> getLegacySKUs() {
        return legacySKUs;
    }

    public List<SKUQuantity> getNewSKUs() {
        return newSKUs;
    }

    @Override
    public String toString() {
        return "Legacy: " + legacySKUs + "\nNew: " + newSKUs;
    }
}
