package io.temporal.samples.ordersaga.dataclasses;

public class SKUQuantity {
    private String sku;
    private int quantity;

    // Default constructor
    public SKUQuantity() {
    }

    // Parameterized constructor
    public SKUQuantity(String sku, int quantity) {
        this.sku = sku;
        this.quantity = quantity;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return sku + ": " + quantity;
    }
}
