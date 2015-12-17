package edu.upc.essi.gps.domain.discounts;

import edu.upc.essi.gps.domain.Category;
import edu.upc.essi.gps.domain.Product;

import java.util.*;
import java.util.concurrent.atomic.DoubleAccumulator;
import java.util.stream.Collectors;

import static edu.upc.essi.gps.utils.DiscountCalculator.*;

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
        this(Collections.singletonList(product), name, id, productsFree, requiredProducts);
    }

    /**
     * Crea una nova instància d'una promoció a partir d'un producte.
     * @param category categoria de producte a la que es vol aplicar el descompte.
     * @param name nom del descompte.
     * @param id identificador del descompte al sistema.
     * @param productsFree quantitat de producte que reps si s'aplica la promoció.
     * @param requiredProducts quantitat de producte que has de comprar per a que s'apliqui la promoció.
     * */
    public ProductPromotion(Category category, String name, long id, int productsFree, int requiredProducts) {
        this(category.getProductes(), name, id, productsFree, requiredProducts);
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
    public DiscountHolder calculate(List<Product> productes) {

        final DoubleAccumulator desc = new DoubleAccumulator((a, b) -> a+b, 0d);

        final List<Product> seleccionats = new LinkedList<>();

        productes.stream()
                .filter(product -> isTriggeredBy(product.getId()))
                .forEach(seleccionats::add);


        int amount = seleccionats.size();

        if (amount < requiredProducts) return new DiscountHolder(0d, new LinkedList<>());

        int times =  amount / productsObtained;

        int freeProducts = (productsObtained - requiredProducts) * times;

        int selectedProducts = productsObtained * times;

        List<Product> utilitzats = seleccionats
                .stream()
                .sorted((o1, o2) -> ((Double) o1.getPrice()).compareTo(o2.getPrice()))
                .limit(selectedProducts)
                .collect(Collectors.toList());

        seleccionats
                .stream()
                .sorted((o1, o2) -> ((Double) o1.getPrice()).compareTo(o2.getPrice()))
                .limit(freeProducts)
                .map(Product::getPrice)
                .forEach(desc::accumulate);

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
