package edu.upc.essi.gps.domain;

/**
 * Aplica un descompte del tipus AxB (3x2, 2x1...).
 * */
public class Promotion extends Discount {

    private final int A;
    private final int B;

    public static final String TYPE_NAME = "promotion";

    public Promotion(Product product, String name, int barCode, long id, int A, int B) {
        super(product, name, barCode, id);
        this.A = A;
        this.B = B;
    }

    @Override
    public double getDiscount() {
        return -trigger.getPrice()*(A-B);
    }

}
