package edu.upc.essi.gps.domain;

import edu.upc.essi.gps.domain.Entity;
import edu.upc.essi.gps.domain.HasName;

public class Product implements Entity, HasName {

    private long id;
    private String name;
    private final int price;
    private final int vatPct;
    private final int barCode;

    private float popularity;

    public Product(long id, String name, int price, int vatPct, int barCode) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.vatPct = vatPct;
        this.barCode = barCode;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getPopularity() {
        return popularity;
    }

    public int getPrice() {
        return price;
    }

    public int getVatPct() {
        return vatPct;
    }

    public int getBarCode() {
        return barCode;
    }
}
