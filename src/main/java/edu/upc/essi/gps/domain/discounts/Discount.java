package edu.upc.essi.gps.domain.discounts;

import edu.upc.essi.gps.domain.Entity;
import edu.upc.essi.gps.domain.HasName;
import edu.upc.essi.gps.domain.Product;
import edu.upc.essi.gps.utils.DiscountHolder;

import java.util.List;

/**
 * Interficie per descomptes
 * */
public interface Discount extends Entity, HasName {

    /**
     * Calcula la millor aplicació del descompte al conjunt de productes donat.
     */
    DiscountHolder calculate(List<Product> productes);

    /**
     * Is product contained?
     */
    boolean isTriggeredBy(long productId);

    /**
     * Gets all products required for the Discount to apply (Present)
     * emmmm una interficie que depen de la seva implementació no es gaire guay... :(
     */
    List<Product> requiresProducts();

}
