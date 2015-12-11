package edu.upc.essi.gps.domain.discounts;

import edu.upc.essi.gps.domain.Product;
import edu.upc.essi.gps.domain.lines.SaleLine;

import java.util.Arrays;
import java.util.List;

/**
 * Classe que representa un descompte del tipus amb el producte A et regalem B.
 * */
public class Present implements Discount {

    /**
     * Nom que identifica aquesta classe com a un tipus concret de descompte.
     * */
    public static final String TYPE_NAME = "present";
    /**
     * Producte que cal comprar per a que es regali el disparador.
     * */
    private final Product required;
    private final long id;
    private final String name;
    private final Product trigger;

    /**
     * Crea una nova instància d'un descompte per regal a partir d'un producte.
     * @param product producte amb el qual s'asocia el descompte.
     * @param name nom del descompte.
     * @param id identificador del descompte al sistema.
     * @param required producte que cal comprar per a que es regali el disparador.
     * */
    public Present(Product product, String name, long id, Product required) {
        trigger = product;
        this.name = name;
        this.id = id;
        this.required = required;
    }

    public double getDiscount() {
        return -trigger.getPrice();
    }

    @Override
    public double calculate(SaleLine saleLine) {
        return 0;
    }

    @Override
    public boolean contains(long productId) {
        return trigger.getId() == productId || required.getId() == productId;
    }

    @Override
    public List<Product> requires() {
        return Arrays.asList(trigger, required);
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
