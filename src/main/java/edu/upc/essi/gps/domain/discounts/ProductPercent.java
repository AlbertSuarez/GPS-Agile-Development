package edu.upc.essi.gps.domain.discounts;

import edu.upc.essi.gps.domain.Product;
import edu.upc.essi.gps.domain.Sale;
import edu.upc.essi.gps.domain.lines.SaleLine;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Classe que representa un descompte del tipus x% (10% de descompte, 23% de descompte...).
 * */
public class ProductPercent implements Discount {

    /**
     * Tant per cent de descompte que s'ha d'aplicar al producte.
     */
    private final double percent;
    private final List<Product> triggers;
    private final String name;
    private final long id;

    /**
     * Crea una nova instància d'un descompte per percentatge a partir d'un producte.
     * @param product productes amb el quals s'asocia el descompte.
     * @param name nom del descompte.
     * @param id identificador del descompte al sistema.
     * @param percent tant per cent de descompte que s'ha d'aplicar al producte.
     * */
    public ProductPercent(Product product, String name, long id, double percent) {
        triggers = Collections.singletonList(product);
        this.name = name;
        this.id = id;
        this.percent = percent;
    }

    /**
     * Crea una nova instància d'un descompte per percentatge a partir d'un producte.
     * @param product productes amb el quals s'asocia el descompte.
     * @param name nom del descompte.
     * @param id identificador del descompte al sistema.
     * @param percent tant per cent de descompte que s'ha d'aplicar al producte.
     * */
    public ProductPercent(List<Product> product, String name, long id, double percent) {
        triggers = product;
        this.name = name;
        this.id = id;
        this.percent = percent;
    }

    @Override
    public double calculate(Sale sale, int line) {
        SaleLine saleLine = sale.getLines().get(line);
        return saleLine.getProduct().getPrice() * saleLine.getAmount() * (percent / 100d);
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
