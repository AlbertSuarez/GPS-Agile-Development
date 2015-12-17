package edu.upc.essi.gps.domain.discounts;

import edu.upc.essi.gps.domain.Category;
import edu.upc.essi.gps.domain.Product;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.DoubleAccumulator;

import static edu.upc.essi.gps.utils.DiscountCalculator.*;

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
    private final List<Product> presents;

    /**
     * Crea una nova instància d'un descompte per regal a partir d'un producte.
     * @param product producte amb el qual s'asocia el descompte.
     * @param name nom del descompte.
     * @param id identificador del descompte al sistema.
     * @param required producte que cal comprar per a que es regali el disparador.
     * */
    public ProductPresent(Product product, String name, long id, Product required) {
        this(Collections.singletonList(product), name, id, Collections.singletonList(required));
    }

    /**
     * Crea una nova instància d'un descompte per regal a partir d'un producte.
     * @param categoryPresent categoria de producte que es vol regalar amb el descompte.
     * @param name nom del descompte.
     * @param id identificador del descompte al sistema.
     * @param categoryRequired categoria de producte a la que es vol aplicar el descompte.
     * */
    public ProductPresent(Category categoryPresent, String name, long id, Category categoryRequired) {
        this(categoryPresent.getProductes(), name, id, categoryRequired.getProductes());
    }

    /**
     * Crea una nova instància d'un descompte per regal a partir d'un producte.
     * @param presents producte amb el qual s'asocia el descompte.
     * @param name nom del descompte.
     * @param id identificador del descompte al sistema.
     * @param required producte que cal comprar per a que es regali el disparador.
     * */
    public ProductPresent(List<Product> presents, String name, long id, List<Product> required) {
        this.presents = presents;
        this.name = name;
        this.id = id;
        this.required = required;
    }

    @Override
    public DiscountHolder calculate(List<Product> productes) {
        //PRE: per a cada producte de productes que compleix isTriggeredBy(producte.getId()),
        // a productes apareixerà una vegada cada producte de presents.

        DoubleAccumulator desc = new DoubleAccumulator((a, b) -> a+b, 0d);

        final List<Product> utilitzats = new LinkedList<>();


        Long amount = productes
                .stream()
                .map(Product::getId)
                .filter(this::isTriggeredBy)
                .count();


        presents.stream()
                .map(Product::getPrice)
                .forEach(desc::accumulate);


        Double presentsDiscount = desc.doubleValue();
        Double discount = 0d;

        for (int i = 0; i < amount; ++i) {
            discount += presentsDiscount;
            utilitzats.addAll(presents);
        }

        return new DiscountHolder(discount, utilitzats);
    }

    @Override
    public boolean isTriggeredBy(long productId) {
        return required.stream()
                .map(Product::getId)
                .anyMatch(id -> id == productId);
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
