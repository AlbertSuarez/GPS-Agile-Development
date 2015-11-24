package edu.upc.essi.gps.domain;

public abstract class Discount implements Entity, HasName {

    protected final Product trigger;
    private final String name;
    private final long id;
    private final int barCode; //valor -1 reservat per a descomptes temporals (no emmagatzemats)

    public Discount(Product product, String name, int barCode, long id) {
        trigger = product;
        this.id = id;
        this.name = name;
        this.barCode = barCode;
    }

    @Override
    public long getId() {
        return id;
    }

    public abstract double getDiscount();

    public Product getTrigger() {
        return trigger;
    }

    @Override
    public String getName() {
        return name;
    }

    public int getBarCode() {
        return barCode;
    }
}
