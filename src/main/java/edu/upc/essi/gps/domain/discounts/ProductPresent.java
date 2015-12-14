package edu.upc.essi.gps.domain.discounts;

import edu.upc.essi.gps.domain.Product;
import edu.upc.essi.gps.domain.Sale;
import edu.upc.essi.gps.domain.lines.SaleLine;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.DoubleAccumulator;

/**
 * Classe que representa un descompte del tipus amb el producte A et regalem B.
 * */
public class ProductPresent implements Discount {

    /**
     * Producte que cal comprar per a que es regali el disparador.
     * */
    private final List<Product> required;
    private final long id;
    private final String name;
    private final List<Product> triggers;

    /**
     * Crea una nova instància d'un descompte per regal a partir d'un producte.
     * @param product producte amb el qual s'asocia el descompte.
     * @param name nom del descompte.
     * @param id identificador del descompte al sistema.
     * @param required producte que cal comprar per a que es regali el disparador.
     * */
    public ProductPresent(Product product, String name, long id, Product required) {
        triggers = Collections.singletonList(product);
        this.name = name;
        this.id = id;
        this.required = Collections.singletonList(required);
    }

    /**
     * Crea una nova instància d'un descompte per regal a partir d'un producte.
     * @param product producte amb el qual s'asocia el descompte.
     * @param name nom del descompte.
     * @param id identificador del descompte al sistema.
     * @param required producte que cal comprar per a que es regali el disparador.
     * */
    public ProductPresent(List<Product> product, String name, long id, List<Product> required) {
        triggers = product;
        this.name = name;
        this.id = id;
        this.required = required;
    }

    @Override
    public double calculate(Sale sale, int line) {
        SaleLine saleLine = sale.getLines().get(line);

        DoubleAccumulator discount = new DoubleAccumulator((a, b) -> a+b, 0d);

        required.stream()
                .forEach((product) -> discount.accumulate(product.getPrice()));

        return discount.doubleValue();
    }

    @Override
    public boolean isTriggeredBy(long productId) {
        return triggers
                .stream()
                .anyMatch((p) -> p.getId() == productId);
    }

    @Override
    public List<Product> requiresProducts() {
        return required;
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
