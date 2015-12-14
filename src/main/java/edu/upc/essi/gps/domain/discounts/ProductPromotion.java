package edu.upc.essi.gps.domain.discounts;

import edu.upc.essi.gps.domain.Product;
import edu.upc.essi.gps.domain.Sale;
import edu.upc.essi.gps.domain.lines.SaleLine;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Classe que representa un descompte del tipus AxB (3x2, 2x1...).
 * */
public class ProductPromotion implements Discount {


    /**
     * Nom que identifica aquesta classe com a un tipus concret de descompte.
     */
    private final long id;
    private final String name;
    /**
     * Quantitat de producte que reps si s'aplica la promoció.
     * */
    private final int productsObtained;
    /**
     * Quantitat de producte que has de comprar per a que s'apliqui la promoció.
     * */
    private final int requiredProducts;
    private List<Product> triggers;

    /**
     * Crea una nova instància d'una promoció a partir d'un producte.
     * @param product producte amb el qual s'asocia el descompte.
     * @param name nom del descompte.
     * @param id identificador del descompte al sistema.
     * @param productsFree quantitat de producte que reps si s'aplica la promoció.
     * @param requiredProducts quantitat de producte que has de comprar per a que s'apliqui la promoció.
     * */
    public ProductPromotion(Product product, String name, long id, int productsFree, int requiredProducts) {
        triggers = new LinkedList<>();
        triggers.add(product);
        this.name = name;
        this.id = id;
        this.productsObtained = productsFree;
        this.requiredProducts = requiredProducts;
    }

    /**
     * Crea una nova instància d'una promoció a partir d'un producte.
     * @param products producte amb el qual s'asocia el descompte.
     * @param name nom del descompte.
     * @param id identificador del descompte al sistema.
     * @param productsFree quantitat de producte que reps si s'aplica la promoció.
     * @param requiredProducts quantitat de producte que has de comprar per a que s'apliqui la promoció.
     * */
    public ProductPromotion(List<Product> products, String name, long id, int productsFree, int requiredProducts) {
        triggers = products;
        this.name = name;
        this.id = id;
        this.productsObtained = productsFree;
        this.requiredProducts = requiredProducts;
    }

    @Override
    public double calculate(Sale sale, int line) {
        List<SaleLine> saleLines = sale.getLines();
        SaleLine saleLine = saleLines.get(line);

        int amount = saleLine.getAmount();
        if (amount < requiredProducts)
            return 0;

        int freeProducts = Math.min(productsObtained - requiredProducts, (amount - requiredProducts));

        int times = amount / productsObtained;
        return times * freeProducts * saleLine.getProduct().getPrice();
    }

    @Override
    public boolean isTriggeredBy(long productId) {
        return triggers
                .stream()
                .anyMatch((p) -> p.getId() == productId);
    }

    @Override
    public List<Product> requiresProducts() {
        return Collections.emptyList();
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
