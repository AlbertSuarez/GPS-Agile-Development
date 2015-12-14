package edu.upc.essi.gps.domain.discounts;

import edu.upc.essi.gps.domain.Entity;
import edu.upc.essi.gps.domain.HasName;
import edu.upc.essi.gps.domain.Product;
import edu.upc.essi.gps.domain.Sale;
import edu.upc.essi.gps.domain.lines.SaleLine;

import java.util.List;

/**
 * Interficie per descomptes
 * */
public interface Discount extends Entity, HasName {

    /**
     * Calculate money to discount
     */
    double calculate(Sale sale, int line);

    /**
     * Is product contained?
     *
     * @param productId
     * @return
     */
    boolean isTriggeredBy(long productId);

    /**
     * Gets all products required for the Discount to apply (Present)
     * emmmm una interficie que depen de la seva implementació no es gaire guay... :(
     *
     * @return
     */
    List<Product> requiresProducts();

}
