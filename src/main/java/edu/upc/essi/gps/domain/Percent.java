package edu.upc.essi.gps.domain;

public class Percent extends Discount {

    private final double percent;

    public static final String TYPE_NAME = "discount";

    public Percent(Product product, String name, int barCode, long id, double percent) {
        super(product, name, barCode, id);
        this.percent = percent;
    }

    @Override
    public double getDiscount() {
        return -trigger.getPrice()*percent/100;
    }
}
