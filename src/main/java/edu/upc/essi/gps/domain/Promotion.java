package edu.upc.essi.gps.domain;

import java.util.List;

/**
 * Classe que representa un descompte del tipus AxB (3x2, 2x1...).
 * */
public class Promotion extends Discount {

    /**
     * Quantitat de producte que reps si s'aplica la promoció.
     * */
    private final int A;

    /**
     * Quantitat de producte que has de comprar per a que s'apliqui la promoció.
     * */
    private final int B;

    /**
     * Nom que identifica aquesta classe com a un tipus concret de descompte.
     * */
    public static final String TYPE_NAME = "promotion";

    /**
     * Crea una nova instància d'una promoció a partir d'un producte.
     * @param product producte amb el qual s'asocia el descompte.
     * @param name nom del descompte.
     * @param id identificador del descompte al sistema.
     * @param A quantitat de producte que reps si s'aplica la promoció.
     * @param B quantitat de producte que has de comprar per a que s'apliqui la promoció.
     * */
    public Promotion(Product product, String name, long id, int A, int B) {
        super(product, name, id);
        this.A = A;
        this.B = B;
    }

    @Override
    public double getDiscount() {
        return -trigger.getPrice()*(A-B);
    }

    @Override
    public boolean checkSale(Sale currentSale) {
        List<Sale.SaleLine> list = currentSale.getLines();
        Sale.SaleLine line = list.get(list.size() - 1);
        return line.getId() == trigger.getId() && line.getAmount() >= A;
    }

    @Override
    public int getAmount(Sale currentSale) {
        if (!checkSale(currentSale)) return 0;
        List<Sale.SaleLine> list = currentSale.getLines();
        Sale.SaleLine line = list.get(list.size()-1);
        return line.getAmount()/A;
    }

}
