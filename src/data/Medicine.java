package data;

public class Medicine {
    public final String pharmacy;
    public final String name;
    public final double price;
    public int qty; // Changed to non-final since stock can be reduced upon reservation
    public final String expiry;
    public final String deliveryTime; // e.g., "Fast Delivery Possible"

    public Medicine(String pharmacy, String name, double price, int qty, String expiry, String deliveryTime) {
        this.pharmacy = pharmacy;
        this.name = name;
        this.price = price;
        this.qty = qty;
        this.expiry = expiry;
        this.deliveryTime = deliveryTime;
    }
}