package edu.upc.essi.gps.domain.discounts;

import edu.upc.essi.gps.domain.Product;
import edu.upc.essi.gps.domain.lines.SaleLine;

import java.util.Collections;
import java.util.List;

/**
 * Classe que representa un descompte del tipus x% (10% de descompte, 23% de descompte...).
 * */
public class Percent implements Discount {

    /**
     * Nom que identifica aquesta classe com a un tipus concret de descompte.
     * */
    public static final String TYPE_NAME = "percent";
    /**
     * Tant per cent de descompte que s'ha d'aplicar al producte.
     */
    private final double percent;
    private final Product trigger;
    private final String name;
    private final long id;

    /**
     * Crea una nova instància d'un descompte per percentatge a partir d'un producte.
     * @param product producte amb el qual s'asocia el descompte.
     * @param name nom del descompte.
     * @param id identificador del descompte al sistema.
     * @param percent tant per cent de descompte que s'ha d'aplicar al producte.
     * */
    public Percent(Product product, String name, long id, double percent) {
        trigger = product;
        this.name = name;
        this.id = id;
        this.percent = percent;
    }

    public double getDiscount() {
        return -trigger.getPrice()*percent/100;
    }

    @Override
    public double calculate(SaleLine saleLine) {
        return saleLine.getProduct().getPrice() * saleLine.getAmount() * percent / 100;
    }

    @Override
    public boolean contains(long productId) {
        return trigger.getId() == productId;
    }

    @Override
    public List<Product> requires() {
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
