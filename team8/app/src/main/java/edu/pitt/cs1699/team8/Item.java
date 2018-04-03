package edu.pitt.cs1699.team8;

public class Item {

    private String name;
    private double price;
    private long quantity;

    public Item(String n, double p, long q) {
        this.name = n;
        this.price = p;
        this.quantity = q;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        String out = String.format("Item:\t%s\n\nPrice:\t%.2f\nQuantity:\t%d", this.name, this.price, this.quantity);
        return out;
    }
}
