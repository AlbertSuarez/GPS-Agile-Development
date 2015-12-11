package edu.upc.essi.gps.domain.discounts;

import edu.upc.essi.gps.domain.Product;
import edu.upc.essi.gps.domain.Sale;

import java.util.Collections;
import java.util.List;

/**
 * Classe que representa un descompte del tipus AxB (3x2, 2x1...).
 * */
public class Promotion implements Discount {


    /**
     * Nom que identifica aquesta classe com a un tipus concret de descompte.
     */
    public static final String TYPE_NAME = "promotion";
    private final long id;
    private final String name;
    /**
     * Quantitat de producte que reps si s'aplica la promoció.
     * */
    private final int productsFree;
    /**
     * Quantitat de producte que has de comprar per a que s'apliqui la promoció.
     * */
    private final int requiredProducts;
    private Product trigger;

    /**
     * Crea una nova instància d'una promoció a partir d'un producte.
     * @param product producte amb el qual s'asocia el descompte.
     * @param name nom del descompte.
     * @param id identificador del descompte al sistema.
     * @param productsFree quantitat de producte que reps si s'aplica la promoció.
     * @param requiredProducts quantitat de producte que has de comprar per a que s'apliqui la promoció.
     * */
    public Promotion(Product product, String name, long id, int productsFree, int requiredProducts) {
        trigger = product;
        this.name = name;
        this.id = id;
        this.productsFree = productsFree;
        this.requiredProducts = requiredProducts;
    }

    public double getDiscount() {
        return -trigger.getPrice() * (productsFree - requiredProducts);
    }

    @Override
    public double calculate(Sale currentSale) {
        return 0;
    }

    @Override
    public boolean contains(long productId) {
        return trigger.getId() == productId;
    }

    @Override
    public List<Product> appliedTo() {
        return Collections.singletonList(trigger);
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }
}
