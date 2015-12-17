package edu.upc.essi.gps.domain.discounts;

import edu.upc.essi.gps.domain.Category;
import edu.upc.essi.gps.domain.Product;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.DoubleAccumulator;
import java.util.stream.Collectors;

import static edu.upc.essi.gps.utils.DiscountCalculator.*;

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
        this(Collections.singletonList(product), name, id, percent);
    }

    /**
     * Crea una nova instància d'un descompte per percentatge a partir d'un producte.
     * @param category categoria de producte a la que es vol aplicar el descompte.
     * @param name nom del descompte.
     * @param id identificador del descompte al sistema.
     * @param percent tant per cent de descompte que s'ha d'aplicar al producte.
     * */
    public ProductPercent(Category category, String name, long id, double percent) {
        this(category.getProductes(), name, id, percent);
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
    public DiscountHolder calculate(List<Product> productes) {

        final DoubleAccumulator desc = new DoubleAccumulator((a, b) -> a+b, 0d);

        productes
                .stream()
                .filter(product -> isTriggeredBy(product.getId()))
                .map(product -> product.getPrice() * (percent / 100d))
                .forEach(desc::accumulate);

        List<Product> utilitzats = productes
                .stream()
                .filter(product -> isTriggeredBy(product.getId()))
                .collect(Collectors.toList());

        return new DiscountHolder(desc.doubleValue(), utilitzats);
    }

    @Override
    public boolean isTriggeredBy(long productId) {
        return triggers.stream()
                .map(Product::getId)
                .anyMatch(id -> id == productId);
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
